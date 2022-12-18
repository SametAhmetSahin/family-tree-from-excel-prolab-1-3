extends Control


#[id, name, surname, birthdate, spouse, motherName, fatherName, bloodType, profession, maritalStatus, maidenName, gender]
var person
var data = {"id":"id", "name":"name", "surname":"surname", "birthdate":"birthdate", "spouse":"spouse", "motherName":"motherName", "fatherName":"fatherName", "bloodType":"bloodType", "profession":"profession", "maritalStatus":"maritalStatus", "maidenName":"maidenName", "gender":"gender"}


func set_person(newperson):
	person = newperson
	update_labels()

func add_node(person: Dictionary, position: Vector2):
	var node_scene = load("res://Node.tscn")
	var newnode = node_scene.instance()
	newnode.set_person(person)
	newnode.rect_position = position
	add_child(newnode)
	return newnode
	#call_deferred("add_child", newnode)

func generate_subnodes(startpos):
		
	var totalwidth = person["children"].size() * 240 + (person["children"].size()-1) * 80
	var offset = Vector2(totalwidth/2, 150)
	for child in person["children"]:
		var childnode = add_node(child, startpos + offset)
		offset += childnode.generate_subnodes(startpos + offset)
	return Vector2(totalwidth/2, 150)
	pass

func update_labels():
	$Panel/id.text = str(person["data"]["id"])
	$Panel/fullname.text = person["data"]["name"] + " " + person["data"]["surname"]
	$Panel/birthdate.text = person["data"]["birthdate"]
	$Panel/bloodtype.text = person["data"]["bloodType"]
	$Panel/profession.text = person["data"]["profession"]
	$Panel/maidenname.text = person["data"]["maidenName"]
	
	#print(person["data"]["gender"])
	#mavi 00b1ff
	if person["data"]["gender"]:
		$Panel.self_modulate = Color("00b1ff")
	else:
		$Panel.self_modulate = Color("f700ff")
