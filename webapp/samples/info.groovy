def map = [:]

map.charset = $request.charset
map.contentLength = $request.contentLength
map.contentType = $request.contentType
map.localAddress = $request.localAddress
map.locale = $request.locale
map.locales = $request.locales
map.localName = $request.localName
map.localPort = $request.localPort
map.method = $request.method
map.protocol = $request.protocol
map.queryString = $request.queryString
map.remoteAddress = $request.remoteAddress
map.remoteHost = $request.remoteHost
map.remoteUser = $request.remoteUser
map.schema = $request.schema
map.serverName = $request.serverName
map.serverPort = $request.serverPort

$view.json(map)