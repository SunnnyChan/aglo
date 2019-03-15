
## Create .thrift
```md
在使用 thrift 前, 需要提供一个 .thrift 后缀的文件, 其内容是使用 IDL 描述的服务接口信息。
```
* demo.thrift
```thrift
namespace java com.gemantic.analyse.thrift.index

struct NewsModel{
  1:i32 id ;
  2:string title;
  3:string content;
  4:string media_from;
  5:string author;
}

service IndexNewsOperatorServices {
  string indexNews(1:NewsModel indexNews),
  bool deleteArtificiallyNews(1:i32 id)
}
```
## Generate Java Class File
```sh
$ thrift --gen java demo.thrift

$ls -1 gen-java/com/gemantic/analyse/thrift/index/
IndexNewsOperatorServices.java
NewsModel.java
```
* [IndexNewsOperatorServices.java](IndexNewsOperatorServices.java)
* [NewsModel.java](NewsModel.java)

## Java Server端
* 实现服务处理接口 [IndexNewsOperatorServicesImpl.java](IndexNewsOperatorServicesImpl.java)
* Java 服务端 [ThriftServerTest.java](ThriftServerTest.java)
```md
创建 Processor
创建 Transport
创建 Protocol
创建 Server
启动 Server
```
## Java Client端 [ThriftClientTest.java](ThriftClientTest.java)
```md
创建 Transport
创建 Protocol
基于 Potocol 创建 Client
打开 Transport
调用服务相应的方法
```