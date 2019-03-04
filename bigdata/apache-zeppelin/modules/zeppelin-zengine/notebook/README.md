
* [Note 持久化](repo/README.md)

## Class
* Note 单个’记事本’的内存对象
```md
是zeppelin管理的最小单位，无论是做权限控制、共享、还是持久化，都是以Note为粒度的。
```
```md
从类关系上看，Note是由一些列的有序Paragraph组成。
因此其绝大部分职责都是与管理Paragraph有关：
1. Paragraph的CRUD、相对顺序控制 
2. 与处理前后端数据双向推送的AngularObject的管理 
3. 整体和单个Paragraph 执行，以及执行过程的基于Observer模式的执行过程Hook 
4. Note基本的样式外观控制 

为了“分离关注点”，其他的功能，如： 
1. Note相关的Interpreter加载和初始化 
2. 持久化与反持久化，包括延迟持久化 
3. 权限控制 
```
* Paragraph
```md
Paragraph代表着一段代码以及支撑其执行所需要的“环境信息”，是代码执行的最小单位。

Paragraph的职责如下： 
1. 获取代码文本，并解析分离类似%spark的interpreter声明段和可执行代码段。 
2. 代码执行，以及执行过程控制（进度和终止） 
3. 代码执行结果获取 
4. 代码中变量查找以及替换
```
* Notebook
```md
实际上是Note的Manager，职责如下：
Note的CRUD，克隆、导入/导出
Note和相关Interpreter配置时和运行时映射关系维护
Note cron式调度执行控制 

其他所有Note公共的服务，都交给ZeppelinServer类来注入
NotebookRepo	Note的持久化服务
SearchService	Note的全文检索服务
NotebookAuthorization	Note的Authorization服务
Credentials	数据源相关的“用户/密码”服务
```
* NotebookServer
```md
将Notebook、Note、Paragraph、Interpreter等类封装的能力，
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


