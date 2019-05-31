# Spring Boot 注解

* @Autowired
```md
表示被修饰的类需要注入对象，Spring会扫描所有被 @Autowired 标注的类，然后根据 类型 在容器中找到匹配的类注入。
```

* [@Bean](@Bean.md)

* [@Configuration](@Configuration.md)

* [@Component - 泛指组件，告知 Spring 要为这个类创建 bean](@Component.md)
* @Controller - 用于标注控制层组件
* @Repository - 用于标注数据访问组件，即DAO组件
* @Service - 用于标注业务层组件 


* @Import 
* @Profile