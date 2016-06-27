Getty内置对象
=====
# 1.HTTP对象

## 1.1 $request
也可以写成```$req```

###属性
|属性|类型|读/写|说明
|----|----|----|----
|attributes|Map|只读|Returns an Map containing the name-values of the attributes available to this request.
|body|Object|只读|Retrieves the body of the request as Object
|charset|String|只读|Returns the name of the character encoding used in the body of this request. This method returns null if the request does not specify a character encoding
|contentLength|int|只读|Returns the length, in bytes, of the request body and made available by the input stream, or -1 if the length is not known ir is greater than Integer.MAX_VALUE.
|contentType|String|只读|Returns the MIME type of the body of the request, or null if the type is not known.
|headers|Map|只读|Returns all the values of the specified request header as an Map of String objects.
|localAddress|String|只读|Returns the Internet Protocol (IP) address of the interface on which the request was received.
|locale|String|只读|Returns the preferred Locale that the client will accept content in, based on the Accept-Language header. If the client request doesn’t provide an Accept-Language header, this method returns the default locale for the server.
|locales|String[]|只读|Returns an Enumeration of Locale objects indicating, in decreasing order starting with the preferred locale, the locales that are acceptable to the client based on the Accept-Language header. If the client request doesn’t provide an Accept-Language header, this method returns an Enumeration containing one Locale, the default locale for the server.
|localName|String|只读|Returns the host name of the Internet Protocol (IP) interface on which the request was received.
|localPort|int|只读|Returns the Internet Protocol (IP) port number of the interface on which the request was received.
|method|String|只读|Returns the name of the HTTP method with which this request was made, for example, GET, POST, or PUT.
|parameters|Map|只读|Returns a Map of the parameters of this request.
|protocol|String|只读|Returns the name and version of the protocol the request uses in the form protocol/majorVersion.minorVersion, for example, HTTP/1.1. For HTTP servlets, the value returned is the same as the value of the CGI variable SERVER_PROTOCOL.
|queryString|String|只读|Returns the query string that is contained in the request URL after the path.
|remoteAddress|String|只读|Returns the Internet Protocol (IP) address of the client or last proxy that sent the request. For HTTP servlets, same as the value of the CGI variable REMOTE_ADDR.
|remoteHost|String|只读|Returns the fully qualified name of the client or the last proxy that sent the request. If the engine cannot or chooses not to resolve the hostname (to improve performance), this method returns the dotted-string form of the IP address. For HTTP servlets, same as the value of the CGI variable REMOTE_HOST.
|remoteUser|String|只读|Returns the login of the user making this request, if the user has been authenticated, or null if the user has not been authenticated.
|schema|String|只读|Returns the name of the scheme used to make this request, for example, http, https, or ftp. Different schemes have different rules for constructing URLs, as noted in RFC 1738.
|serverName|String|只读|Returns the host name of the server to which the request was sent. It is the value of the part before “:” in the Host header value, if any, or the resolved server name, or the server IP address.
|serverPort|int|只读|Returns the port number to which the request was sent. It is the value of the part after “:” in the Host header value, if any, or the server port where the client connection was accepted on.
|uri|String|只读|Returns the part of this request’s URL from the protocol name up to the query string in the first line of the HTTP request.

## 1.2 $response
也可以写成```$res```、```$resp```

###属性
|属性|类型|读/写|说明
|----|----|----|----
|charset|String|读写|Returns or sets the character encoding (MIME charset) of the response being sent to the client, for example, UTF-8.
|contentType|String|读写|Returns or sets the content type used for the MIME body sent in this response.
|headers|Map|只读|Returns or sets a response header with the given name and date-value.
|locale|String|读写|Returns or sets the locale of the response.
|status|int|读写|Gets or sets the current status code of this response.

###方法
|方法|参数|返回值|说明
|----|----|----|----
|error|int code|Response|Sends an error response to the client using the specified status code and clears the buffer.
|error|int code, String message|Response|Sends an error response to the client using the specified status and clears the buffer.
|json|Object object|Response|输出Json序列化对象.
|jsp|String view|Response|输出JSP视图.
|print|Object object|Response|Prints a object.
|println|Object object|Response|Prints a object and then terminates the line.
|redirect|String location|Response|Sends a temporary redirect response to the client using the specified redirect location URL and clears the buffer.
|write|InputStream stream|Response|Writes an input stream.
|write|byte[] bytes|Response|Writes an array of byte.
|write|Reader reader|Response|Writes a reader

## 1.3 $application
也可以写成```$app```

###属性
|属性|类型|读/写|说明
|----|----|----|----
|application|Application|只读|返回应用设置
|attributes|Map|只读|Stores an attribute in this application. Attributes are keped across requests and sessions.
|port|int|只读|Returns the port of the instancce.
|startTime|Date|只读|Returns the start time of the instance.

## 1.4 $session
###属性
|属性|类型|读/写|说明
|----|----|----|----
|attributes|Map|只读|Returns the object bound with the specified name in this session, or null if no object is bound under the name.
|creationTime|long|只读|Returns the time when this session was created, measured in milliseconds since midnight January 1, 1970 GMT.
|id|String|只读|Returns a string containing the unique identifier assigned to this session.
|lastAccessedTime|long|只读|Returns the last time the client sent a request associated with this session, as the number of milliseconds since midnight January 1, 1970 GMT, and marked by the time the container received the request.
|maxInactiveInterval|int|只读|Returns or sets the time, in seconds, between client requests before the servlet container will invalidate this session.
|servletContext|ServletContext|只读|Returns the ServletContext to which this session belongs.

## 1.5 $cookie
$cookie对象是Map接口的实例，Key和Value分别为String类型、javax.servlet.http.Cookie类型。$cookie对象具有Map接口的所有方法，例如：size()、isEmpty()。

###方法
|方法|参数|返回值|说明
|----|----|----|----
|put|Cookie cookie|Cookie|Adds the specified cookie to the response.
|put|String name, String value|Cookie|Adds the specified cookie to the response.
|put|String name, String value, int maxAge|Cookie|Adds the specified cookie to the response.
|put|String name, String value, String domain, String path, int maxAge, boolean secure|Cookie|Adds the specified cookie to the response.

## 1.6 $logger
也可以写成```$log```

###属性
|属性|类型|读/写|说明
|----|----|----|----
|debugEnabled|boolean|只读|Check whether this category is enabled for the DEBUG Level.
|infoEnabled|boolean|只读|Check whether this category is enabled for the INFO Level.
|traceEnabled|boolean|只读|Check whether this category is enabled for the TRACE Level.

###方法
|方法|参数|返回值|说明
|----|----|----|----
|debug|Object message|void|Log a message object with the DEBUG level.
|debug|Object message, Throwable t|void|Log a message object with the DEBUG level including the stack trace of the Throwable passed as parameter.
|error|Object message|void|Log a message object with the ERROR level.
|error|Object message, Throwable t|void|Log a message object with the ERROR level including the stack trace of the Throwable passed as parameter.
|fatal|Object message|void|Log a message object with the FATAL level.
|fatal|Object message, Throwable t|void|Log a message object with the FATAL level including the stack trace of the Throwable passed as parameter.
|info|Object message|void|Log a message object with the INFO level.
|info|Object message, Throwable t|void|Log a message object with the INFO level including the stack trace of the Throwable passed as parameter.
|trace|Object message|void|Log a message object with the TRACE level.
|trace|Object message, Throwable t|void|Log a message object with the TRACE level including the stack trace of the Throwable passed as parameter.

# 2.Websocket对象
## 1.1 $request
也可以写成```$req```

###属性
|属性|类型|读/写|说明
|----|----|----|----
|body|Object|只读|Retrieves the body of the request as Object
|charset|String|只读|Returns the name of the character encoding used in the body of this request. This method returns null if the request does not specify a character encoding
|contentLength|int|只读|Returns the length, in bytes, of the request body and made available by the input stream, or -1 if the length is not known ir is greater than Integer.MAX_VALUE.
|contentType|String|只读|Returns the MIME type of the body of the request, or null if the type is not known.
|headers|Map|只读|Returns all the values of the specified request header as an Map of String objects.
|localAddress|String|只读|Returns the Internet Protocol (IP) address of the interface on which the request was received.
|localName|String|只读|Returns the host name of the Internet Protocol (IP) interface on which the request was received.
|localPort|int|只读|Returns the Internet Protocol (IP) port number of the interface on which the request was received.
|method|String|只读|Returns the name of the HTTP method with which this request was made, for example, GET, POST, or PUT.
|parameters|Map|只读|Returns a Map of the parameters of this request.
|protocol|String|只读|Returns the name and version of the protocol the request uses in the form protocol/majorVersion.minorVersion, for example, HTTP/1.1. For HTTP servlets, the value returned is the same as the value of the CGI variable SERVER_PROTOCOL.
|queryString|String|只读|Returns the query string that is contained in the request URL after the path.
|remoteAddress|String|只读|Returns the Internet Protocol (IP) address of the client or last proxy that sent the request. For HTTP servlets, same as the value of the CGI variable REMOTE_ADDR.
|remoteHost|String|只读|Returns the fully qualified name of the client or the last proxy that sent the request. If the engine cannot or chooses not to resolve the hostname (to improve performance), this method returns the dotted-string form of the IP address. For HTTP servlets, same as the value of the CGI variable REMOTE_HOST.
|remoteUser|String|只读|Returns the login of the user making this request, if the user has been authenticated, or null if the user has not been authenticated.
|schema|String|只读|Returns the name of the scheme used to make this request, for example, http, https, or ftp. Different schemes have different rules for constructing URLs, as noted in RFC 1738.
|serverName|String|只读|Returns the host name of the server to which the request was sent. It is the value of the part before “:” in the Host header value, if any, or the resolved server name, or the server IP address.
|serverPort|int|只读|Returns the port number to which the request was sent. It is the value of the part after “:” in the Host header value, if any, or the server port where the client connection was accepted on.
|uri|String|只读|Returns the part of this request’s URL from the protocol name up to the query string in the first line of the HTTP request.

## 1.2 $response
也可以写成```$res```、```$resp```

###属性
|属性|类型|读/写|说明
|----|----|----|----
|charset|String|读写|Returns or sets the character encoding (MIME charset) of the response being sent to the client, for example, UTF-8.
|contentType|String|读写|Returns or sets the content type used for the MIME body sent in this response.
|headers|Map|只读|Returns or sets a response header with the given name and date-value.
|status|int|读写|Gets or sets the current status code of this response.

###方法
|方法|参数|返回值|说明
|----|----|----|----
|error|int code|Response|Sends an error response to the client using the specified status code and clears the buffer.
|error|int code, String message|Response|Sends an error response to the client using the specified status and clears the buffer.
|print|String s|Response|Prints string.
|println|String s|Response|Prints string and then terminates the line.
|write|InputStream stream|Response|Writes an input stream.
|write|byte[] bytes|Response|Writes an array of byte.
|write|Reader reader|Response|Writes a reader

## 1.3 $application
也可以写成```$app```

###属性
|属性|类型|读/写|说明
|----|----|----|----
|application|Application|只读|返回应用设置
|attributes|Map|只读|Stores an attribute in this application. Attributes are keped across requests and sessions.
|port|int|只读|Returns the port of the instancce.
|startTime|Date|只读|Returns the start time of the instance.

## 1.4 $session
###属性

|属性|类型|读/写|说明
|----|----|----|----
|attributes|Map|只读|Returns the object bound with the specified name in this session, or null if no object is bound under the name.
|creationTime|long|只读|Returns the time when this session was created, measured in milliseconds since midnight January 1, 1970 GMT.
|id|String|只读|Returns a string containing the unique identifier assigned to this session.
|idleTimeout|long|读写|Return the number of milliseconds before this conversation will be closed by the container if it is inactive, ie no messages are either sent or received in that time.
|lastAccessedTime|long|读写|Returns the last time the client sent a request associated with this session, as the number of milliseconds since midnight January 1, 1970 GMT, and marked by the time the container received the request.
|statusCode|int|只读|
|statusReason|String|只读|
|open|boolean|只读|Return true if and only if the underlying socket is open.
|secure|boolean|只读|Return true if and only if the underlying socket is using a secure transport.

###方法

|方法|参数|返回值|说明
|----|----|----|----
|close||void|Request a close of the current conversation with a normal status code and no reason phrase.
|close|int statusCode, String statusReason|void|Send a websocket Close frame, with status code.
|resume||void|Resume a previously suspended connection.
|suspend||void|Suspend a the incoming read events on the connection.

## 1.5 $cookie
$cookie对象是Map接口的实例，Key和Value分别为String类型、java.net.Cookie类型。$cookie对象具有Map接口的所有方法，例如：size()、isEmpty()。

###方法
|方法|参数|返回值|说明
|----|----|----|----
|put|Cookie cookie|Cookie|Adds the specified cookie to the response.
|put|String name, String value|Cookie|Adds the specified cookie to the response.
|put|String name, String value, int maxAge|Cookie|Adds the specified cookie to the response.
|put|String name, String value, String domain, String path, int maxAge, boolean secure|Cookie|Adds the specified cookie to the response.

## 1.6 $logger
也可以写成```$log```

###属性
|属性|类型|读/写|说明
|----|----|----|----
|debugEnabled|boolean|只读|Check whether this category is enabled for the DEBUG Level.
|infoEnabled|boolean|只读|Check whether this category is enabled for the INFO Level.
|traceEnabled|boolean|只读|Check whether this category is enabled for the TRACE Level.

###方法
|方法|参数|返回值|说明
|----|----|----|----
|debug|Object message|void|Log a message object with the DEBUG level.
|debug|Object message, Throwable t|void|Log a message object with the DEBUG level including the stack trace of the Throwable passed as parameter.
|error|Object message|void|Log a message object with the ERROR level.
|error|Object message, Throwable t|void|Log a message object with the ERROR level including the stack trace of the Throwable passed as parameter.
|fatal|Object message|void|Log a message object with the FATAL level.
|fatal|Object message, Throwable t|void|Log a message object with the FATAL level including the stack trace of the Throwable passed as parameter.
|info|Object message|void|Log a message object with the INFO level.
|info|Object message, Throwable t|void|Log a message object with the INFO level including the stack trace of the Throwable passed as parameter.
|trace|Object message|void|Log a message object with the TRACE level.
|trace|Object message, Throwable t|void|Log a message object with the TRACE level including the stack trace of the Throwable passed as parameter.
