extends PanelContainer

var selected = false
var selectable = true
# Declare member variables here. Examples:
# var a = 2
# var b = "text"

# 0 is rock, 1 is paper, 2 is scissors
export (int, 0, 5) var card = 0;
var card_sprites = ["res://rock.png", "res://paper.png", "res://scissors.png"]
# Called when the node enters the scene tree for the first time.
func _ready():
	
	$VBoxContainer/TextureButton.texture_normal = load(card_sprites[card%3])
	if card > 2:
		$VBoxContainer/TextureButton.modulate = Color((card%3)-0.25, (card%2)-0.25, (card)-0.25)

# Called every frame. 'delta' is the elapsed time since the previous frame.
#func _process(delta):
#	pass

func deleteCard(card_to_delete):
	for i in get_tree().get_nodes_in_group("playerselectiondeck"):
		i.removeCard(card_to_delete)

func _on_TextureButton_pressed():
	deleteCard(get_parent())
