Getty内置对象
=====
# 1.HTTP内置对象

## 1.1 $request

也可以写作```$req```

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

###属性

###方法

## 1.3 $application
## 1.4 $session
## 1.5 $cookie
## 1.6 $logger

# 2.Websocket内置对象

## 1.1 $request
## 1.2 $response
## 1.3 $application
## 1.4 $session
## 1.5 $cookie
## 1.6 $logger
