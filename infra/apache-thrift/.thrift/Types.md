# [Types](http://thrift.apache.org/docs/types)
```md
	Thrift类型系统包括预定义基本类型，用户自定义结构体，容器类型，异常和服务定义
```
* 基本类型
```md
bool：布尔类型(true or value)，占一个字节
byte：有符号字节
i16:16位有符号整型
i32:32位有符号整型
i64:64位有符号整型
double：64位浮点数
string：未知编码或者二进制的字符串

注意，thrift不支持无符号整型，因为很多目标语言不存在无符号整型（如java）。
```
* Special Types
```md
binary：一系列未编码的字节
```
* Structs
```md
Thrift结构定义了一个公共对象 - 它们本质上等同于OOP语言中的类，但没有继承。
在面向对象语言中，thrift结构体被转换成类。
Thrift结构体在概念上同C语言结构体类型—-一种将相关属性聚集（封装）在一起的方式。

struct有一组强类型字段，每个字段都有唯一的名称标识符。
字段可以具有Thrift IDL中描述的各种注释（数字字段ID，可选的默认值等）。
```
* union 类型
```md
和 C/C++ 中的 union 类似。
```
* 容器类型
```md
Thrift容器与类型密切相关，它与当前流行编程语言提供的容器类型相对应，采用java泛型风格表示的。
List<t1>：一系列t1类型的元素组成的有序表，元素可以重复
Set<t1>：一系列t1类型的元素组成的无序表，元素唯一
Map<t1,t2>：key/value对（key的类型是t1且key唯一，value类型是t2）。

容器中的元素类型可以是除了service以外的任何合法thrift类型（包括结构体和异常）
```
* Exceptions
```md
异常在功能上等同于结构，除了它们在每种目标编程语言中适当地继承本地异常基类，
以便与任何给定语言中的本机异常处理无缝集成。
```
* Services
```md
使用Thrift类型定义服务。服务的定义在语义上等同于在面向对象的编程中定义接口（或纯虚拟抽象类）。
服务由一组命名函数组成，每个函数都有一个参数列表和一个返回类型。

service 类型可以被继承, 例如:
service PeopleDirectory {
   oneway void log(1: string message),
   void reloadDatabase()
}
service EmployeeDirectory extends PeopleDirectory {
   Employee findEmployee(1: i32employee_id) throws (1: MyError error),
   bool createEmployee(1: Employee new_employee)
}

请注意，除了所有其他已定义的Thrift类型之外，void是函数返回的有效类型。
```
> * oneway
```md
关键字通常用于修饰无返回值(void)的函数, 但是它和直接的无返回值的函数还是有区别的。

例如
上面的 log 函数和 reloadDatabase 函数, 当客户端通过 thrift 进行远程调用服务端的 log 函数时, 
不需要等待服务端的 log 函数执行结束就可以直接返回; 

但是当客户端调用 reloadDatabase 方法时, 虽然这个方法也是无返回值的, 
但客户端必须要阻塞等待, 直到服务端通知客户端此调用已结束后, 客户端的远程调用才可以返回.
```
* 枚举类型
```md
和 Java 中的 enum 类型一样, 例如:

enum Fruit {
    Apple,
    Banana,
}
```
