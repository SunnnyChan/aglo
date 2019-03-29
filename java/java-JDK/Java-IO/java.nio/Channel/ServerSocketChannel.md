# ServerSocketChannel
```md
能够监听客户端发起的TCP连接，并为每个TCP连接创建一个新的SocketChannel来进行数据读写。

阻塞或非阻塞

服务器端用于创建TCP连接的通道，只能对accept事件感兴趣。
accept方法会返回一个已和客户端连接好的SocketChannel通道，它才服务器是真正传输数据的通道。
```
