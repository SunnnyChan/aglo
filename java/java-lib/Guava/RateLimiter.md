# RateLimiter

```md
RateLimiter使用的是一种叫令牌桶的流控算法，RateLimiter会按照一定的频率往桶里扔令牌，线程拿到令牌才能执行，
比如你希望自己的应用程序QPS不要超过1000，那么RateLimiter设置1000的速率后，就会每秒往桶里扔1000个令牌。
```
```md
RateLimiter经常用于限制对一些物理资源或者逻辑资源的访问速率。
与Semaphore 相比，Semaphore 限制了并发访问的数量而不是使用速率。
```


