# compiler

```md
在 compiler 目录下，采用C++编写
作用是将用户定义的thrift文件编译生成对应语言的代码，
而服务器是事先已经实现好的、可供用户直接使用的RPC Server（当然，用户也很容易编写自己的server）。

同大部分编译器一样，Thrift编译器（采用C++语言编写）也分为词法分析、语法分析等步骤
Thrift使用了开源的flex和Bison进行词法语法分析（具体见thrift.ll和thrift.yy），
经过语法分析后，Thrift根据对应语言的模板（在compiler\cpp\src\generate目录下）生成相应的代码。
Thrift最重要的组件是编译器（采用C++编写），它为用户生成了网络通信相关的代码，从而大大减少了用户的编码工作。
```