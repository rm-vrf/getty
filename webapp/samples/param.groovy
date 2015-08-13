$response.println "name: ${name} <br/>"
$response.println "name again: ${$request.parameters.name} <br/>"
$response.println "parameters: ${$request.parameters} <br/>"
$response.println "body: ${$request.body} <br/>"
