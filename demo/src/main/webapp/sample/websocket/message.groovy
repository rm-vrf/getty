def message = $request.body

$log.info 'get message: ' + message + ', session: ' + $session.id

if (message == 'start') {
	def executor = Executors.newScheduledThreadPool(1)
	executor.scheduleAtFixedRate(()->{
		$resp.print 'message: ' + message
	}, 0, 5, TimeUnit.SECONDS)
} else if (message == 'stop') {
	$session.stop()
}
