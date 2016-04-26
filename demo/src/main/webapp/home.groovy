import demo.*

$resp.println 'home'

def sample = new Sample()
$resp.println sample.say('Lu')

def dyna = new Dynamic()
$resp.println dyna.fin(100)
