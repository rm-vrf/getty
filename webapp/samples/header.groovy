$request.headers.each{ header ->
	$response.println "${header.key}: ${header.value} <br/>"
}

$response.println '<br/>'
$response.println "request accept: ${$request.headers.Accept} <p/>"

$response.headers['x-request-name'] = 'header'
