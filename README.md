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

发布地址

https://github.com/lane-cn/getty/releases

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
    $response.print "${param.key}: ${param.value}"
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

使用方式请看samples目录中的例子，这里是对象的属性和方法说明：

http://www.batchfile.cn/?p=88

Getty支持以下几个特殊文件，实现特殊功能

- _rewrite.json
- _application.groovy
- _session.groovy
- _filter.groovy

使用方式请看samples目录中的例子，这里是特使文件的功能和使用说明：

http://www.batchfile.cn/?p=93
