if ($request.parameters.name == null) {
	$response.println 'Click here: <a href="param.groovy?name=Zoom">param.groovy?name=Zoom</a>'
} else {
	$response.println "name: ${name} <br/>"
	$response.println "name again: ${$request.parameters.name} <br/>"
	$response.println "parameters: ${$request.parameters} <br/>"
	$response.println "body: ${$request.body} <br/>"
}
