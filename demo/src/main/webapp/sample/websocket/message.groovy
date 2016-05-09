def message = $request.body

$log.info 'get message: ' + message + ', session: ' + $session.id
$resp.println 'input: ' + message
