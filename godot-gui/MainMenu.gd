extends Control



func _on_hcgame_pressed():
	Globalvars.human = true
	get_tree().change_scene("res://DeckSelect.tscn")
	pass # Replace with function body.
 
