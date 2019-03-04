# Apache Zeppelin


## Directories
* /bin
* /notebook  //默认的notebook的持久化存储目录
* /conf
```md
/conf/shiro.ini //apache shiro框架使用的权限控制文件

/conf/zeppelin-env.sh //${ZEPPELIN_HOME}/bin/common.sh脚本调用

/conf/zeppelin-site.xml //zeppelin的配置模板，被注释掉的 property 是 zeppelin 的默认配置

/conf/interpreter.json 
  zeppelin需要知道全局已经注册了哪些interpreter，以及这些interpreter的配置。
  InterpreterInfoSaving类的唯一实例会被持久化到 ${ZEPPELIN_HOME/conf/interpreter.json文件。
``` 

## Modules
* [zeppelin-server](modules/zeppelin-server/README.md)
```md
项目入口，通过Jetty内嵌服务器提供WebSocekt服务和RESTful服务，并且提供了基本的权限验证服务。
```
* [zeppelin-zengine](modules/zeppelin-zengine/README.md)
```md
实现 Notebook 的持久化和检索服务。
```
* [zeppelin-interpreter](modules/zeppelin-interpreter/README.md)
```md
抽象了 interpreter 接口，规定了解释器的功能。
并且提供了与 zeppelin-zengine 用 Thrift 进行通信的协议。
```
* zeppelin-web
```md
使用 AngluarJS 框架开发的前端。
```
* zeppelin-display
```md
实现前台 Angular 元素绑定后台数据。
```
* zeppelin-spark-dependencies
```md
此module中没有代码，具体作用是使用户可以使用zeppelin内嵌的Spark。
不过由于Zeppelin支持了太多的解释器，package size过大，已经有人提议在未来的版本中移除此模块。
```
* zeppelin-distribution
```md
此模块主要是为了 Zeppelin 打包使用。
编译完成之后，会在zeppelin-distribution/target/目录下生成分发包。
```
* helium-dev
```md
Zeppelin-0.7 以后新加入的模块，使 interpreter、storage 
等模块可以在运行时（Zeppelin不需要重启）加入到 Zeppelin 中。
不过目前 helium 相关的很多功能还处于 Experimental 阶段，因此不太建议在生产环境中使用。
``` 
![](pic/zeppelin-modules.png)

# References
* [zeppelin源码分析](https://blog.csdn.net/spacewalkman/article/category/6228596)
* [Apache Zeppelin 项目结构及代码分析](https://www.jianshu.com/p/02596c7a2342)