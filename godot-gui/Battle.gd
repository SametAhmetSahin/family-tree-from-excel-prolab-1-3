extends Control

onready var requests = $Requests

func wait_for_request(req):
	yield(req, "request_completed")

func _ready():

	if Globalvars.human:
	#wait_for_request(requests.post_request("http://localhost:8080/initializegame", {"\"deck\"": Globalvars.cards}))
		var post_req = requests.post_request("http://localhost:8080/initializegame", {"\"deck\"": Globalvars.cards})
		yield(post_req, "request_completed")
		print("response: " + str(Globalvars.response) + " endresponse")
	
	else:
		var post_req = requests.post_request("http://localhost:8080/initializegame", {"\"deck\"": [0, 1, 2, 2, 2]})
		yield(post_req, "request_completed")
		print("response: " + str(Globalvars.response) + " endresponse")
	updateGame()

func updateGame():
	var get_req1 = requests.get_request("http://localhost:8080/checkgamestatus?a=a")
	yield(get_req1, "request_completed")
	
	match Globalvars.response:
		1:
			Globalvars.gamestatuscode=1
			get_tree().change_scene("res://gameover.tscn")
		2:
			Globalvars.gamestatuscode=2
			get_tree().change_scene("res://gameover.tscn")
		3:
			Globalvars.gamestatuscode=3
			get_tree().change_scene("res://gameover.tscn")
	

	var get_req = requests.get_request("http://localhost:8080/getgamestatus?a=a")
	yield(get_req, "request_completed")
	Globalvars.UpdateGameStatus()
	print("gamestatus:" + str(Globalvars.gamestatus))
	$player1/Deck.updateDeck()
	$player2/Deck.updateDeck()

func _on_Button_pressed():
	if Globalvars.human:
		if Globalvars.selectedCard != null:
			print(Globalvars.selectedCard.id)
			var post_req = requests.post_request("http://localhost:8080/playaround", {"\"selection\"": Globalvars.selectedCard.id})
			yield(post_req, "request_completed")

			updateGame()
	else:
		var post_req = requests.post_request("http://localhost:8080/playaround", {"\"selection\"": 0})
		yield(post_req, "request_completed")

		updateGame()
		
