# 滑动窗口限流

滑动窗口限流是一种常用的限流算法，通过维护一个固定大小的窗口，在单位时间内允许通过的请求次数不超过设定的阈值。具体来说，滑动窗口限流算法通常包括以下几个步骤：

1. 初始化：设置窗口大小、请求次数阈值和时间间隔。
2. 维护窗口：将请求按照时间顺序放入窗口中，并保持窗口内请求数量不超过阈值。
3. 检查通过：每当有新的请求到达时，检查窗口内请求的总数是否超过阈值，如果未超过则允许通过，同时移除窗口最老的请求。
4. 更新窗口：随着时间的推移，更新窗口内的请求情况，确保窗口内的请求符合限流条件。

滑动窗口限流算法可以有效控制系统的请求流量，避免系统被大量请求压垮。同时，由于其简单高效的特点，被广泛应用于接口限流、流量控制等场景中。需要注意的是，滑动窗口限流算法对于突发请求并不能完全解决问题，因此在实际应用中可能需要结合其他策略进行综合考虑。

## 基于redis-zset实现的滑动窗口算法流程
![](https://images.cnblogs.com/cnblogs_com/lori/2369799/o_240429091411_limit.png)

## 核心代码
```java
/**
 * 滑动窗口限流. 需要注意的是，我们要定期清楚过期的key，否则会导致内存泄漏，可以使用ZREMRANGEBYSCORE方法实现.
 * @param key 限流的key
 * @param timeWindow 单位时间，秒
 * @param limit 窗口大小，单位时间最大容许的令牌数
 * @param runnable 成功后的回调方法
 */
public void slidingWindow(String key, int timeWindow, int limit, Runnable runnable) {
    Long currentTime = System.currentTimeMillis();
    if (redisTemplate.hasKey(key)) {
        Long intervalTime = timeWindow * 1000L;
        Long from = currentTime - intervalTime;
        Integer count = redisTemplate.opsForZSet().rangeByScore(key, from, currentTime).size();
        if (count != null && count >= limit) {
            throw new RedisLimitException("每" + timeWindow + "秒最多只能访问" + limit + "次.");
        }
        log.info("from key:{}~{},current count:{}", from, currentTime, count);
    }
    redisTemplate.opsForZSet().add(key, UUID.randomUUID().toString(), currentTime);
    Optional.ofNullable(runnable).ifPresent(o -> o.run());
}
```
上面实现了一个基于时间戳为主要窗口依据的滑动窗口限流逻辑，由于zset的数据量会随着时间的流失而变大，所以我们需要定期再根据score来清理它。
```java
/**
 * 清期昨天的zset元素,这块应该写个任务调度，每天执行一次，清量需要的zset元素.
 * @param key
 */
public void delByYesterday(String key) {
    Instant currentInstant = Instant.now();
    Instant oneDayAgoInstant = currentInstant.minusSeconds(86400);
    long oneDayAgoTimeMillis = oneDayAgoInstant.toEpochMilli();
    redisTemplate.opsForZSet().removeRangeByScore(key, 0, oneDayAgoTimeMillis);

}
```
上面代码逻辑，事实上，我们可以通过其它语言去实现，比较通过go可以实现相关的逻辑，从新可以在MSE网关上实现限流功能。


