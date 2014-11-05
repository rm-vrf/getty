def jvm = ['processor': Runtime.getRuntime().availableProcessors()]
jvm.maxMemory = (Runtime.getRuntime().maxMemory() / 1024 / 1024) + "MB"
jvm.totalMemory = (Runtime.getRuntime().totalMemory() / 1024 / 1024) + "MB"
jvm.freeMemory = (Runtime.getRuntime().freeMemory() / 1024 / 1024) + "MB"

def map = ['ok': true]
map.env = System.getenv()
map.prop = System.getProperties()
map.jvm = jvm

_view.json(map)
