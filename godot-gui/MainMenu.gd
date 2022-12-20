extends Control


func _on_rendertree_pressed():
	Globalvars.ip = $PanelContainer/VBoxContainer/ip.text
	get_tree().change_scene("res://TreeView.tscn")
	pass # Replace with function body.
 
