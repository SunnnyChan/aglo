# Modules

* [zeppelin-server](zeppelin-server/README.md)
* [zeppelin-zengine](zeppelin-zengine/README.md)
* [zeppelin-interpreter](zeppelin-interpreter/README.md)

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