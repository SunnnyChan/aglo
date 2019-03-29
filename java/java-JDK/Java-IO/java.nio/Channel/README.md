# Channel
```java
在传统IO中，读取一个文件中的内容：
File file = new File("data.txt");
InputStream inputStream = new FileInputStream(file);
byte[] bytes = new byte[1024];
inputStream.read(bytes);
inputStream.close();
```
```md
InputStream 实际上就是为读取文件提供一个通道的。

可以将 NIO 中的Channel同传统IO中的Stream来类比。
但是要注意，传统IO中，Stream是单向的，比如InputStream只能进行读取操作，OutputStream只能进行写操作。
而Channel是双向的，既可用来进行读操作，又可用来进行写操作。
```

## Channel 对象
* [FileChannel](FileChannel.md)
* [DatagramChannel](DatagramChannel.md)
* [SocketChannel](SocketChannel.md)
* [ServerSocketChannel](ServerSocketChannel.md)

## 创建
```md
通过调用内部的open静态方法实现的，此方法是线程安全的。
```

## 读写
```md
不论哪种类型的Channel对象，都有read（要理解为从通道中读取，写入缓冲区中）和 
write（要理解为从缓冲区中读取数据，写入到通道中）方法，而且read和write方法都只针对ByteBuffer对象。
```
```md
要获取由通道传输过来的数据时，先调用channel.read（byteBufferObj）方法，
这个方法在内部调用了byteBufferObj对象的put方法，将通道中的数据写入缓冲区中。

接着调用byteBufferObj.flip()，然后调用byteBufferObj的get方法获取通道传过来的数据，
最后调用clear或compact方法转换成写模式，为下次channel.read做准备。
```
```md
要向通道发送数据时，先调channel.write（byteBufferObj）方法，
这个方法内部调用了byteBufferObj的get方法获取数据，然后将数据写入通道中。

当写入完成后调用clear或compact方法转换成写模式，为下次channel.write写入缓冲区取做准备。
```
