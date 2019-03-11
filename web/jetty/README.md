# [Jetty -  Servlet Engine and Http Server](https://www.eclipse.org/jetty/) 
> [github](https://github.com/eclipse/jetty.project)
```md
Eclipse Jetty 提供的 Web服务器 和 javax.servlet 容器，
以及对HTTP / 2，WebSocket，OSGi，JMX，JNDI，JAAS和许多其他集成的支持。

Jetty的口号是“不要在Jetty中部署应用程序，在应用程序中部署Jetty”。
这意味着，将HTTP模块放入您的应用程序，而不是将您的应用程序放入HTTP服务器。
```

* LifeCycle

## Jetty Architecture
![](pic/jetty-arch.jpg)
```md
Connector 负责接收网络请求，Handler 负责解析请求并产生响应，通过线程池 ThreadPool 来执行任务，
而Connector，Handler，ThreadPool 这三个组件都是依附在Server中。
```
* Connector
* Handler
* Server

## Jetty Code Modules
* [jetty-servlet]()
* [jetty-webapp]()
* [jetty-websocket](jetty-websocket/README.md)
* jetty-http
* jetty-server
* jetty-client
* jetty-fcgi
* jetty-http2
* jetty-io
* jetty-jmx
* jetty-memcached
* jetty-security
* jetty-annotations
* jetty-jaas


## Reference
* [The Definitive Reference](https://www.eclipse.org/jetty/documentation/9.4.x/)
* [芋道源码 - Jetty 源码解析](http://www.iocoder.cn/Jetty/Jetty-collection/?vip)