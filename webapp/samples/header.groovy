_request.headers().each{ header ->
	_response.print(header.key)
	_response.print(': ')
	_response.print(header.value)
	_response.println('<br/>')
}

_response.println('<br/>')
_response.println('request accept: ' + _request.header('Accept') + '<p/>')

_response.header('x-request-name', 'header')
