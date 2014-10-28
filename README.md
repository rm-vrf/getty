Getty Web Server
=====
<pre>
   ____      _   _
  / ___| ___| |_| |_ _   _
 | |  _ / _ \ __| __| | | |
 | |_| |  __/ |_| |_| |_| |
  \____|\___|\__|\__|\__, |
                     |___/
  Write Groovy scripts in Jetty!
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