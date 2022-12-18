extends Control

onready var requests = $Requests

onready var node_scene = preload("res://Node.tscn")

func generate_tree(person: Dictionary, startpos: Vector2):
	#startpos = startpos
	var totalwidth = person["children"].size() * 240 + (person["children"].size()-1) * 80
	var offset = Vector2(totalwidth, 100)
	#print(person["children"])
	var rootnode = add_node(person, startpos)
	rootnode.set_parent_line(Vector2(0, 0))
	rootnode.root = true
	offset += rootnode.generate_subnodes(startpos + offset)
	offset += Vector2(320, 0)
	for child in person["children"]:
		#print(child.keys())
		#var childnode = rootnode.add_node(child, startpos + offset)
		#offset += childnode.generate_subnodes(startpos + offset)
		#offset += Vector2(320, 0)
		pass


func add_node(person: Dictionary, position: Vector2):
	var newnode = node_scene.instance()
	newnode.set_person(person)
	newnode.global_position = position
	$Tree.call_deferred("add_child", newnode)
	return newnode
	#add_child(newnode)

func wait_for_request(req):
	yield(req, "request_completed")


func _ready():
	var get_req = requests.get_request("http://localhost:8080/gettree")
	yield(get_req, "request_completed")
	var response_decoded = Marshalls.base64_to_utf8(Globalvars.response)
	#print("response: " + str(response_decoded) + " endresponse")
	var parse_result: JSONParseResult = JSON.parse(response_decoded)
	var result = parse_result.result
	var pos = Vector2(0, 0)
	for root in result:
		#print(root["rootNode"]["data"].keys())
		#print(str(root["rootNode"]["data"]["id"]) + " " + root["rootNode"]["data"]["name"] + " " + root["rootNode"]["data"]["surname"])
		var person = root["rootNode"]
		#var data = {"id": person["data"]["id"], "name": person["data"]["name"], "surname": person["data"]["surname"], "birthdate": person["data"]["birthdate"], "spouse": person["data"]["spouse"], "motherName": person["data"]["motherName"], "fatherName": person["data"]["fatherName"], "bloodType": person["data"]["bloodType"], "profession": person["data"]["profession"], "maritalStatus": person["data"]["maritalStatus"], "maidenName": person["data"]["maidenName"], "gender": person["data"]["gender"]}
		pos += Vector2(4000, 0)
		#add_node(data, pos)
		
		generate_tree(person, pos)
		
	


func _on_Button_pressed():
	var get_req = requests.get_request("http://localhost:8080/gettree")
	yield(get_req, "request_completed")
	print("response: " + str(Globalvars.response) + " endresponse")
