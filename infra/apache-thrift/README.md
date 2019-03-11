# [Apache Thrift](https://github.com/apache/thrift)

* [What Is Apache Thrift](WhatIs.md)

* 组成
```md
	编译器
		在compiler目录下，采用C++编写
		作用是将用户定义的thrift文件编译生成对应语言的代码，而服务器是事先已经实现好的、可供用户直接使用的RPC Server（当然，用户也很容易编写自己的server）。
		同大部分编译器一样，Thrift编译器（采用C++语言编写）也分为词法分析、语法分析等步骤
		Thrift使用了开源的flex和Bison进行词法语法分析（具体见thrift.ll和thrift.yy），经过语法分析后，Thrift根据对应语言的模板（在compiler\cpp\src\generate目录下）生成相应的代码。
		Thrift最重要的组件是编译器（采用C++编写），它为用户生成了网络通信相关的代码，从而大大减少了用户的编码工作。
	服务器
		在lib目录下
		对于服务器实现而言，Thrift仅包含比较经典的服务器模型，比如单线程模型（TSimpleServer），线程池模型（TThreadPoolServer）、一个请求一个线程（TThreadedServer）和非阻塞模型(TNonblockingServer)等。
```
* 网络栈
```md
	Transport
		Transport层提供了一个简单的网络读写抽象层。
		使得thrift底层的transport从系统其它部分（如：序列化/反序列化）解耦
		接口提供的方法
			open
			close
			read
			write
			flush
		Thrift使用ServerTransport接口接受或者创建原始transport对象
			ServerTransport用在server端，为到来的连接创建Transport对象
			open
			listen
			accept
			close
	Protocol
		Protocol抽象层定义了一种将内存中数据结构映射成可传输格式的机制。
		Protocol定义了datatype怎样使用底层的Transport对自己进行编解码。
		Protocol的实现要给出编码机制并负责对数据进行序列化。
	 Processor
		Processor封装了从输入数据流中读数据和向数据数据流中写数据的操作。
		读写数据流用Protocol对象表示
		与服务相关的processor实现由编译器产生
		Processor主要工作流程如下
			从连接中读取数据（使用输入protocol），将处理授权给handler（由用户实现），最后将结果写到连接上（使用输出protocol）。
```

* 传输格式
```md
	TBinaryProtocol – 二进制格式.
	TCompactProtocol – 压缩格式
	TJSONProtocol – JSON格式
	TSimpleJSONProtocol –提供JSON只写协议, 生成的文件很容易通过脚本语言解析。
	TDebugProtocol – 使用易懂的可读的文本格式，以便于debug
```
* 数据传输方式
```md
	TSocket -阻塞式socket
	TFramedTransport – 以frame为单位进行传输，非阻塞式服务中使用。
	TFileTransport – 以文件形式进行传输。
	TMemoryTransport – 将内存用于I/O. java实现时内部实际使用了简单的ByteArrayOutputStream。
	TZlibTransport – 使用zlib进行压缩， 与其他传输方式联合使用。当前无java实现。
```
* 服务模型
```md
	TSimpleServer – 简单的单线程服务模型，常用于测试
	TThreadPoolServer – 多线程服务模型，使用标准的阻塞式IO。
	TNonblockingServer – 多线程服务模型，使用非阻塞式IO（需使用TFramedTransport数据传输方式）
```

* [Thrift 类](ClassThrift.md)

## [Development](dev/README.md)
* [Grammar](dev/Grammar.md)

## Resources
* [Java Tutorial](http://thrift.apache.org/tutorial/java)