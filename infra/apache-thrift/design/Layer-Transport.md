# Transport
```md
Transport 层提供了从网络中读取数据或将数据写入网络的抽象
Transport 层和 Protocol 层相互独立, 
我们可以根据自己需要选择不同的 Transport 层, 而对上层的逻辑不造成任何影响.
```

```md
Thrift 的 Java 实现中, 我们使用接口 TTransport 来描述传输层对象, 这个接口提供的常用方法有:
open
close
read
write
flush
```
```md
在服务器端, 我们通常会使用 TServerTransport 来监听客户端的请求, 
并生成相对应的 Transport 对象, 这个接口提供的常用方法有:
open
listen
accept
close
```

## 数据传输方式(常用 Transport)
```md
TSocket -阻塞式socket
TFramedTransport – 以frame为单位进行传输，非阻塞式服务中使用。
TFileTransport – 以文件形式进行传输。
TMemoryTransport – 将内存用于I/O. java实现时内部实际使用了简单的ByteArrayOutputStream。
TZlibTransport – 使用zlib进行压缩， 与其他传输方式联合使用。当前无java实现。
```
