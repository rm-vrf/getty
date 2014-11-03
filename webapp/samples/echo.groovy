response.println 'Hello world!'
response.println '你好世界'

response.contentType('text/html')
response.println response.charset()
response.println response.contentType()
