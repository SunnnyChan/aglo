# Apache Zeppelin

## [WhatIs](WhatIs.md)

## Project Hierarchy
* /bin
* /notebook  //默认的notebook的持久化存储目录
> * [持久化存储实例](workflow/note/note.json.md)

* [/conf](modules/Conf.md)

## [Modules](modules/README.md)
* [zeppelin-server](modules/zeppelin-server/README.md)
* [zeppelin-zengine](modules/zeppelin-zengine/README.md)
* [zeppelin-interpreter](modules/zeppelin-interpreter/README.md)

## WorkFlow
![](pic/zeppelin-exec.jpg)

* [Paragraph](workflow/paragraph/README.md)
* [Note](workflow/Note/README.md)
* [Interpreter](workflow/interpreter/README.md)

## Deploy
```md
bin
conf
interpreter
lib
LICENSE
licenses
local-repo
logs
notebook
NOTICE
README.md
run
spark-warehouse
webapps
zeppelin-web-0.8.1.war
```
### /interpreter
* [interpreter-setting.json](workflow/interpreter/Spark_interperter-setting.json.md)

## References
* [zeppelin 源码分析](https://blog.csdn.net/spacewalkman/article/category/6228596)