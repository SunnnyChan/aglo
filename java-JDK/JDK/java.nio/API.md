# API

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