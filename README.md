Getty Web Server
=====
```
  ____      _   _
 / ___| ___| |_| |_ _   _
| |  _ / _ \ __| __| | | |
| |_| |  __/ |_| |_| |_| |
 \____|\___|\__|\__|\__, |
                    |___/
Groovy on Jetty!
```

Getty是运行Groovy脚本的Web服务器，开发者可以使用Groovy语言开发Web程序。

> Groovy是一种基于JVM的动态脚本语言，使用Groovy不需要编写过多代码，并且具有闭包和动态语言中的其他特征。
> 关于Groovy的特点和语法请看：[http://groovy-lang.org](http://groovy-lang.org)

使用Getty不需要编译，能够简化部署过程。又能实现与Java的互操作，已经使用Java语言开发出来的库仍然可以继续使用。同时对JVM的优化方式还是一致的。

Getty为Java Web开发人员提供了一套动态脚本环境，适合用来快速开发原型系统，或者代码量小于100万行的小型系统。尤其适合不需要发布又经常变更的场合。

## 下载

- Getty服务：[http://121.199.25.213/getty-1.0.0-SNAPSHOT-GA.tar.gz](http://121.199.25.213/getty-1.0.0-SNAPSHOT-GA.tar.gz)
- Getty服务带演示应用：[http://121.199.25.213/getty-with-demo-1.0.0-SNAPSHOT-GA.tar.gz](http://121.199.25.213/getty-with-demo-1.0.0-SNAPSHOT-GA.tar.gz)

## 安装与启动

```
$ wget http://121.199.25.213/getty-with-demo-1.0.0-SNAPSHOT-GA.tar.gz
$ tar -xvf getty-with-demo-1.0.0-SNAPSHOT-GA.tar.gz
$ cd getty-with-demo-1.0.0-SNAPSHOT
$ chmod a+x bin/*.sh
$ 
```

支持功能

- Jetty inside，运行Groovy代码
- MVC + JSP View Resolvor
- URL rewrit handler
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
