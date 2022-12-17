extends Control


#[id, name, surname, birthdate, spouse, motherName, fatherName, bloodType, profession, maritalStatus, maidenName, gender]

var data = {"id":"id", "name":"name", "surname":"surname", "birthdate":"birthdate", "spouse":"spouse", "motherName":"motherName", "fatherName":"fatherName", "bloodType":"bloodType", "profession":"profession", "maritalStatus":"maritalStatus", "maidenName":"maidenName", "gender":"gender"}

func set_data(newdata):
	
	data = newdata
	
	update_labels()

func update_labels():
	$Panel/id.text = str(data["id"])
	$Panel/fullname.text = data["name"] + " " + data["surname"]
	$Panel/birthdate.text = data["birthdate"]
	$Panel/bloodtype.text = data["bloodType"]
	$Panel/profession.text = data["profession"]
	$Panel/maidenname.text = data["maidenName"]
	
	print(data["gender"])
	# mavi 00b1ff
	#if data["gender"] == 0
	
	#$Panel.self_modulate = Color("00b1ff")


