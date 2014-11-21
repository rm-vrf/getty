def j = $session.attributes.counter
if (!(j > 0)) {
	j = 0
}
j ++
$session.attributes.counter = j
$response.println('session id: ' + $session.id)
$response.println('<br/>')
$response.println('session visit count: ' + j)
