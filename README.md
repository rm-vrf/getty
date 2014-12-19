Getty Web Server
=====
<pre>
  ____      _   _
 / ___| ___| |_| |_ _   _
| |  _ / _ \ __| __| | | |
| |_| |  __/ |_| |_| |_| |
 \____|\___|\__|\__|\__, |
                    |___/
Groovy on Jetty!
</pre>

Getty是一个运行Groovy脚本的Web服务器。
程序员可以采用Java编写业务代码，编译成jar包，使用Groovy联调，在Getty中快速部署。部署和修改不需要重新编译和启动。适合用来快速开发原型系统，或者代码量小于100万行的小型系统。

下载地址

http://www.batchfile.cn/release/getty-1.0.2.tar.gz

支持功能

- Jetty inside，运行Groovy代码
- MVC + JSP View Resolvor
- URL Rewriter
- 支持ServletFilter、SessionListener

Getty向Groovy脚本注入以下变量

- $request
- $response
- $application
- $session
- $cookie
- $model
- $view
- $logger

可以使用这些变量实现HTTP输入和输出的处理。例如，要从请求中获取所有的变量，输出到页面上，这样写：
```
$request.parameters.each { param ->
    $response.print param.key
    $response.print ': '
    $response.prinln param.value
}
```

支持JSP视图和MVC的开发方式

```
person = ['name': 'John', 'age': 18]
$model.put('person', person)
$view.jsp '/samples/jsp/mvc.jsp'
```

也支持JSON输出

```
def map = ['ok': true]
$view.json(map)
```

使用方式请看samples目录中的例子，以下是对象的属性和方法说明：

$request对象
-----

属性

|| *属性*           || *类型*    || *读/写* || *说明* ||
|| body            || Object   || 只读    || Retrieves the body of the request as Object      ||
|| charset         || String   || 只读    || Returns the name of the character encoding used in the body of this request. This method returns null if the request does not specify a character encoding      ||
|| contentLength   || int      || 只读    || Returns the length, in bytes, of the request body and made available by the input stream, or -1 if the length is not known ir is greater than Integer.MAX_VALUE.      ||
|| contentType     || String   || 只读    || Returns the MIME type of the body of the request, or null if the type is not known.      ||
|| headers         || Map      || 只读    || Returns all the values of the specified request header as an Map of String objects.      ||
|| localAddress    || String   || 只读    || Returns the Internet Protocol (IP) address of the interface on which the request was received.      ||
|| locale          || String   || 只读    || Returns the preferred Locale that the client will accept content in, based on the Accept-Language header. If the client request doesn't provide an Accept-Language header, this method returns the default locale for the server.      ||
|| locales         || String[] || 只读    || Returns an Enumeration of Locale objects indicating, in decreasing order starting with the preferred locale, the locales that are acceptable to the client based on the Accept-Language header. If the client request doesn't provide an Accept-Language header, this method returns an Enumeration containing one Locale, the default locale for the server.      ||
|| localName       || String   || 只读    || Returns the host name of the Internet Protocol (IP) interface on which the request was received.      ||
|| localPort       || int      || 只读    || Returns the Internet Protocol (IP) port number of the interface on which the request was received.      ||
|| method          || String   || 只读    || Returns the name of the HTTP method with which this request was made, for example, GET, POST, or PUT.      ||
|| parameters      || Map      || 只读    || Returns a Map of the parameters of this request.      ||
|| protocol        || String   || 只读    || Returns the name and version of the protocol the request uses in the form protocol/majorVersion.minorVersion, for example, HTTP/1.1. For HTTP servlets, the value returned is the same as the value of the CGI variable SERVER_PROTOCOL.      ||
|| queryString     || String   || 只读    || Returns the query string that is contained in the request URL after the path.      ||
|| remoteAddress   || String   || 只读    || Returns the Internet Protocol (IP) address of the client or last proxy that sent the request. For HTTP servlets, same as the value of the CGI variable REMOTE_ADDR.      ||
|| remoteHost      || String   || 只读    || Returns the fully qualified name of the client or the last proxy that sent the request. If the engine cannot or chooses not to resolve the hostname (to improve performance), this method returns the dotted-string form of the IP address. For HTTP servlets, same as the value of the CGI variable REMOTE_HOST.      ||
|| remoteUser      || String   || 只读    || Returns the login of the user making this request, if the user has been authenticated, or null if the user has not been authenticated.      ||
|| schema          || String   || 只读    || Returns the name of the scheme used to make this request, for example, http, https, or ftp. Different schemes have different rules for constructing URLs, as noted in RFC 1738.      ||
|| serverName      || String   || 只读    || Returns the host name of the server to which the request was sent. It is the value of the part before ":" in the Host header value, if any, or the resolved server name, or the server IP address.      ||
|| serverPort      || int      || 只读    || Returns the port number to which the request was sent. It is the value of the part after ":" in the Host header value, if any, or the server port where the client connection was accepted on.      ||
|| uri             || String   || 只读    || Returns the part of this request's URL from the protocol name up to the query string in the first line of the HTTP request.      ||

$response对象
-----

$application对象
-----

$session对象
-----

$cookie对象
-----


$model对象
-----

$view对象
-----

$logger对象
-----

Getty支持以下几个特殊文件，实现特殊功能

- _rewrite.json
- _application.groovy
- _session.groovy
- _filter.groovy

使用方式请看samples目录中的例子，以下是特使文件的功能和使用说明：

