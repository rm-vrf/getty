def i = $application.attributes.counter
if (!(i > 0)) {
	i = 0
}
i ++
$application.attributes.counter = i
$response.println('application start time: ' + $application.startTime)
$response.println('<br/>')
$response.println('application visit count: ' + i)
$response.println('<p/>')

def j = $session.attributes.counter
if (!(j > 0)) {
	j = 0
}
j ++
$session.attributes.counter = j
$response.println('session id: ' + $session.id)
$response.println('<br/>')
$response.println('session visit count: ' + j)
$response.println('<p/>')
