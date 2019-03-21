# OkHttp
```md
OkHttp 库的设计和实现的首要目标是高效。

OkHttp 提供了对最新的 HTTP 协议版本 HTTP/2 和 SPDY 的支持。
这使得对同一个主机发出的所有请求都可以共享相同的套接字连接。
如果HTTP/2 和 SPDY 不可用，OkHttp 会使用连接池来复用连接以提高效率。

OkHttp 提供了对 GZIP 的默认支持来降低传输内容的大小。

OkHttp 也提供了对 HTTP 响应的缓存机制，可以避免不必要的网络请求。
当网络出现问题时，OkHttp 会自动重试一个主机的多个 IP 地址。
```


## References
* [OkHttp使用完全教程](https://www.jianshu.com/p/ca8a982a116b)
* [OkHttp：Java 平台上的新一代 HTTP 客户端](https://www.ibm.com/developerworks/cn/java/j-lo-okhttp/index.html)