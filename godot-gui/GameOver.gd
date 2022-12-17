extends Control

func _ready():
	
	var requests = load("res://Requests.tscn").instance()
	

	var get_req = requests.get_request("http://localhost:8080/checkgamestatus?a=a")
	yield(get_req, "request_completed")
	
	var scores = Globalvars.response.split(",")
	
	match Globalvars.gamestatuscode:
		1:
			$PanelContainer/VBoxContainer/RichTextLabel.text += "\nPlayer 1 Won!"
		2:
			$PanelContainer/VBoxContainer/RichTextLabel.text += "\nPlayer 2 Won!"
		3:
			$PanelContainer/VBoxContainer/RichTextLabel.text += "\nIt's a Draw!"
			
	$PanelContainer/VBoxContainer/RichTextLabel.text += "\nScores:\nPlayer1:" + scores[0] + "\nPlayer2:" + scores[1] + "\n"

