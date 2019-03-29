# Channel
```md
Java NIO的通道类似 Java IO 中的流。
```

## Channel 对象
* FileChannel：
```md
文件
阻塞
```
* DatagramChannel
```md
UDP协议
阻塞或非阻塞

connect方法仅用于客户端到服务器端的连接，连接的作用仅仅是避免每次发送和接受数据时的安全检查，
提高发送和接受数据的效率，而不是像TCP连接那样表示握手的意思。
客户端通道只有调用了connect方法后，才能使用read和write方法读写数据。

客户端也可以不事先调用connet方法，而直接使用receive方法和send方法来实现数据的收发。
```
* SocketChannel
```md
TCP协议
阻塞或非阻塞

TCP客户端和TCP服务器端都用它来传输数据。
客户端必须调用connect方法去连接服务器。
```
* ServerSocketChannel
```md
用于TCP服务器端的监听和链接
对每一个新进来的连接都会创建一个SocketChannel。

阻塞或非阻塞

服务器端用于创建TCP连接的通道，只能对accept事件感兴趣。
accept方法会返回一个已和客户端连接好的SocketChannel通道，它才服务器是真正传输数据的通道。
```

## 创建
```md
通过调用内部的open静态方法实现的，此方法是线程安全的。
```

## 读写
```md
不论哪种类型的Channel对象，都有read（要理解为从通道中读取，写入缓冲区中）和 
write（要理解为从缓冲区中读取数据，写入到通道中）方法，而且read和write方法都只针对ByteBuffer对象。
```
```md
要获取由通道传输过来的数据时，先调用channel.read（byteBufferObj）方法，
这个方法在内部调用了byteBufferObj对象的put方法，将通道中的数据写入缓冲区中。

接着调用byteBufferObj.flip()，然后调用byteBufferObj的get方法获取通道传过来的数据，
最后调用clear或compact方法转换成写模式，为下次channel.read做准备。
```
```md
要向通道发送数据时，先调channel.write（byteBufferObj）方法，
这个方法内部调用了byteBufferObj的get方法获取数据，然后将数据写入通道中。

当写入完成后调用clear或compact方法转换成写模式，为下次channel.write写入缓冲区取做准备。
```
