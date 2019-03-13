# jetty.server.Server

![](../pic/jetty.server.Server.jpg)


* ServerConnector (HTTP connector using NIO ByteChannels and Selectors)
![](../pic/ServerConnector-UML.jpg)


## 
* start
```md
AbstractLifeCycle start() -> doStart(); 预留了doStart()方法供子类重写。
|-ContainerLifeCycle doStart()
  |-AbstractConnector 
    |-AbstractNetworkConnector doStart() ->open(); 设置了一个open方法供子类重写
      |-ServerConnector	open()； 实现了open方法，打开ServerSocketChannel
```
