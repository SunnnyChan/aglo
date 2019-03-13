# [Jetty -  Servlet Engine and Http Server](https://www.eclipse.org/jetty/) 
> [github](https://github.com/eclipse/jetty.project)
```md
Eclipse Jetty 提供的 Web服务器 和 javax.servlet 容器，
以及对HTTP / 2，WebSocket，OSGi，JMX，JNDI，JAAS和许多其他集成的支持。

Jetty的口号是“不要在Jetty中部署应用程序，在应用程序中部署Jetty”。
这意味着，将HTTP模块放入您的应用程序，而不是将您的应用程序放入HTTP服务器。
```

## Jetty Architecture
![](pic/jetty-arch.jpg)
```md
Connector 负责接收网络请求，
Handler 负责解析请求并产生响应，
通过线程池 ThreadPool 来执行任务，

而Connector，Handler，ThreadPool 这三个组件都是依附在Server中。
```
![](pic/jetty-components.jpg)
```md
Jetty 的核心是围绕着 Server 类来构建，Server 类继承了 Handler，关联了 Connector 和 Container。
Container 是管理 Mbean 的容器。
Jetty 的 Server 的扩展主要是实现一个个 Handler 并将 Handler 加到 Server 中，Server 中提供了调用这些 Handler 的访问规则。
```
```md
Jetty 中还有一些可有可无的组件，我们可以在它上做扩展。
如 JMX，你可以定义一些 Mbean 把它加到 Server 中，当 Server 启动的时候，这些 Bean 就会一起工作。
```
* Connector
* Handler
* Server

* [ThreadPool](jetty/ThreadPool.md)

* [LifeCycle](jetty/LifeCycle.md)

## Jetty
![](pic/jetty-workflow.png)
```md
核心类：org.mortbay.jetty.Server
核心接口：org.mortbay.component.LifeCycle
核心线程池封装：org.mortbay.thread.QueuedThreadPool
核心IO处理类：org.mortbay.jetty.nio.SelectChannelConnector
核心Servlet处理类：org.mortbay.jetty.servlet.ServletHandler
```

### 接受请求
* [基于 HTTP 协议工作](jetty/WorkMode-HTTP.md)
* [基于 AJP 工作](jetty/WorkMode-AJP.md)
* [基于 NIO 方式工作](jetty/WorkMode-NIO.md)

### 处理请求

## Jetty Code Modules
* [jetty-servlet](jetty-servlet/README.md)
* [jetty-webapp](jetty-webapp/README.md)
* [jetty-websocket](jetty-websocket/README.md)
* [jetty-http](jetty-http/README.md)
* [jetty-server](jetty-server/README.md)
* jetty-client
* jetty-fcgi
* jetty-http2
* jetty-io
* jetty-jmx
* jetty-memcached
* jetty-security
* jetty-annotations
* jetty-jaas

## Integrate
* 与 Jboss 集成
```md
前面介绍了 Jetty 可以基于 AJP 协议工作，在正常的企业级应用中，Jetty 作为一个 Servlet 引擎都是基于 AJP 协议工作的，
所以它前面必然有一个服务器，通常情况下与 Jboss 集成的可能性非常大。
```

## Utility
* [JFinal]

## vs. Tomcat
* [Jetty 的工作原理以及与 Tomcat 的比较](https://www.ibm.com/developerworks/cn/java/j-lo-jetty/index.html)

## Reference
* [The Definitive Reference](https://www.eclipse.org/jetty/documentation/9.4.x/)
* [芋道源码 - Jetty 源码解析](http://www.iocoder.cn/Jetty/Jetty-collection/?vip)