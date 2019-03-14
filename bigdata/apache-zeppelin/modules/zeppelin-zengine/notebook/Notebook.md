# org.apache.zeppelin.notebook.NoteBook
```md
Note的manager类，负责Notse的CURD，复制、导入导出；
Note和相关Interpreter配置和运行时映射关系的维护。
```
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
