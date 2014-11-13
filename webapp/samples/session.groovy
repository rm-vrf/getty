def i = _application.get('counter')
if (!(i > 0)) {
	i = 0
}
i ++
_application.put('counter', i)
_response.println('application counter: ' + i)
_response.println('<br/>')

def j = _session.get('counter')
if (!(j > 0)) {
	j = 0
}
j ++
_session.put('counter', j)
_response.println('session counter: ' + j)
