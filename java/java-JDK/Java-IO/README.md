# Java IO

![](pic/IO-Mode.jpg)

* BIO (Blocking I/O)
* NIO (Non-blocking I/O)
```md
socket主要的读、写、注册和接收函数，在等待就绪阶段都是非阻塞的，真正的I/O操作是同步阻塞的（消耗CPU但性能非常高）。
```
* AIO (Async I/O)
