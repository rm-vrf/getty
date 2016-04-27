import java.util.concurrent.*

$log.info 'connect from client, session id: ' + $session.id

def executor = Executors.newScheduledThreadPool(1)
executor.scheduleAtFixedRate(()->{
	$resp.print 'alive'
}, 0, 5, TimeUnit.SECONDS)
