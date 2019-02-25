# Condition
```md
Condition从拥有监控方法（wait,notify,notifyAll）的Object对象中抽离出来成为独特的对象，
高效的让每个对象拥有更多的等待线程。
和锁对比起来，如果说用Lock代替synchronized，那么Condition就是用来代替Object本身的监控方法。

Condition实例跟Object本身的监控相似，同样提供wait()方法让调用的线程暂时挂起让出资源，
直到其他线程通知该对象转态变化，才可能继续执行。
Condition实例来源于Lock实例，通过Lock调用newCondition()即可。
Condition较Object原生监控方法，可以保证通知顺序。
```