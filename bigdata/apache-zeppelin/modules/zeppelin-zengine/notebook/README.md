
* [Note 持久化](repo/README.md)

## Class
* [Note - 单个’记事本’的内存对象](Note.md)

* [Paragraph - Paragraph is a representation of an execution unit.](Paragraph.md)

* [Notebook - 笔记本相关操作的高级 API，如创建、移动和删除笔记或目录](Notebook.md)

* NotebookServer
```md
将 Notebook、Note、Paragraph、Interpreter等类封装的能力，
通过WebSocket的形式对web 客户端提供出去，所以其具体的职责包括： 
1. 维护WebSocket连接与Note之间映射关系 
2. 处理客户端和服务器之间的双向通信（通过WebSocket，具体的通信协议见：Message类），
    包括消息的序列化/反序列化，消息解析和服务端处理、处理结果的向客户端广播/单播发送等。 
3. Note的CRUD操作以及Paragraph的CRUD操作、执行、导入、导出时的权限控制 
4. 前后端AngularObject的双向bind处理 
5. WebSocket客户端合法性校验(checkOrigin)
```
***关于zeppelin采用WebSocket技术的必要性问题***
```md
zeppelin是共享式、Notebook式的大数据分析环境，以repl的方式执行以Paragraph为最小粒度的代码段。 
1. 首先repl的方式强调实时反馈执行结果，特别是在大数据环境下，一段代码可能需要执行很长时间，
    在执行的过程中，zeppelin的用户期望看到执行进度和中间结果，需要在前后端之间建立一个长连接，便于实时传递数据。 
2. 另外zeppelin的另一个亮点是其结果可视化能力，需要在前后台传递图片，并且支持较大数据量的传输的能力（相对传统http技术）。 
3. 再者，由于是共享式环境，一个Note可能被多个用户同时看到、甚至编辑，
    需要在各个已经打开了同一个Note的web客户端之间同步Note的代码、执行结果和进度信息。 
    基于以上3点，zeppelin采用WebSocket技术是水到渠成的事情。
```
