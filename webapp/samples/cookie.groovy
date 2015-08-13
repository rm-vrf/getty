$cookie.put('userName', 'jacky')

$response.println("userName: ${$cookie.userName?.value} <p/>")

$cookie.each {name, cookie->
	$response.println("name: ${name}, value: ${cookie.value} <br/>")
}
