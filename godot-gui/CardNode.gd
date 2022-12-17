extends Control


# Declare member variables here. Examples:
# var a = 2
# var b = "text"

export var cardname: String = ""
export var durability = 20
export var levelpoint = 0
export var id = 0
export (bool) var isUsed = false

# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
#func _process(delta):
#	pass
func updateLabels():
	$Card/VBoxContainer/Durability.text = str(durability)
	print(get_node("Card/VBoxContainer").get_children())
	get_node("Card/VBoxContainer/Name").text = cardname
	get_node("Card/VBoxContainer/LevelPoint").text = str(levelpoint)
	get_node("Card").card = get_node("Card").cardTypes[cardname]
	get_node("Card").updateTexture()
	get_node("Card").modulate = Color(1, 1, 1, 1.0-float(int(isUsed))/2.0)
	pass


var cardtypes = {"rock":0, "paper":1, "scissors": 2}

func setCard(cardtype):
	$Card.card = cardtypes[cardtype]
	cardname = cardtype

func getCard():
	return $Card.card
