if ($vars.year == null) {
	$resp.println 'Click here: <a href="/doc/2016/27">/doc/2016/27</a>'
} else {
	$resp.println $vars.year
	$resp.println '/'
	$resp.println $vars.id
}
