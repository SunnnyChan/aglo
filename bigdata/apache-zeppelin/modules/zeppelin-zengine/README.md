# zeppelin-zengine


* helium
* interpreter
* [notebook](notebook/README.md)
> * repo // Note持久化
> * serach // Note的全文检索
* plugin
* scheduler
* search
* storage
* ticket
* user
* util

## 记事本模块
* Note
```md
由paragraph组成，权限控制、共享和持久化的最小单位。
```
* [Paragraph](paragraph/README.md)
```md
代码执行的最小单位，负责获取代码文本，执行过程控制及返回结果获取。
```
* [Notebook](notebook/README.md)
```md
Note的manager类，负责Note的CURD，复制、导入导出；
Note和相关Interpreter配置和运行时映射关系的维护。
```
* NotebookServer
```md
主要是将其他类封装，提供WebSocket等通信服务。
```
## [记事本持久化模块](notebook/repo/README.md)

## 解释器执行调度器模块
```md
此处为“生产者——消费者”模型，Note 和 RemoteInterpreterServer 为生产者，
Scheduler 为消费者，缓冲区为 Scheduler 内部的Job队列。
产品为 org.apache.zeppelin.scheduler.Job 类。
```
* SchedulerFactory
```md
负责创建所有Scheduler，单例模式。
```
* Scheduler
```md
调度器接口，规定了所有调度器必须实现的方法。
```
FIFOScheduler
```md
先进先出调度器，不支持并发执行
```
ParallelScheduler
```md
并发调度器，支持并发执行
```
* RemoteScheduler
```md
远程interpreter的代理。
```
## 搜索服务
```md
SearchService
搜索服务接口，目前只有一个实现类LucenceSearch
LucenceSearch
对Note进行全文索引，方便提供搜索服务。
```