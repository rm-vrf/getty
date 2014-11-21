def map = [:]

def i = $application.attributes.counter
if (!(i > 0)) {
	i = 0
}
i ++
$application.attributes.counter = i

map.visitCount = $application.attributes.counter
map.name = $application.name
map.version = $application.version
map.buildTime = $application.buildTime.toString()
map.startTime = $application.startTime.toString()
map.baseDirectory = $application.baseDirectory
map.configuration = $application.configuration

$view.json map
