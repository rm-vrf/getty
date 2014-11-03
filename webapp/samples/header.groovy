response.println('request headers: ' + request.headers())
response.println('request accept: ' + request.header('Accept'))

response.header('x-request-name', 'header')
