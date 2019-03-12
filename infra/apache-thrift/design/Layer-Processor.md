# Processor
```md
Processor 层对象由 Thrift 根据用户的 IDL 文件所生成, 我们通常不能随意指定.

主要有两个功能:
从 Protocol 层读取数据, 然后转交给对应的 handler 处理
将 handler 处理的结构发送 Prootcol 层.
```

