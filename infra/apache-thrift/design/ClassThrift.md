# Class Thrift
```md
concurrency：并发和时钟管理方面的库
processor：Processor相关类
protocol：Protocol相关类
	
	TBinaryProtocol：二进制编码
	TJSONProtocol：JSON编码
	TCompactProtocol：密集二进制编码
	TDebugProtocol：以用户易读的方式组织数据
transport：transport相关类
	TFileTransport：文件（日志）传输类，允许client将文件传给server，允许server将收到的数据写到文件中。
	THttpTransport：采用Http传输协议进行数据传输
	TSocket：采用TCP Socket进行数据传输
	TZlibTransport：压缩后对数据进行传输，或者将收到的数据解压
	下面几个类主要是对上面几个类地装饰（采用了装饰模式），以提高传输效率。
	TBufferedTransport：对某个Transport对象操作的数据进行buffer，即从buffer中读取数据进行传输，或者将数据直接写入buffer
	TFramedTransport：同TBufferedTransport类似，也会对相关数据进行buffer，同时，它支持定长数据发送和接收。
	TMemoryBuffer：从一个缓冲区中读写数据
server：server相关类
	TSimpleServer：简单的单线程服务器，主要用于测试
	TThreadPoolServer：使用标准阻塞式IO的多线程服务器
	TNonblockingServer：使用非阻塞式IO的多线程服务器，TFramedTransport必须使用该类型的server
```