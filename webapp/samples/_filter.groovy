def before() {
	$logger.info "filter begin: ${$request.uri}"
}

def after() {
	$logger.info "filter end: ${$request.uri}"
}
