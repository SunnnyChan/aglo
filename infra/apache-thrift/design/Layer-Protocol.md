# Protocol
```md
这一层的作用是内存中的数据结构转换为可通过 Transport 传输的数据流或者反操作, 即我们所谓的 序列化 和 反序列化.
```
### 传输协议
```md
TBinaryProtocol – 二进制格式.
TCompactProtocol – 压缩格式
TJSONProtocol – JSON格式
TSimpleJSONProtocol –提供JSON只写协议, 生成的文件很容易通过脚本语言解析。
TDebugProtocol – 使用易懂的可读的文本格式，以便于debug
```
***注意, 客户端和服务器的协议要一样.***

