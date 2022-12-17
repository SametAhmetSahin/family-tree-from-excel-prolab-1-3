extends Control


# Declare member variables here. Examples:
# var a = 2
# var b = "text"

var selectedcardcount = 0
var maxcardcount = 5

# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
#func _process(delta):
#	pass
onready var CardNode = preload("res://SelectionCard.tscn")

func _on_RockCard_pressed():
	if selectedcardcount < maxcardcount:
		var card = CardNode.instance()
		card.setCard("rock")
		$PanelContainer/VBoxContainer/Deck.addCard(card)



func _on_PaperButton_pressed():
	if selectedcardcount < maxcardcount:
		var card = CardNode.instance()
		card.setCard("paper")
		$PanelContainer/VBoxContainer/Deck.addCard(card)


func _on_ScissorsButton_pressed():
	if selectedcardcount < maxcardcount:
		var card = CardNode.instance()
		card.setCard("scissors")
		$PanelContainer/VBoxContainer/Deck.addCard(card)


func _on_Button_pressed():
	get_tree().change_scene("res://Battle.tscn")
