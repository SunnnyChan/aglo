# Develop by Thrift

* Server
```md
	（1）  创建一个transport对象
	（2）  为transport对象创建输入输出protocol
	（3）  基于输入输出protocol创建processor
	（4）  等待连接请求并将之交给processor处理
```

* Client端
```md

	（1）	定义TTransport，为你的client设置传输方式（如socket， http等）。
	（2）	定义Protocal，使用装饰模式（Decorator设计模式）封装TTransport，为你的数据设置编码格式（如二进制格式，JSON格式等）
	（3）	实例化client对象，调用服务接口。
```