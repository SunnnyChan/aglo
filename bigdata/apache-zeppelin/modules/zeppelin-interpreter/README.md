# zeppelin-interpreter
```md
抽象了 interpreter 接口，规定了解释器的功能。
并且提供了与 zeppelin-zengine 用 Thrift 进行通信的协议。
```
* Interpreter
```md
Interpreter是一个抽象类，规定了所有解释器必须实现的功能。
所有解释器都要继承这个抽象类，通过不同的具体实现，来完成不同语言的解释执行。
```
* [RemoteInterpreterService](RemoteInterpreterService.md)

* InterpreterGroup
```md
一组Interpreter，用于启动启动和停止解释器JVM的最小单元。
```
* RemoteInterpreterProcess
```md
采用独立JVM启动interpreter的具体执行类
```
* RemoteInterpreter
```md
远程interpreter的本地代理
```
* InterpreterSetting
```md
维护interpreter相关元信息，维护note与interpreterGroup的关系
```
* InterpreterOption
```md
决定Zeppelin创建interpreter进程时的处理方式。
```
* InterpreterFactory
```md
负责创建interpreter实例，interpreter 配置文件的加载与持久化，interpreterSetting的管理。
```