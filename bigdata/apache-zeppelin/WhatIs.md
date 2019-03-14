# What Is Zeppelin

```md
是一款基于Web的多用途笔记本，用于数据分析和数据可视化。
```

## 技术栈
### 前端
```md
主要使用AngularJS框架开发，使用Node.js进行包的构建。
使用Jupyter Notebook实现记事本功能，并且使用了Highlight.js和Bootstrap。
```
### 后端
```md
使用了Jetty作为内嵌服务器，通信方式除了WebSocket，还使用Jersey框架提供了Restful服务。

由于Zeppelin的解释器（Interpreter）是独立的JVM进程，
因此Zeppelin 使用 Apache Commons Exec 框架来使主进程可以启动解释器进程，
并且使用Thrift框架在主进程与解释器进程间进行通信。

使用 Apache Shiro进行权限控制，
使用Apache Lucence对Note进行全文检索。
```