$log.info 'connect from client, session id: ' + $session.id

Thread.start {
	while (true) {
		if ($session.attributes['status'] == 'start') {
			$resp.print "It's my life. " + $session.id
		}
		Thread.sleep(2000L)
	}
}
