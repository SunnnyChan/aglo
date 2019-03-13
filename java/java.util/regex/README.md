# [java.util.regex](https://docs.oracle.com/javase/8/docs/api/index.html?java/util/regex/package-summary.html)
* 类分层结构
```md
java.lang.Object
  java.util.regex.Matcher (implements java.util.regex.MatchResult)
  java.util.regex.Pattern (implements java.io.Serializable)
  java.lang.Throwable (implements java.io.Serializable)
    java.lang.Exception
      java.lang.RuntimeException
        java.lang.IllegalArgumentException
          java.util.regex.PatternSyntaxException
```
* Pattern
```md
Pattern 对象是一个正则表达式的编译表示。
Pattern 类没有公共构造方法。
要创建一个 Pattern 对象，你必须首先调用其公共静态编译方法，它返回一个 Pattern 对象。
该方法接受一个正则表达式作为它的第一个参数。
```
* Matcher
```md
Matcher 对象是对输入字符串进行解释和匹配操作的引擎。
与Pattern 类一样，Matcher 也没有公共构造方法。
你需要调用 Pattern 对象的 matcher 方法来获得一个 Matcher 对象。
```
* PatternSyntaxException
```md
PatternSyntaxException 是一个非强制异常类，它表示一个正则表达式模式中的语法错误。
```

* 捕获组


## Resources
* [Java 正则表达式](http://www.runoob.com/java/java-regular-expressions.html)