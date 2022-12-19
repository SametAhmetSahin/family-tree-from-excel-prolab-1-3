extends Node2D


#[id, name, surname, birthdate, spouse, motherName, fatherName, bloodType, profession, maritalStatus, maidenName, gender]
export (Dictionary) var person
export (Dictionary) var data = {"id":"id", "name":"name", "surname":"surname", "birthdate":"birthdate", "spouse":"spouse", "motherName":"motherName", "fatherName":"fatherName", "bloodType":"bloodType", "profession":"profession", "maritalStatus":"maritalStatus", "maidenName":"maidenName", "gender":"gender"}
export (bool) var root = false

func start_glow():
	$AnimationPlayer.play("glow" +  str(int(person["data"]["gender"])))
func stop_glow():
	$AnimationPlayer.play("normal" + str(int(person["data"]["gender"])))

func set_person(newperson):
	person = newperson
	update_labels()

func add_node(person: Dictionary, newpos: Vector2):
	var node_scene = load("res://Node.tscn")
	var newnode = node_scene.instance()
	newnode.set_person(person)
	newnode.position = newpos
	add_child(newnode)
	return newnode
	#call_deferred("add_child", newnode)
	
func set_parent_line(to: Vector2):
	$Line2D.points[1] = to
	pass
func set_spouse_line(to: Vector2):
	$Line2D.points[1] = to
	$Line2D.default_color = Color("ff6666")
	#print(person["data"].keys())
	#print(person["data"]["maritalStatus"])
	if ["Bekar", "Dul"].has(person["data"]["maritalStatus"]):
		$Line2D.width = 2
		$Line2D.default_color == Color("8205b4")
	pass

func generate_subnodes(startpos):
	var totalwidth = person["children"].size() * 240 + (person["children"].size()-1) * 80
	var offset = Vector2(-totalwidth/2, 0)
	#print(person["spouse"])
	for child in person["children"]:
		var childnode = add_node(child, offset + Vector2(0, 150))
		childnode.set_parent_line(-childnode.position + Vector2(120, 0))
		offset += childnode.generate_subnodes(offset) + Vector2(320, 0)
	if person.has("spouse"):
		var spousenode = add_node(person["spouse"], Vector2(260, 0))
		spousenode.set_spouse_line(Vector2(-320, 0))
		offset += Vector2(80, 0)
	if root:
		if person.has("mother"):
			print("person is root and has mother")
			if person["mother"] != 0:
				print({"data": Globalvars.TreeView.get_person_from_id(person["mother"])})
				var mothernode = add_node({"data": Globalvars.TreeView.get_person_from_id(person["mother"])}, Vector2(320, -150))
				mothernode.set_parent_line(-Vector2(320, -150))
				print("Added mother")
				pass
		if person.has("father"):
			print("person is root and has father")
			if person["father"] != 0:
				var fathernode = add_node({"data": Globalvars.TreeView.get_person_from_id(person["father"])}, Vector2(0, -150))
				fathernode.set_parent_line(-Vector2(0, -150))
				print("Added father")
				pass
	return offset

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
#		$AnimationPlayer.get_animation("glow").track_set_key_value(0, 0, Color("00b1ff"))
#		$AnimationPlayer.get_animation("glow").track_set_key_value(0, 3, Color("00b1ff"))
#		$AnimationPlayer.get_animation("normal").track_set_key_value(0, 0, Color("00b1ff"))
	else:
		$Panel.self_modulate = Color("f700ff")
		$Panel/TextureRect.self_modulate = Color("ff00f5")
#		$AnimationPlayer.get_animation("glow").track_set_key_value(0, 0, Color("f700ff"))
#		$AnimationPlayer.get_animation("glow").track_set_key_value(0, 3, Color("f700ff"))
#		$AnimationPlayer.get_animation("normal").track_set_key_value(0, 0, Color("f700ff"))
		
	
