extends Node


var response = ""

func setResponse(string):
	response = string
	print("setResponse ")# + string)

func UpdateGameStatus():
	print("Updating game status")
	print("response is:" + str(response))
	var parse_result: JSONParseResult = JSON.parse(response)
	print("parse ok")
	print(parse_result.error == OK)

onready var TreeView = get_node("/root/TreeView")
