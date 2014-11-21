$request.headers.each{ header ->
	$response.print(header.key)
	$response.print(': ')
	$response.print(header.value)
	$response.println('<br/>')
}

$response.println('<br/>')
$response.println('request accept: ' + $request.headers.Accept + '<p/>')

$response.headers['x-request-name'] = 'header'
