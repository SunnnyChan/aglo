# java.nio

*  NIO vs. IO
```md
IO是面向流的，而NIO是面向块的。
面向流是指：系统一次一个字节的处理数据，一个输入流产生一个字节的数据，一个输出流消费一个字节的数据。
面向块是指：以块的形式处理数据。每一个操作都在一步中产生或者消费一个数据块。在NIO中就是Buffer对象。
```
```md
按块的方式处理数据要比按流的方式处理数据快，
因为按块的方式读取或写入数据所执行的系统调用要远少于一次一个字节的方式，类似于BufferedInputStream的方式。
```

*  Buffer(缓冲区) 
```md
实质上是一个容器对象，它包含一些要写入或者刚读出的数据。
在 NIO 库中，所有数据都是用缓冲区处理的。
在读取数据时，它是直接读到缓冲区中的。在写入数据时，它是写入到缓冲区中的。
```
```md
缓冲区实质上是一个数组。
通常它是一个字节数组，但是也可以使用其他种类的数组。
但是一个缓冲区不 仅仅 是一个数组。
缓冲区提供了对数据的结构化访问，而且还可以跟踪系统的读/写进程。
```
```md
除了boolean类型之外，java为每种基本类型都封装了对应的Buffer对象。
```
> * ByteBuffer
```md
实质上是对byte数组进行了封装，其内部是一个byte数组，
ByteBuffer对象提供了一些实用的API供我们去操作这个数组，完成一些读取或写入的功能。
```
> * 状态变量
**容量(Capacity)：缓冲区能够容纳的数据元素的最大数量**
```md
指定了底层数组的大小。在缓冲区创建时被设定，并且永远不能被改变。
```
**位置(Position)：下一个要被读或写的元素的索引**
```md
指定了下一个字节将放到数组的哪一个元素中。
比如，从通道中读三个字节到缓冲区中，那么缓冲区的 position 将会设置为3，指向数组中第四个元素。

初始的position值为0。
```
**边界(Limit)**
```md
缓冲区的第一个不能被读或写的元素。或者说，缓冲区中现存元素的计数。

在写模式下，Buffer的limit表示你最多能往Buffer里写多少数据。

当切换Buffer到读模式时， limit表示你最多能读到多少数据。
因此，当切换Buffer到读模式时，limit会被设置成写模式下的position值。
```
**标记(Mark)：一个备忘位置**
```md
调用 mark()来设定 mark = postion。调用 reset()设定 position = mark。
初始的mark值为-1。
```
> * 上面四个属性遵循以下的关系：
```md
0 <= mark <= position <= limit <= capacity
```