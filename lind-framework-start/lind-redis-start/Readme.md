# Lettuce 和 Jedis 
Lettuce 和 Jedis 的定位都是Redis的client，所以他们当然可以直接连接redis server。Jedis在实现上是直接连接的redis server，如果在多线程环境下是非线程安全的，这个时候只有使用连接池，为每个Jedis实例增加物理连接Lettuce的连接是基于Netty的，连接实例（StatefulRedisConnection）可以在多个线程间并发访问，应为StatefulRedisConnection是线程安全的，所以一个连接实例（StatefulRedisConnection）就可以满足多线程环境下的并发访问，当然这个也是可伸缩的设计，一个连接实例不够的情况也可以按需增加连接实例。

# 序列化
## Jackson2Json
```json
[
  "com.lind.redis.User",
  {
    "id": 1,
    "name": "zzl",
    "createTime": "2024-03-15T09:18:33.39"
  }
]
```
## fastjson&默认
```json
{
    "@type": "com.ruoyi.common.redis.SerializerTest$User",
    "createTime": "2024-03-15 09:13:22.526673400",
    "id": 1,
    "name": "zzl"
}
```
# lock组件
实现对某个资源或者代码片断进行锁的添加，这种锁是跨进度，跨服务的。
# 依赖引用
```
<dependency>
 <groupId>com.pkulaw</groupId>
 <artifactId>pkulaw-lock-start</artifactId>
 <version>1.0.0</version>
</dependency>
```
# 配置
```
pkulaw.redis.lock.enable: true #开启
pkulaw.redis.lock.registryKey: system-lock #锁前缀
pkulaw.redis.lock.interrupt: false #没有获到锁是否立即中断,true表示中断,false表示阻塞可重入锁
pkulaw.redis.lock.manualLockKey：user-lock #手动锁键
```
# 使用
```
@Autowire
DistributedLockTemplate distributedLockTemplate;
private void lock5Second() {
    distributedLockTemplate.execute("订单流水号", 2, TimeUnit.SECONDS, new Callback() {
        @Override
        public Object onGetLock() throws InterruptedException {
            // 获得锁后要做的事
            log.info("{} 拿到锁，需要5秒钟，这时有请求打入应该被阻塞或者拒绝", Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(5L);
            return null;
        }

        @Override
        public Object onTimeout() throws InterruptedException {
            // 获取到锁（获取锁超时）后要做的事
            log.info("{} 没拿到锁", Thread.currentThread().getName());
            return null;
        }
    });
}
```

# limit组件
实现服务整体的限流，或者某个接口的限流。
# 依赖引用
```
<dependency>
 <groupId>com.pkulaw</groupId>
 <artifactId>pkulaw-limit-start</artifactId>
 <version>1.0.0</version>
</dependency>
```
# 配置
```
pkulaw.ratelimit.enable: true #开启
pkulaw.ratelimit.limit: 100 #限制个数
pkulaw.ratelimit.timeout: 1000 #超时毫秒
```
# 使用
```
@RateLimiter
@GetMapping("limit")
public CommonResult limit() {
    System.out.println(new PageDto());
    CommonResult commonResult = CommonResult.ok("ok");
    return commonResult;
}
```
