# java.nio

## WhatIs(WhatIs.md)

##
* 只读缓冲区
```md
可以使用asReadOnlyBuffer()函数来生成一个只读的缓冲区视图。
这个新的缓冲区不允许使用put()，并且其isReadOnly()函数将会返回true。
对这一只读缓冲区的put()函数的调用尝试会导致抛出ReadOnlyBufferException异常。

两个缓冲区共享数据元素，拥有同样的容量，但每个缓冲区拥有各自的位置，上界和标记属性。
对一个缓冲区内的数据元素所做的改变会反映在另外一个缓冲区上。
```
* 复制缓冲区
```md
duplicate()函数创建了一个与原始缓冲区相似的新缓冲区。
两个缓冲区共享数据元素，拥有同样的容量，但每个缓冲区拥有各自的位置，上界和标记属性。
对一个缓冲区内的数据元素所做的改变会反映在另外一个缓冲区上。
这一副本缓冲区具有与原始缓冲区同样的数据视图。
如果原始的缓冲区为只读，或者为直接缓冲区，新的缓冲区将继承这些属性。

复制一个缓冲区会创建一个新的Buffer对象，但并不复制数据。
原始缓冲区和副本都会操作同样的数据元素。
```

* 直接缓冲区
```md
直接缓冲区意味着所分配的这段内存是堆外内存，
而我们通过ByteBuffer.allocate(int capacity)或者ByteBuffer.wrap(byte[] array)分配的内存是堆内存，
其返回的实例为HeapByteBuffer，HeapByteBuffer中持有一个byte数组，这个数组所占有的内存是堆内内存。
```
```md
直接ByteBuffer是通过调用ByteBuffer.allocateDirect(int capacity)函数来创建的。
```
* 缓冲区分片
```md
slice() 方法根据现有的缓冲区创建一种 子缓冲区 。
也就是说，它创建一个新的缓冲区，新缓冲区与原来的缓冲区的一部分共享数据。
```
```md
对这个缓冲区 分片 ，以创建一个包含槽 3 到槽 6 的子缓冲区。
在某种意义上，子缓冲区就像原来的缓冲区中的一个 窗口。
窗口的起始和结束位置通过设置 position 和 limit 值来指定。
```
```java
print(buffer, bs);
buffer.position( 3 ).limit( 7 );
ByteBuffer slice = buffer.slice();
print(slice, slice.array());

打印如下：
java.nio.HeapByteBuffer[pos=4 lim=10 cap=10]
lloyoy$$$$
java.nio.HeapByteBuffer[pos=0 lim=4 cap=4]
lloyoy$$$$
```
```md
slice 是缓冲区的 子缓冲区 。
不过， slice 和 buffer 共享同一个底层数据数组。
```

* 类型视图缓冲区
```md
在进行IO操作时，可能会使用各种ByteBuffer类去读取文件内容，
接收来自网络连接的数据使用各种ByteBuffer类去读取文件内容，接收来自网络连接的数据等。
一旦数据到达了您的 ByteBuffer，我们需要对他进行一些操作。
```
```md
ByteBuffer类允许创建视图来将byte型缓冲区字节数据映射为其它的原始数据类型。
asLongBuffer()函数创建一个将八个字节型数据当成一个 long 型数据来存取的视图缓冲区。
```
```java
public abstract class ByteBuffer extends Buffer implements Comparable{
    // 这里仅列出部分API
    public abstract CharBuffer asCharBuffer();
    public abstract ShortBuffer asShortBuffer();
    public abstract IntBuffer asIntBuffer();
    public abstract LongBuffer asLongBuffer();
    public abstract FloatBuffer asFloatBuffer();
    public abstract DoubleBuffer asDoubleBuffer();
}

buffer.clear();
buffer.order(ByteOrder.BIG_ENDIAN);//指定字节序
buffer.put (0, (byte)0);
buffer.put (1, (byte)'H');
buffer.put (2, (byte)0);
buffer.put (3, (byte)'i');
buffer.put (4, (byte)0);
buffer.put (5, (byte)'!');
buffer.put (6, (byte)0);

CharBuffer charBuffer = buffer.asCharBuffer();
System.out.println("pos=" + charBuffer.position() + " limit=" + charBuffer.limit() + " cap=" + charBuffer.capacity());;
print(charBuffer, bs);

打印如下：
pos=0 limit=5 cap=5
Hi!   
$H$i$!$$$$
```
```md
新的缓冲区的容量是字节缓冲区中存在的元素数量除以视图类型中组成一个数据类型的字节数。视图缓冲区的第一个元素从创建它的ByteBuffer对象的位置开始(positon()函数的返回值)。
```
## [API](API.md)
