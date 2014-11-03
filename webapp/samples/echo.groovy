_response.println 'Hello world!'
_response.println '你好世界'

_response.contentType('text/html')
_response.println _response.charset()
_response.println _response.contentType()
