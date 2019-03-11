# [Jetty Websocket API](https://www.eclipse.org/jetty/documentation/9.4.x/websocket-jetty.html)
```md
Jetty提供了自己更强大的WebSocket API，具有用于服务器和客户端使用WebSockets的通用核心API。
它是基于WebSocket消息的事件驱动API。
```
* [Jetty开发指导：Jetty Websocket API](https://blog.csdn.net/tomato__/article/details/38418155)

## [WebSocket Events](https://www.eclipse.org/jetty/documentation/9.4.x/jetty-websocket-api-events.html)
* On Connect Event
```md
向WebSocket指示升级已成功并且WebSocket现已打开。
```
* On Close Event
```md
表示WebSocket现已关闭。
每个关闭事件都有一个状态代码（和一个可选的关闭原因消息）
```
* On Error Event
```md
如果发生错误，在实现过程中，将通过此事件处理程序通知WebSocket。
```
* On Message Event
```md
表示已收到完整消息并已准备好由WebSocket处理。可以是（UTF8）TEXT消息或原始BINARY消息。
```
## WebSocket Session
```md
Session对象可用于：
连接状态（是否打开）
  if(session.isOpen()) {
    // send message
  }
连接安全
  if(session.isSecure()) {
    // connection is using 'wss://'
  }
升级请求和响应中有什么
  UpgradeRequest req = session.getUpgradeRequest();
  String channelName = req.getParameterMap().get("channelName");

  UpgradeResponse resp = session.getUpgradeResponse();
  String subprotocol = resp.getAcceptedSubProtocol();
获取本地或远程地址
  InetSocketAddress remoteAddr = session.getRemoteAddress();
获取并设置空闲超时
  session.setIdleTimeout(2000); // 2 second timeout
```
## Send Messages to Remote Endpoint
* Blocking Send Message
* Send Partial Message
* Send Ping / Pong Control Frame
* Async Send Message

## Using WebSocket Annotations
```md
WebSocket的最基本形式是标记的POJO，其中注释由Jetty WebSocket API提供。
```
* @WebSocket
* @OnWebSocketConnect
* @OnWebSocketClose
* @OnWebSocketMessage
* @OnWebSocketError
* @OnWebSocketFrame

## Using WebSocketListener
```md
WebSocket的基本形式，使用org.eclipse.jetty.websocket.api.WebSocketListener传入事件。
```
## Using the WebSocketAdapter
## [Jetty WebSocket Server API](https://www.eclipse.org/jetty/documentation/9.4.x/jetty-websocket-server-api.html)
* The Jetty WebSocketServlet
* Using the WebSocketCreator

## Jetty WebSocket Client API