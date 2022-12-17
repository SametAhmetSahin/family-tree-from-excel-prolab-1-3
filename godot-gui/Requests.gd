extends Node2D


# Declare member variables here. Examples:
# var a = 2
# var b = "text"

func get_request(url, parameters=""):
	var new_request = HTTPRequest.new()
	add_child(new_request)
	new_request.connect("request_completed", self, "_http_request_completed")
	var error = new_request.request(url + str(parameters))
	if error != OK:
		push_error("An error occured")
	return new_request

func dict_to_get_parameters(dict):
	var result = ""
	for key in dict.keys():
		result += key + "=" + str(dict[key])
		if key != dict.keys()[-1]:
			result += "&"
	return result

func post_request(url="", query=""):
	var new_request = HTTPRequest.new()
	add_child(new_request)
	new_request.connect("request_completed", self, "_http_request_completed")
	new_request.request(url, ["Content-Type: application/json"], false, HTTPClient.METHOD_POST, str(query))
	return new_request
# Called when the node enters the scene tree for the first time.

func _http_request_completed(result, response_code, headers, body):
	Globalvars.setResponse(body.get_string_from_utf8())
