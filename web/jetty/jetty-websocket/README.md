# Jetty Websocket

* org.eclipse.jetty.websocket.servlet

## Develop
* [Jetty Websocket API](API.md)
* [Jetty 9 – Updated WebSocket API](https://webtide.com/jetty-9-updated-websocket-api/)

* First, we need the servlet to provide the glue.
```md
以配置 MyEchoSocket 覆盖 configure（WebSocketServerFactory）。
```
```java
public class MyEchoServlet extends WebSocketServlet
{
    @Override
    public void configure(WebSocketServerFactory factory)
    {
        // register a socket class as default
        factory.register(MyEchoSocket.class);
    }
}
```
```md
WebSocketServlet类的职责是配置WebSocketServerFactory。
最重要的方面是描述 在新套接字请求到达时 如何创建WebSocket实现。这是通过配置适当的WebSocketCreator对象来完成的。
在上面的示例中，默认WebSocketCreator用于注册特定类以在每个新的传入升级请求上实例化。

如果您希望使用自己的WebSocketCreator实现，可以在此配置步骤中提供它。
```
* As for implementing the MyEchoSocket, you have 3 choices.
> * Implementing Listener
> * Using an Adapter
> * Using Annotations


## Resources
* [embedded-jetty-websocket-examples](https://github.com/jetty-project/embedded-jetty-websocket-examples)
* [Webtide - Jetty and CometD Expert Services](https://webtide.com/)
