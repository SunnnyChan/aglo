# Web

* Undertow
```md
是一个用java编写的灵活的高性能Web服务器，提供基于NIO的阻塞和非阻塞API
Undertow 是红帽公司（RedHat）的开源产品，是 WildFly8（JBoos） 默认的 Web 服务器。
Undertow框架jar包： undertow-core.jar undertow-servlet.jar
```

```md
* 特点
	Lightweight（轻量级）
		Undertow核心jar包在1Mb以下
		它在运行时也是轻量级的，有一个简单的嵌入式服务器使用少于4Mb的堆空间
	HTTP Upgrade Support（支持http升级）
		支持HTTP升级，允许多个协议通过HTTP端口进行多路复用
	Web Socket Support（支持WebScoket）
		  Undertow提供对Web Socket的全面支持，包括JSR-356支持
	Servlet 3.1  
		Undertow提供对Servlet 3.1的支持，包括对嵌入式servlet的支持
		还可以在同一部署中混合Servlet和本机Undertow非阻塞处理程序
	Embeddable（可嵌入的）
		Undertow可以嵌入在应用程序中或独立运行，只需几行代码
	Flexible（灵活性）
```

* [CometD](https://github.com/cometd/cometd) 
```md
CometD 框架是基于 HTTP 的事件驱动通信解决方案。第 2 版本中添加了对注释配置和 WebSocket 的支持。
CometD 的事件驱动方法非常适合事件驱动 Web 开发的新概念。

基于 Bayeux 标准化通信协议，Bayeux 通信协议主要是基于 HTTP。
它提供了客户端与服务器之间的响应性双向异步通信。
Bayeux 是一种 “发布- 订阅” 协议。
```
[cometd.org](https://cometd.org/)

* [JFinal](https://gitee.com/jfinal/jfinal)
```md
基于 Jetty 的极速 WEB + ORM 框架。
```

* [http-request](https://github.com/kevinsawicki/http-request)

* [redant - 一个基于Netty的轻量级Web容器](https://github.com/all4you/redant)
> * [Netty 实战：如何编写一个麻小俱全的 web 容器](https://www.jianshu.com/p/dbbb75ee01e8)