# Configure

* /conf/shiro.ini //apache shiro框架使用的权限控制文件

* /conf/zeppelin-env.sh //${ZEPPELIN_HOME}/bin/common.sh脚本调用

* /conf/zeppelin-site.xml //zeppelin的配置模板，被注释掉的 property 是 zeppelin 的默认配置

* /conf/interpreter.json 
```md
  zeppelin 需要知道全局已经注册了哪些 interpreter，以及这些interpreter的配置。
  InterpreterInfoSaving类 的唯一实例会被持久化到 ${ZEPPELIN_HOME/conf/interpreter.json文件。
``` 