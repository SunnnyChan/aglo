# Jetty On Http
```md
如果前端没有其它 web 服务器，那么 Jetty 应该是基于 HTTP 协议工作。
也就是当 Jetty 接收到一个请求时，必须要按照 HTTP 协议解析请求和封装返回的数据。
```
```md
我们设置 Jetty 的 Connector 实现类为 org.eclipse.jetty.server.bi.SocketConnector 让 Jetty 以 BIO 的方式工作，
Jetty 在启动时将会创建 BIO 的工作环境，它会创建 HttpConnection 类用来解析和封装 HTTP1.1 的协议，
ConnectorEndPoint 类是以 BIO 的处理方式处理连接请求，ServerSocket 是建立 socket 连接接受和传送数据，
Executor 是处理连接的线程池，它负责处理每一个请求队列中任务。acceptorThread 是监听连接请求，一有 socket 连接，它将进入下面的处理流程。
```
```md
当 socket 被真正执行时，HttpConnection 将被调用，
这里定义了如何将请求传递到 servlet 容器里，有如何将请求最终路由到目的 servlet，
关于这个细节可以参考《 servlet 工作原理解析》一文。
```
* Jetty 创建接受连接环境需要三个步骤：
```md
创建一个队列线程池，用于处理每个建立连接产生的任务，这个线程池可以由用户来指定，这个和 Tomcat 是类似的。
创建 ServerSocket，用于准备接受客户端的 socket 请求，以及客户端用来包装这个 socket 的一些辅助类。
创建一个或多个监听线程，用来监听访问端口是否有连接进来。
```

```md
当建立连接的环境已经准备好了，就可以接受 HTTP 请求了，当 Acceptor 接受到 socket 连接后将转入以下流程执行：
Accetptor 线程将会为这个请求创建 ConnectorEndPoint。
HttpConnection 用来表示这个连接是一个 HTTP 协议的连接，它会创建 HttpParse 类解析 HTTP 协议，
并且会创建符合 HTTP 协议的 Request 和 Response 对象。
接下去就是将这个线程交给队列线程池去执行了。
```