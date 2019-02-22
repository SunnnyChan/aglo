# How to Read Source Code?

## Java源码
* 建议从JDK源码开始读起
```md
从JDK的工具包开始
然后是core包
Java IO 包，是对继承和接口运用得最优雅的案例，看看《Java In A Nutshell》，里面有整个Java IO的架构图
```
* Java Web 项目源码阅读
```md
表结构 → web.xml → mvc → db → spring ioc → log→ 代码
```
* Java 框架源码阅读
```md
Spring、MyBatis这类框架，
在读Spring源码前，一定要先看看《J2EE Design and Development》这本书，它是Spring的设计思路。

可以选择去买书籍来帮助阅读，当然这是可取的。
```

## 参考
* [如何阅读Java源码](https://www.toutiao.com/a6660259363988439555/)