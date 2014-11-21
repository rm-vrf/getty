$cookie.put('userName', 'jacky')

$response.println('userName: ' + $cookie.userName?.value)
$response.println('<p/>')

$cookie.each {name, cookie->
	$response.println('name: ' + name + ', value: ' + cookie.value)
	$response.println('<br/>')
}
