extends Camera2D


# Declare member variables here. Examples:
# var a = 2
# var b = "text"
var mouse_start_pos
var screen_start_position

var dragging = false

# Called when the node enters the scene tree for the first time.
func _input(event):
	if event.is_action("zoom_in"):
		zoom /= 1.1
		zoom = Vector2(clamp(zoom.x, 0.1, 10), clamp(zoom.y, 0.1, 10))
		#position = event.position * zoom
	if event.is_action("zoom_out"):
		zoom *= 1.1
		zoom = Vector2(clamp(zoom.x, 0.1, 10), clamp(zoom.y, 0.1, 10))
		#position = event.position * zoom + screen_start_position
	if event.is_action("drag"):
		if event.is_pressed():
			mouse_start_pos = event.position
			screen_start_position = position
			dragging = true
		else:
			dragging = false
	elif event is InputEventMouseMotion and dragging:
		position = zoom * (mouse_start_pos - event.position) + screen_start_position
	
