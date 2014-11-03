response.println 'name: ' + name
response.println 'name again: ' + request.parameter('name')
response.println 'parameters: ' + request.parameters()
response.println 'body: ' + request.body()
