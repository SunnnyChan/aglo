# Channel
```md
Java NIO的通道类似Java IO中的流。
```

* FileChannel：
```md
文件
阻塞
```
* DatagramChannel
```md
UDP协议
阻塞或非阻塞
```
* SocketChannel
```md
TCP协议
阻塞或非阻塞
```
* ServerSocketChannel
```md
用于TCP服务器端的监听和链接
对每一个新进来的连接都会创建一个SocketChannel。

阻塞或非阻塞
```