_response.println 'name: ' + name + '<br/>'
_response.println 'name again: ' + _request.parameter('name') + '<br/>'
_response.println 'parameters: ' + _request.parameters() + '<br/>'
_response.println 'body: ' + _request.body() + '<br/>'
