# FileChannel
```md
使用FileChannel可以从文件读或者向文件写入数据。
```
```md
在文件通道中read和write方法都是阻塞的，对于read方法，除非遇到文件结束，否则会把缓冲区的剩余空间读满再返回。
对于write方法，会一次性把缓冲区中的内容全部写入到文件中才会返回。
```
 ## 示例
```java
 File file = new File("data.txt");
 FileOutputStream outputStream = new FileOutputStream(file);
 FileChannel channel = outputStream.getChannel();
 ByteBuffer buffer = ByteBuffer.allocate(1024);
 String string = "java nio";
 buffer.put(string.getBytes());
 buffer.flip(); //此处必须要调用buffer的flip方法
 channel.write(buffer);
 channel.close();
 outputStream.close();
```
