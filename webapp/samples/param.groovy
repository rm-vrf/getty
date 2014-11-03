_response.println 'name: ' + name
_response.println 'name again: ' + _request.parameter('name')
_response.println 'parameters: ' + _request.parameters()
_response.println 'body: ' + _request.body()
