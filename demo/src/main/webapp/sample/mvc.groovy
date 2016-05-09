def person = ['name': 'John', 'age': 18]
$request.attributes['person'] = person
$resp.jsp '/sample/jsp/mvc.jsp'
