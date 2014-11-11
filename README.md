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

支持功能

- Jetty inside，运行Groovy代码
- MVC + JSP View Resolvor
- URL Rewriter
- 内置ORM & 内嵌数据库
- 支持Groovy编写ServletFilter
- 也支持J2EE应用

以上功能都是构思，还没有成为现实。等我写好了push上来，到时候大家下载再用也不晚。

- Q: 那么需要多久呢？
- A: 估工期我不在行，大概需要一个月吧。

11月11日
-----------

大家好我回来了，我一直在工作，我没偷懒。这段时间的成果展示一下：

运行打包命令：
<pre>
mvn package
</pre>

得到Getty运行包：getty-with-dependencies.zip，解压后为start.sh文件赋予执行权限：

<pre>
chmod a+x start.sh
</pre>

好了可以运行了。访问```http://localhos:1025```可以看到Welcome页面。在```webapp```目录里提供了示例，展示了Web应用的开发方法，十分简单。

Getty向Groovy脚本注入了几个变量，大家一看名字就知道这是什么意思：

- _request
- _response
- _session
- _cookie
- _model
- _view
- _logger

例如，要从请求中获取所有的变量，输出到页面上，这样写：
<pre>
_request.parameters().each { param ->
    _response.print(param.key)
	_response.print(': ')
	_response.prinln(param.value)
}
</pre>

这种写法使用了Groovy的闭包方式，非常方便，是不是。

支持JSP视图，MVC的开发方式：

<pre>
person = ['name': 'John', 'age': 18]
_model.put('person', person)
_view.jsp '/samples/jsp/mvc.jsp'
</pre>

也支持JSON输入，这么干：

<pre>
def map = ['ok': true]
_view.json(map)
</pre>

下一步的计划
----------

- 输入参数实现JSON反序列化
- 支持文件上传
- 支持URL Rewrite，实现RESTful服务
- 支持应用启动脚本
- 支持Session Create和Session Destr绑定脚本

