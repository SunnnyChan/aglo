# ThreadPoolExecutor
* [类结构图 参考](https://github.com/SunnnyChan/sc.drill-code/blob/master/java/java-JDK/JDK/java.util.concurrent/pic/JUC-Thread-Class-Map.png)

```java
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;
```
## 线程状态和工作线程数量
```java
    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;
```
![](../pic/thread-pool-status.jpg)

* RUNNING
```md
运行状态，该状态下线程池可以接受新的任务，也可以处理阻塞队列中的任务
执行 shutdown 方法可进入 SHUTDOWN 状态
执行 shutdownNow 方法可进入 STOP 状态
```
* SHUTDOWN
```md
待关闭状态，不再接受新的任务，继续处理阻塞队列中的任务
当阻塞队列中的任务为空，并且工作线程数为0时，进入 TIDYING 状态
```
* STOP
```md
停止状态，不接收新任务，也不处理阻塞队列中的任务，并且会尝试结束执行中的任务
当工作线程数为0时，进入 TIDYING 状态
```
* TIDYING
```md
整理状态，此时任务都已经执行完毕，并且也没有工作线程
执行 terminated 方法后进入 TERMINATED 状态
```
* TERMINATED
```md
终止状态，此时线程池完全终止了，并完成了所有资源的释放
```

### ctl
```java
 private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
```
```java
    // Packing and unpacking ctl
    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }
```
```md
ctl 的高3位（至少需要3位才能表示得了5种状态）用来表示线程池的状态(runState)，低29位用来表示工作线程的个数(workerCnt)
```
## 核心线程数和最大线程数
```md
创建线程是有代价的，不能每次要执行一个任务时就创建一个线程，
但是也不能在任务非常多的时候，只有少量的线程在执行，这样任务是来不及处理的，
而是应该创建合适的足够多的线程来及时的处理任务。

为了解决这个问题，线程池设计了两个变量来协作。
```

* corePoolSize
```java
private volatile int corePoolSize;
```
```md
用来表示线程池中的核心线程的数量，也可以称为可闲置的线程数量。
```

* maximumPoolSize
```java
private volatile int maximumPoolSize;
```
```md
maximumPoolSize 用来表示线程池中最多能够创建的线程数量
```

## ThreadFactory

## 缓存任务的阻塞队列
```md
当线程池接收到一个任务时，如果工作线程数没有达到corePoolSize，那么就会新建一个线程，并绑定该任务，
直到工作线程的数量达到 corePoolSize 前都不会重用之前的线程。
```
```md
当工作线程数达到 corePoolSize 了，这时又接收到新任务时，会将任务存放在一个阻塞队列中等待核心线程去执行。
```
***为什么不直接创建更多的线程来执行新任务?***
```md
核心线程中很可能已经有线程执行完自己的任务了，或者有其他线程马上就能处理完当前的任务，
并且接下来就能投入到新的任务中去，所以阻塞队列是一种缓冲的机制，给核心线程一个机会让他们充分发挥自己的能力。

另外一个值得考虑的原因是，创建线程毕竟是比较昂贵的，不可能一有任务要执行就去创建一个新的线程。
```
```md
所以我们需要为线程池配备一个阻塞队列，用来临时缓存任务，这些任务将等待工作线程来执行。
```
* 有界的队列
```md
如果是有界队列，那么当阻塞队列中装满了等待执行的任务，这时再有新任务提交时，
线程池就需要创建新的“临时”线程来处理，相当于增派人手来处理任务。
```
* 无界的队列
```md
如果是无界队列，那么当核心线程都在忙的时候，所有新提交的任务都会被存放在该无界队列中，
这时最大线程数将变得没有意义，因为阻塞队列不会存在被装满的情况。
```

## 非核心线程存活时间
```md
有界队列时，创建的“临时”线程是有存活时间的。
不可能让他们一直都存活着，当阻塞队列中的任务被执行完毕，并且又没有那么多新任务被提交时，“临时”线程就需要被回收销毁，
在被回收销毁之前等待的这段时间，就是非核心线程的存活时间，也就是 keepAliveTime 属性。
```
***那么什么是“非核心线程”呢？是不是先创建的线程就是核心线程，后创建的就是非核心线程呢？***
```md
其实核心线程跟创建的先后没有关系，而是跟工作线程的个数有关，
如果当前工作线程的个数大于核心线程数，那么所有的线程都可能是“非核心线程”，都有被回收的可能。
```
```md
一个线程执行完了一个任务后，会去阻塞队列里面取新的任务，在取到任务之前它就是一个闲置的线程。
```
```md
取任务的方法有两种，
一种是通过 take() 方法一直阻塞直到取出任务，
另一种是通过 poll(keepAliveTime，timeUnit) 方法在一定时间内取出任务或者超时，
如果超时这个线程就会被回收，请注意核心线程一般不会被回收。
```
***那么怎么保证核心线程不会被回收呢？***
```md
还是跟工作线程的个数有关，每一个线程在取任务的时候，线程池会比较当前的工作线程个数与核心线程数：
```
```md
如果工作线程数小于当前的核心线程数，则使用第一种方法取任务，也就是没有超时回收，这时所有的工作线程都是“核心线程”，他们不会被回收；
如果大于核心线程数，则使用第二种方法取任务，一旦超时就回收，所以并没有绝对的核心线程，只要这个线程没有在存活时间内取到任务去执行就会被回收。
```
```md
所以每个线程想要保住自己“核心线程”的身份，必须充分努力，尽可能快的获取到任务去执行，这样才能逃避被回收的命运。
```
```md
核心线程一般不会被回收，但是也不是绝对的，如果我们设置了允许核心线程超时被回收的话，
那么就没有核心线程这种说法了，所有的线程都会通过 poll(keepAliveTime, timeUnit) 来获取任务，
一旦超时获取不到任务，就会被回收，一般很少会这样来使用，除非该线程池需要处理的任务非常少，并且频率也不高，不需要将核心线程一直维持着。
```
## 拒绝策略
```md
虽然我们有了阻塞队列来对任务进行缓存，这从一定程度上为线程池的执行提供了缓冲期，
但是如果是有界的阻塞队列，那就存在队列满的情况，也存在工作线程的数据已经达到最大线程数的时候。
如果这时候再有新的任务提交时，显然线程池已经心有余而力不足了，
因为既没有空余的队列空间来存放该任务，也无法创建新的线程来执行该任务了，所以这时我们就需要有一种拒绝策略，即 handler。

拒绝策略是一个 RejectedExecutionHandler 类型的变量，
用户可以自行指定拒绝的策略，如果不指定的话，线程池将使用默认的拒绝策略：抛出异常。
```
```md
在线程池中还为我们提供了很多其他可以选择的拒绝策略：
直接丢弃该任务
使用调用者线程执行该任务
丢弃任务队列中的最老的一个任务，然后提交该任务
```

## 工作流程
