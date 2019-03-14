# org.apache.zeppelin.notebook.Note
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
```
```md
为了“分离关注点”，其他的功能，如： 
1. Note相关的Interpreter加载和初始化 
2. 持久化与反持久化，包括延迟持久化 
3. 权限控制 
```
