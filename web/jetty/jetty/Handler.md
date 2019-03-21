# Handler
```md
HandlerWrapper
	它可以将一个 Handler 委托给另外一个类去执行
	如我们要将一个 Handler 加到 Jetty 中，那么就必须将这个 Handler 委托给 Server 去调用
	配合 ScopeHandler 类我们可以拦截 Handler 的执行，在调用 Handler 之前或之后，可以做一些另外的事情
		类似于 Tomcat 中的 Valve
```
```md
HandlerCollection
	以将多个 Handler 组装在一起，构成一个 Handler 链，方便我们做扩展。
```
```md
HandlerContainer
	Server
		ServletHandler
		SessionHandler
		SecurityHandler
		...
```
```md
核心Servlet处理类
	org.mortbay.jetty.servlet.ServletHandler
```