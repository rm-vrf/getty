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

Getty向Groovy脚本注入了如下变量

- $request
- $response
- $application
- $session
- $cookie
- $model
- $view
- $logger

例如，要从请求中获取所有的变量，输出到页面上，这样写：
<pre>
$request.parameters.each { param ->
    $response.print param.key
	$response.print ': '
	$response.prinln param.value
}
</pre>

支持JSP视图和MVC的开发方式

<pre>
person = ['name': 'John', 'age': 18]
$model.put('person', person)
$view.jsp '/samples/jsp/mvc.jsp'
</pre>

也支持JSON输出

<pre>
def map = ['ok': true]
$view.json(map)
</pre>

详细使用方式请看samples目录。
