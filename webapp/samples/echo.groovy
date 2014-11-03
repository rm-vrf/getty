_response.println 'Hello world!' + '<br/>'
_response.println '你好世界' + '<br/>'

_response.contentType('text/html')
_response.println _response.charset() + '<br/>'
_response.println _response.contentType() + '<br/>'
