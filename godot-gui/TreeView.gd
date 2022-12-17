extends Control

onready var requests = $Requests

onready var node_scene = preload("res://Node.tscn")

func add_node(data: Dictionary, position: Vector2):
	var newnode = node_scene.instance()
	newnode.set_data(data)
	newnode.rect_position = position
	add_child(newnode)

func wait_for_request(req):
	yield(req, "request_completed")

func _ready():
	var get_req = requests.get_request("http://localhost:8080/gettree")
	yield(get_req, "request_completed")
	var response_decoded = Marshalls.base64_to_utf8(Globalvars.response)
	#print("response: " + str(response_decoded) + " endresponse")
	var parse_result: JSONParseResult = JSON.parse(response_decoded)
	var result = parse_result.result
	var x = 0
	var y = 0
	for root in result:
		#print(root["rootNode"]["data"].keys())
		#print(str(root["rootNode"]["data"]["id"]) + " " + root["rootNode"]["data"]["name"] + " " + root["rootNode"]["data"]["surname"])
		var pos = Vector2(x, y)
		x += 100
		y += 200
		var person = root["rootNode"]["data"]
		var data = {"id": person["id"], "name": person["name"], "surname": person["surname"], "birthdate": person["birthdate"], "spouse": person["spouse"], "motherName": person["motherName"], "fatherName": person["fatherName"], "bloodType": person["bloodType"], "profession": person["profession"], "maritalStatus": person["maritalStatus"], "maidenName": person["maidenName"], "gender": person["gender"]}

		add_node(data, pos)
	
	


func _on_Button_pressed():
	var get_req = requests.get_request("http://localhost:8080/gettree")
	yield(get_req, "request_completed")
	print("response: " + str(Globalvars.response) + " endresponse")
