# LifeCycle
![](pic/jetty-lifeCycle.jpg)
```md
受JSR77规范的启发，Jetty的绝大多数的组件（Connector, Handler ,Buffer）都实现了LifeCycle接口。
```
```md
整个 Jetty 的所有组件的生命周期管理是基于观察者模板设计，它和 Tomcat 的管理是类似的。
在这里是 Listener 类，这个类通常对应到观察者模式中常用的 Observer 角色，
当 start、fail 或 stop 等事件触发时，这些 Listener 将会被调用。
```
```md
核心接口
	org.mortbay.component.LifeCycle
	整个 Jetty 的所有组件的生命周期管理是基于观察者模板设计，它和 Tomcat 的管理是类似的
```