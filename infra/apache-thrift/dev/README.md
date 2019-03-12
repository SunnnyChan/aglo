# Develop by Thrift
* Create .thrift
```md
在使用 thrift 前, 需要提供一个 .thrift 后缀的文件, 其内容是使用 IDL 描述的服务接口信息。
```
```thrift
namespace java com.xys.thrift

service HelloWorldService {
    string sayHello(string name);
}
```
```md
定义了一个名为 HelloWorldService 的接口, 它有一个方法, 即 sayHello. 
当通过 thrift --gen java test.thrift 来生成 thrift 接口服务时, 会产生一个 HelloWorldService.java 的文件, 
在此文件中会定义一个 HelloWorldService.Iface 接口, 我们在服务器端实现此接口即可.
```

* Server
```md
实现服务处理接口 impl
创建 Processor
创建 Transport
创建 Protocol
创建 Server
启动 Server
```
```java
public class HelloServer {
    public static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws Exception {
        HelloServer server = new HelloServer();
        server.startServer();
    }

    public void startServer() throws Exception {
        // 创建 TProcessor
        TProcessor tprocessor = 
                new HelloWorldService.Processor<HelloWorldService.Iface>(new HelloWorldImpl());

        // 创建 TServerTransport, TServerSocket 继承于 TServerTransport
        TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
        
        // 创建 TProtocol
        TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();
        
        TServer.Args tArgs = new TServer.Args(serverTransport);
        tArgs.processor(tprocessor);
        tArgs.protocolFactory(protocolFactory);

        // 创建 TServer
        TServer server = new TSimpleServer(tArgs);
        // 启动 Server
        server.serve();
    }
}
```

* Client端
```md
创建 Transport
创建 Protocol
基于 Potocol 创建 Client
打开 Transport
调用服务相应的方法
```
```java
public class HelloClient {
    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 8080;
    public static final int TIMEOUT = 30000;

    public static void main(String[] args) throws Exception {
        HelloClient client = new HelloClient();
        client.startClient("XYS");
    }

    public void startClient(String userName) throws Exception {
        // 创建 TTransport
        TTransport transport = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
        // 创建 TProtocol
        TProtocol protocol = new TBinaryProtocol(transport);

        // 创建客户端.
        HelloWorldService.Client client = new HelloWorldService.Client(protocol);
        
        // 打开 TTransport
        transport.open();
        
        // 调用服务方法
        String result = client.sayHello(userName);
        System.out.println("Result: " + result);

        transport.close();
    }
}
```

## 实例
```maven
<dependency>
    <groupId>org.apache.thrift</groupId>
    <artifactId>libthrift</artifactId>
    <version>0.10.0</version>
</dependency>
```
* thrift 文件
***test.thrift***
```md
namespace java com.xys.thrift

service HelloWorldService {
    string sayHello(string name);
}
```
> * 编译
```md
cd src/main/resources/
thrift --gen java test.thrift
mv gen-java/com/xys/thrift/HelloWorldService.java ../java/com/xys/thrift 
```
```md
当执行 thrift --gen java test.thrift 命令后, 会在当前目录下生成一个 gen-java 目录, 
其中会以包路径格式存放着生成的服务器端 thrift 代码, 将其拷贝到工程对应的目录下即可。
```
* 服务实现
```java
public class HelloWorldImpl implements HelloWorldService.Iface {
    public HelloWorldImpl() {
    }

    @Override
    public String sayHello(String name) throws TException {
        return "Hello, " + name;
    }
}
```
* 服务端/客户端实现
> * TSimpleServer 服务器模型
```java
public class SimpleHelloServer {
    public static final int SERVER_PORT = 8080;

    public static void main(String[] args) throws Exception {
        SimpleHelloServer server = new SimpleHelloServer();
        server.startServer();
    }

    public void startServer() throws Exception {
        TProcessor tprocessor = new HelloWorldService.Processor<HelloWorldService.Iface>(
                new HelloWorldImpl());

        TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
        TSimpleServer.Args tArgs = new TSimpleServer.Args(serverTransport);
        tArgs.processor(tprocessor);
        tArgs.protocolFactory(new TBinaryProtocol.Factory());

        TServer server = new TSimpleServer(tArgs);

        server.serve();
    }
}
```
```md
我们在服务器端的代码中, 没有显示地指定 Transport 的类型, 
是因为 TSimpleServer.Args 在构造时, 会指定一个默认的 TransportFactory, 
当有新的客户端连接时, 就会生成一个 TSocket 的 Transport 实例. 
由于这一点, 我们在客户端实现时, 也就需要指定客户端的 Transport 为 TSocket 才行.
```
```java
public class SimpleHelloClient {
    public static final String SERVER_IP = "localhost";
    public static final int SERVER_PORT = 8080;
    public static final int TIMEOUT = 30000;

    public void startClient(String userName) throws Exception {
        TTransport transport = null;

        transport = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
        // 协议要和服务端一致
        TProtocol protocol = new TBinaryProtocol(transport);
        HelloWorldService.Client client = new HelloWorldService.Client(
                protocol);
        transport.open();
        String result = client.sayHello(userName);
        System.out.println("Result: " + result);

        transport.close();
    }

    public static void main(String[] args) throws Exception {
        SimpleHelloClient client = new SimpleHelloClient();
        client.startClient("XYS");
    }
}
```

## Resources
* [Thrift 简易入门与实战](https://segmentfault.com/a/1190000008606491)