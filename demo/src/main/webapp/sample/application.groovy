def map = [:]

def i = $application.attributes.counter
if (!(i > 0)) {
	i = 0
}
i ++
$application.attributes.counter = i

map.visitCount = $application.attributes.counter
map.app = $application.application

$view.json map
