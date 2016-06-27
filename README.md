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

Getty是运行Groovy脚本的Web服务器。在Getty上，开发者可以使用Groovy语言开发Web程序。

> Groovy是一种基于JVM的动态脚本语言，使用Groovy不需要编写过多代码，并且具有闭包和动态语言中的其他特征。
> 关于Groovy的特点和语法请看：[http://groovy-lang.org](http://groovy-lang.org)

使用Getty不需要编译，能够简化部署过程。又能实现与Java互操作，已经使用Java语言开发出来的库仍然可以继续使用。同时对JVM的优化方式是一致的。

Getty为Java Web开发人员提供了一套动态脚本环境，适合用来快速开发原型系统，或者代码量小于100万行的小型系统。尤其适合不需要发布又经常变更的场合。

主要功能：

- 内嵌Jetty，运行Groovy脚本
- MVC + JSP视图解析
- URL模式定义，实现RESTful服务
- 支持ServletFilter、SessionListener
- 支持Websocket协议，可以编写Websocket服务
- 支持定时任务

# 1.下载

- Getty服务：[http://121.199.25.213/getty/getty-latest.tar.gz](http://121.199.25.213/getty/getty-latest.tar.gz)
- Getty服务带演示应用：[http://121.199.25.213/getty/getty-with-demo-latest.tar.gz](http://121.199.25.213/getty/getty-with-demo-latest.tar.gz)

# 2.安装

下载安装包，解压后为bin目录中的脚本赋予执行权限：

```
$ wget http://121.199.25.213/getty-with-demo-1.0.0-GA.tar.gz
$ tar -xvf getty-with-demo-1.0.0-GA.tar.gz
$ cd getty-with-demo-1.0.0
$ chmod a+x bin/*.sh
```

# 3.启动

运行启动脚本：

```
$ bin/start.sh
```

在logs/getty.log可以看到日志输出：

```
INFO [(getty.server.Main)]   ____      _   _           
INFO [(getty.server.Main)]  / ___| ___| |_| |_ _   _   
INFO [(getty.server.Main)] | |  _ / _ \ __| __| | | |  
INFO [(getty.server.Main)] | |_| |  __/ |_| |_| |_| |  
INFO [(getty.server.Main)]  \____|\___|\__|\__|\__, |  
INFO [(getty.server.Main)]                     |___/   
INFO [(getty.server.Main)] Groovy on Jetty!            
INFO [(getty.server.Main)] application root: ./src/main/webapp
INFO [(getty.manager.ApplicationManager)] load application from ./src/main/webapp
INFO [(getty.manager.ApplicationManager)] load application from directory: ./src/main/webapp, name: demo
INFO [(getty.manager.ApplicationInstanceManager)] start demo at port: 1025
INFO [(app_start.groovy)] start demo, port: 1025
INFO [(getty.manager.CrontabManager)] start crontab
```

Getty已经启动，在1025端口启动了示例应用。可以在浏览器中访问：[http://localhost:1025/sample](http://localhost:1025/sample)

# 4.应用

## 4.1 描述文件

### 4.1.1 app.yaml

Getty使用应用描述文件定义一个应用，应用描述文件是yaml格式，位置在应用的根目录。app.yaml是应用的基本描述文件，这个文件是必须有的，格式如下：

```
application: demo
version: alpha-001
port: 1025

handlers:
  - url: /doc/(year)/(id)
    script: sample/doc.groovy
    http_headers:
      X-Foo-Header: foo
      X-Bar-Header: bar value

  - url: /sample/param
    script: sample/param.groovy

filters:
  - url_pattern: /admin/*
    script: filter.groovy

listeners:
  - start: app_start.groovy
    stop: app_stop.groovy

error_handlers:
  - file: default_error.html
  
  - error_code: 404
    file: over_quota.html

index_pages: 
  - index.html
  - index.htm
  - index.groovy

charset_encoding: utf-8
file_encoding: utf-8
```

### 4.1.2 session.yaml

session.yaml描述应用的会话属性，可以定义会话监听器，格式如下：

```
timeout: 20

listeners:
  - created: session_created.groovy
    destroyed: session_destroyed.groovy
```

### 4.1.3 websocket.yaml

websocket.yaml描述Websocket服务，格式如下：

```
url_pattern: *.ws

handlers:
  - url: /test.ws
    script:
      connect: sample/websocket/connect.groovy
      close: sample/websocket/close.groovy
      message: sample/websocket/message.groovy
      error: sample/websocket/error.groovy
```

### 4.1.4 cron.yaml

cron.yaml描述定时任务，格式如下：

```
concurrent: false

cron:
  - description: test target
    script: tasks/hello_task.groovy
    schedule: 0 0/1 * * * ?
```

## 4.2 脚本变量

Getty向Groovy脚本注入以下变量

- $request
- $response
- $application
- $session
- $cookie
- $logger

可以使用这些变量实现输入和输出。例如，要从请求中获取所有的变量输出到页面上，这样写：

```
$request.parameters.each { param ->
    $response.print "${param.key}: ${param.value}"
}
```

使用了Groovy语言的闭包方式，是不是很简单。支持JSP视图和MVC的开发方式：

```
def person = ['name': 'John', 'age': 18]
$request.attributes['person'] = person
$resp.jsp '/sample/jsp/mvc.jsp'
```

也支持JSON输出：

```
def map = ['ok': true]
$resp.json(map)
```

使用方式请看sample中的例子，这里是对象的属性和方法说明：[API.md](https://github.com/lane-cn/getty/blob/master/API.md)
