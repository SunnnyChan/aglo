# What Is Apache Thrift
```md
是一个跨语言的轻量级RPC消息和数据交换框架。

Thrift是一个跨语言的服务部署框架，最初由Facebook于2007年开发，2008年进入Apache开源项目。
Thrift通过一个中间语言(IDL, 接口定义语言)来定义RPC的接口和数据类型，然后通过一个编译器生成不同语言的代码。
（目前支持C++,Java, Python, PHP, Ruby, Erlang, Perl, Haskell, C#, Cocoa, Smalltalk和OCaml）
并由生成的代码负责RPC协议层和传输层的实现。

Thrift为数据传输，数据序列化和应用程序级处理提供了清晰的抽象。
代码生成系统采用简单的定义语言作为输入，并跨编程语言生成代码，使用抽象堆栈构建可互操作的RPC客户端和服务器。
```
* 优点
```md
支持非常多的语言绑定
由文件生成目标代码，简单易用
消息定义文件支持注释
数据结构与传输表现的分离，支持多种消息格式
包含完整的客户端/服务端堆栈，可快速实现RPC
支持同步和异步通信
```
* 缺点
```md
和 protobuf 一样不支持动态特性
```
* 应用
```md
Facebook的开源的日志收集系统(scribe: https://github.com/facebook/scribe)
淘宝的实时数据传输平台(TimeTunnel http://code.taobao.org/p/TimeTunnel/wiki/index)
Evernote开放接口(https://github.com/evernote/evernote-thrift)
Quora(http://www.quora.com/Apache-Thrift)
HBase( http://abloz.com/hbase/book.html#thrift )
```
## 与Avro
* Schema处理
```md
Thrift 依赖IDL-->代码的生成，静态的。走代码生成，编译载入的流程。
Avro可以生成代码，后编译执行，但是还必须依赖IDL（meta元数据描述），也可以走动态解释执行IDL。
```
* 序列化的方式
```md
Thrift 提供多种序列化实现 TCompactProtocol，TBinaryProtocol
每个Field前面都是带Tag的，这个Tag用于标识这个域的类型和顺序ID（IDL中定义，用于Versioning）
在同一批数据里面，这些Tag的信息是完全相同的，当数据条数大的时候这显然就浪费了。

Avro，格式包括--》文件头中有schema+数据records(自描述)，只对感兴趣的部分反序列化，
schema允许定义数据的排序order，采用block链表结构，突破了用单一整型表示大小的限制。
比如Array或Map由一系列Block组成，每个Block包含计数器和对应的元素，计数器为0标识结束。
```
* RPC的服务
```md
Avro提供了
	ttpServer : 缺省,基于Jetty内核的服务
	NettyServer: 新的基于Netty的服务
		
Thrift提供了：
	TThreadPolServer: 多线程服务
	TNonBlockingServer: 单线程 non blocking的服务
	THsHaServer: 多线程 non blocking的服务
```