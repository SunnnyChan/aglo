# ByteBuffer

* 创建
```md
缓冲区类没有一种是可以直接实例化的，他们都是抽象类，但都包含了静态工厂方法创建相应的实例。
```
```java
ByteBuffer buffer = ByteBuffer.allocate(1024);
```
```md
allocate方法分配了一个具有指定大小底层数组的缓冲区对象，这个大小也就是上面提到的Capacity。
```
```java
byte array[] = new byte[1024];
ByteBuffer buffer = ByteBuffer.wrap(array);
```
```md
使用已经存在的数组来作为缓冲区对象的底层数组。

此时，buffer对象的底层数组指向了array，这意味着直接修改array数组也会使buffer对象读取的数据产生变化。
```
```java
byte[] bs = new byte[10];
ByteBuffer buffer = ByteBuffer.wrap(bs);
System.out.println(buffer.toString());

打印如下：
java.nio.HeapByteBuffer[pos=0 lim=10 cap=10]

新初始化的Buffer实例中，position = 0，limit=capacity=10
```
* 存取

* Buffer.flip()
```md
想要将刚刚写入的数据读出的话应该怎么做？
应该将position设为0：buffer.position(0)，就可以从正确的位置开始获取数据。

但是它是怎样知道何时到达我们所插入数据末端的呢？
这就是边界属性被引入的目的。边界属性指明了缓冲区有效内容的末端。
我们需要将limit设置为当前位置：buffer.limit(buffer.position())。

buffer.limit(buffer.position()).position(0);

buffer.flip()封装了这些操作，调用buffer.flip()后，limit设置为当前position值，position重置为0.
```
* Buffer.rewind()
```md
rewind()方法与filp()相似，但是不影响limit，他只是将position设为0，这样就可以从新读取已经读过的数据了。
```
* Buffer.mark()、Buffer.reset()
```md
Buffer.mark(),使缓冲区能够记住一个位置并在之后将其返回。
缓冲区的标记在mark()函数被调用之前是未定义的，调用时标记被设为当前位置的值。

reset()函数将位置设为当前的标记值。
如果标记值未定义，调用reset()将导致InvalidMarkException异常。
```
* Buffer.remaining()、Buffer.hasRemaining()
```md
remaining()函数将返回从当前位置到上界还剩余的元素数目。
hasRemaining()会返回是否已经达到缓冲区的边界。
```
* Buffer.clear()
```md
clear()函数将缓冲区重置为空状态。它并不改变缓冲区中的任何数据元素，而是仅仅将上界设为容量的值，并把位置设回 0。
```
* ByteBuffer.compact()
```md
compact()方法并不是Buffer接口中定义的，而是属于ByteBuffer。
如果Buffer中仍有未读的数据，且后续还需要这些数据，但是此时想要先写些数据，那么使用compact()方法。

compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。
limit属性依然像clear()方法一样，设置成capacity。现在Buffer准备好写数据了，但是不会覆盖未读的数据。
```
* ByteBuffer.equals()、ByteBuffer.compareTo()

* asReadOnlyBuffer() 只读缓冲区
```md
可以使用asReadOnlyBuffer()函数来生成一个只读的缓冲区视图。
这个新的缓冲区不允许使用put()，并且其isReadOnly()函数将会返回true。
对这一只读缓冲区的put()函数的调用尝试会导致抛出ReadOnlyBufferException异常。

两个缓冲区共享数据元素，拥有同样的容量，但每个缓冲区拥有各自的位置，上界和标记属性。
对一个缓冲区内的数据元素所做的改变会反映在另外一个缓冲区上。
```
* duplicate() 复制缓冲区
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