extends PanelContainer

var selected = false
var selectable = true
# Declare member variables here. Examples:
# var a = 2
# var b = "text"

# 0 is rock, 1 is paper, 2 is scissors
export (int, 0, 5) var card = 0;
var cardTypes = {"Rock":0, "Paper":1, "Scissor":2, "HeavyRock":3, "SpecialPaper":4, "MasterScissor":5} 
var card_sprites = ["res://rock.png", "res://paper.png", "res://scissors.png"]
# Called when the node enters the scene tree for the first time.
func _ready():
	updateTexture()

func updateTexture():
	$VBoxContainer/TextureButton.texture_normal = load(card_sprites[card%3])
	if card > 2:
		$VBoxContainer/TextureButton.modulate = Color((card%3)-0.25, (card%2)-0.25, (card)-0.25)

func select():
	selected = true
	Globalvars.selectedCard = get_parent()
	
func _physics_process(delta):
	if Globalvars.selectedCard == get_parent():
		rect_position.y = -50
	else:
		rect_position.y = 0

func unselect():
	selected = false
	Globalvars.selectedCard = null

func _on_TextureButton_pressed():
	if selectable:
		if selected:
			unselect()
		else:
			select()
			
