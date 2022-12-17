extends Node


var cards = []

var response = ""

var gamestatus

var selectedCard

var human = true

var gamestatuscode=0

func setResponse(string):
	response = string
	print("setResponse ")# + string)

func UpdateGameStatus():
	print("Updating game status")
	print("response is:" + str(response))
	var parse_result: JSONParseResult = JSON.parse(response)
	print("parse ok")
	print(parse_result.error == OK)
	#print("parse_result " + str(parse_result))
	gamestatus = parse_result.get_result()
	print(typeof(gamestatus))
