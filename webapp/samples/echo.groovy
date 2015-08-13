$response.println 'Hello world! <br/>'
$response.println '你好世界 <br/>'

$response.contentType = 'text/html'
$response.println "${$response.charset} <br/>"
$response.println "${$response.contentType} <br/>"
