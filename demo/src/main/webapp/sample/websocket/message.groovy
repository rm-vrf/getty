def message = $request.body

$log.info 'get message: ' + message + ', session: ' + $session.id
$resp.println 'hello: ' + message + ", session: " + $session.id
$session.attributes['status'] = message
