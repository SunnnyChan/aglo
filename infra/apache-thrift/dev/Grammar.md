#
```md
Types
	Thrift类型系统包括预定义基本类型，用户自定义结构体，容器类型，异常和服务定义
	基本类型
		bool：布尔类型(true or value)，占一个字节
		byte：有符号字节
		i16:16位有符号整型
		i32:32位有符号整型
		i64:64位有符号整型
		double：64位浮点数
		string：未知编码或者二进制的字符串
		注意，thrift不支持无符号整型，因为很多目标语言不存在无符号整型（如java）。
	容器类型
		Thrift容器与类型密切相关，它与当前流行编程语言提供的容器类型相对应，采用java泛型风格表示的。
		List<t1>：一系列t1类型的元素组成的有序表，元素可以重复
		Set<t1>：一系列t1类型的元素组成的无序表，元素唯一
		Map<t1,t2>：key/value对（key的类型是t1且key唯一，value类型是t2）。
		容器中的元素类型可以是除了service以外的任何合法thrift类型（包括结构体和异常）
	结构体和异常
		Thrift结构体在概念上同C语言结构体类型—-一种将相关属性聚集（封装）在一起的方式。
			在面向对象语言中，thrift结构体被转换成类。
		异常在语法和功能上类似于结构体，只不过异常使用关键字exception而不是struct关键字声明。
			但它在语义上不同于结构体—当定义一个RPC服务时，开发者可能需要声明一个远程方法抛出一个异常。
	服务
		服务的定义方法在语法上等同于面向对象语言中定义接口。
		Thrift编译器会产生实现这些接口的client和server桩。
	类型定义
		Thrift支持C/C++风格的typedef:
			typedef i32 MyInteger   \\a
 
typedef Tweet ReTweet  \\b
			a.  末尾没有逗号
			b.   struct可以使用typedef
枚举类型
	可以像C/C++那样定义枚举类型
	a.  编译器默认从0开始赋值
	b.  可以赋予某个常量某个整数
	c.  允许常量是十六进制整数
	d.  末尾没有逗号
	e.  给常量赋缺省值时，使用常量的全称
	注意，不同于protocol buffer，thrift不支持枚举类嵌套，枚举常量必须是32位的正整数
注释
	hrfit支持shell注释风格，C/C++语言中单行或者多行注释风格
命名空间
	hrift中的命名空间同C++中的namespace和java中的package类似
		它们均提供了一种组织（隔离）代码的方式
	因为每种语言均有自己的命名空间定义方式（如python中有module）
		hrift允许开发者针对特定语言定义namespace
		namespace cpp com.example.project  // a
		namespace java com.example.project // b
		a．  转化成namespace com { namespace example { namespace project {
		b．  转换成package com.example.project
文件包含
	Thrift允许thrift文件包含，用户需要使用thrift文件名作为前缀访问被包含的对象
	include "tweet.thrift"           // a
 
...
 
struct TweetSearchResult {
 
1: list<tweet.Tweet> tweets; // b
 
}

		a．  thrift文件名要用双引号包含，末尾没有逗号或者分号
		b．  注意tweet前缀
常量
	Thrift允许用户定义常量，复杂的类型和结构体可使用JSON形式表示。
	const i32 INT_CONST = 1234;    // a
	const map<string,string> MAP_CONST = {"hello": "world", "goodnight": "moon"}
	a．  分号是可选的，可有可无；支持十六进制赋值。
定义结构体
	结构体由一系列域组成，每个域有唯一整数标识符，类型，名字和可选的缺省参数组成。
	a.  每个域有一个唯一的，正整数标识符
	b.  每个域可以标识为required或者optional（也可以不注明）
	c.  结构体可以包含其他结构体
	d.  域可以有缺省值
	e.  一个thrift中可定义多个结构体，并存在引用关系
	规范的struct定义中的每个域均会使用required或者optional关键字进行标识。
		如果required标识的域没有赋值，thrift将给予提示。
		如果optional标识的域没有赋值，该域将不会被序列化传输。
		如果某个optional标识域有缺省值而用户没有重新赋值，则该域的值一直为缺省值。
	与service不同，结构体不支持继承，即，一个结构体不能继承另一个结构体。
定义服务
	在流行的序列化/反序列化框架（如protocol buffer）中，thrift是少有的提供多语言间RPC服务的框架。
	Thrift编译器会根据选择的目标语言为server产生服务接口代码，为client产生桩代码。
	a． 函数定义可以使用逗号或者分号标识结束
	b． 参数可以是基本类型或者结构体，参数是只读的（const），不可以作为返回值！！！
	c． 返回值可以是基本类型或者结构体
	d． 返回值可以是void
	注意，函数中参数列表的定义方式与struct完全一样
	Service支持继承，一个service可使用extends关键字继承另一个service
```