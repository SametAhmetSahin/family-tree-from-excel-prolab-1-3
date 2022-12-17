extends Control


# Declare member variables here. Examples:
# var a = 2
# var b = "text"
onready var CardNode = preload("res://Card.tscn")
		

func _ready():
	pass # Replace with function body.

func cleanDeck():
	for child in get_node("HBoxContainer").get_children():
		child.queue_free()
	Globalvars.selectedCard = null
		
func updateDeck():
	cleanDeck()
	var whichplayer = get_parent().name
	var deck = Globalvars.gamestatus[whichplayer + "deck"]
	#print(deck)
	for item in deck:
		var new_card_node = CardNode.instance()
		
		new_card_node.cardname = str2var(item["object"]["type"])
		new_card_node.durability = int(item["object"]["durability"])
		new_card_node.levelpoint = int(item["object"]["levelPoint"])
		new_card_node.isUsed = item["isUsed"]
		new_card_node.id = int(item["id"])
		get_node("HBoxContainer").add_child(new_card_node)
		new_card_node.updateLabels()
		
	pass

func addCard(card):
	Globalvars.cards.append(card.getCard())
	$HBoxContainer.add_child(card)
	print("Added card " + str(card) + "to deck!")
	print(get_tree().get_root().get_children())
	get_node("/root/DeckSelect").selectedcardcount += 1

func removeCard(card):
	$HBoxContainer.remove_child(card)
	Globalvars.cards.erase(card.getCard())
	card.queue_free()
	
	get_node("/root/DeckSelect").selectedcardcount -= 1
	
	print("Removed card " + str(card) + "from deck!")
	
	
