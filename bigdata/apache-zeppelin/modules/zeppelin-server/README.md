# zeppelin-server
```md
ZeppelinServer是各个组件的”组装者”，它是系统的主入口。
```
* 职责
```md
1. 内嵌jetty服务器，支持以WebSocket和REST两种方式对外暴露系统功能 
2. 创建NotebookServer实例，建立起处理WebSocket Connection和消息处理的服务端 
3. 创建Notebook需要的相关依赖，如 Note持久化服务(NotebookRepo)、
    Note的全文索引服务（SearchService），并完成向Note、Paragraph的注入。 
4. Note权限配置文件的加载以及初始化 
5. InterpreterFactory的初始化 
6. 初始化动态依赖加载器(DependencyResolver)
```