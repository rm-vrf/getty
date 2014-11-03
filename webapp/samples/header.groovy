_response.println('request headers: ' + _request.headers())
_response.println('request accept: ' + _request.header('Accept'))

_response.header('x-request-name', 'header')
