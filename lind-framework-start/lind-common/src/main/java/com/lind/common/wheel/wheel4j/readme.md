# 1. 基本概念

> 时间轮 是一种 实现延迟功能（定时器） 的 巧妙算法。如果一个系统存在大量的任务调度，时间轮可以高效的利用线程资源来进行批量化调度。把大批量的调度任务全部都绑定时间轮上，通过时间轮进行所有任务的管理，触发以及运行。能够高效地管理各种延时任务，周期任务，通知任务等。

相比于 JDK 自带的 Timer、DelayQueue + ScheduledThreadPool 来说，时间轮算法是一种非常高效的调度模型。不过，时间轮调度器的时间精度可能不是很高，对于精度要求特别高的调度任务可能不太适合，因为时间轮算法的精度取决于时间段“指针”单元的最小粒度大小。比如时间轮的格子是一秒跳一次，那么调度精度小于一秒的任务就无法被时间轮所调度。

# 2. 原理解析

时间轮（TimingWheel）算法应用范围非常广泛，各种操作系统的定时任务调度都有用到，我们熟悉的 Linux Crontab，以及 Java 开发过程中常用的 Dubbo、Netty、Akka、Quartz、ZooKeeper 、Kafka 等，几乎所有和 时间任务调度 都采用了时间轮的思想。

> 定时的任务调度 分两种：
> 
> * 相对时间：一段时间后执行
> * 绝对时间：指定某个确定的时间执行
> 
> 当然，这两者之间是可以相互转换的，例如当前时间是12点，定时在5分钟之后执行，其实绝对时间就是：12:05；定时在12:05执行，相对时间就是5分钟之后执行。

时间轮（TimingWheel）是一个 存储定时任务的环形队列，底层采用数组实现，数组中的每个元素可以存放一个定时任务列表（TimerTaskList）。TimerTaskList 是一个环形的双向链表，链表中的每一项表示的都是定时任务项（TimerTaskEntry），其中封装了真正的定时任务 TimerTask。

![](https://images.cnblogs.com/cnblogs_com/lori/2399824/o_240524033524_wheel.webp)

## 2.1. 基本模型构成

* tickMs（基本时间跨度）：时间轮由多个时间格组成，每个时间格代表当前时间轮的基本时间跨度（tickMs）。
* wheelSize（时间单位个数）：时间轮的时间格个数是固定的，可用（wheelSize）来表示，那么整个时间轮的总体时间跨度（interval）可以通过公式 tickMs × wheelSize计算得出。
* currentTime（当前所处时间）：时间轮还有一个表盘指针（currentTime），用来表示时间轮当前所处的时间，currentTime 是 tickMs 的整数倍。currentTime 可以将整个时间轮划分为到期部分和未到期部分，currentTime 当前指向的时间格也属于到期部分，表示刚好到期，需要处理此时间格所对应的 TimerTaskList 的所有任务。

## 2.2. 处理流程分析

1. 若时间轮的 tickMs=1ms，wheelSize=20，那么可以计算得出 interval 为 20ms；
2. 初始情况下表盘指针 currentTime 指向时间格 0，此时有一个定时为 2ms 的任务插入进来会存放到时间格为 2 的 TimerTaskList 中；
3. 随着时间的不断推移，指针 currentTime 不断向前推进，过了 2ms 之后，当到达时间格 2 时，就需要将时间格 2 所对应的 TimeTaskList 中的任务做相应的到期操作；
4. 此时若又有一个定时为 8ms 的任务插入进来，则会存放到时间格 10 中，currentTime 再过 8ms 后会指向时间格 10。 当到达时间格 2 时，如果同时有一个定时为 19ms 的任务插入进来怎么办？
5. 新来的 TimerTaskEntry 会复用原来的 TimerTaskList，所以它会插入到原本已经到期的时间格 1 中（一个显而易见的环形结构）。

总之，整个时间轮的总体跨度是不变的，随着指针 currentTime 的不断推进，当前时间轮所能处理的时间段也在不断后移，总体时间范围在 currentTime 和 currentTime + interval 之间。

## 2.3. 问题再次深入
如果此时有个定时为 350ms 的任务该如何处理？直接扩充 wheelSize 的大小么？
很多业务场景不乏几万甚至几十万毫秒的定时任务，这个 wheelSize 的扩充没有底线，就算将所有的定时任务的到期时间都设定一个上限，比如 100 万毫秒，那么这个 wheelSize 为 100 万毫秒的时间轮不仅占用很大的内存空间，而且效率也会拉低。所以 层级时间轮（类似十进制/二进制的计数方式）的概念应运而生，当任务的到期时间超过了当前时间轮所表示的时间范围时，就会尝试添加到上层时间轮中。

![](https://images.cnblogs.com/cnblogs_com/lori/2399824/o_240524033759_wheel2.webp)

复用之前的案例，第一层的时间轮 tickMs=1ms, wheelSize=20, interval=20ms。第二层的时间轮的 tickMs 为第一层时间轮的 interval，即为 20ms。每一层时间轮的 wheelSize 是固定的，都是 20，那么第二层的时间轮的总体时间跨度 interval 为 400ms。以此类推，这个 400ms 也是第三层的 tickMs 的大小，第三层的时间轮的总体时间跨度为 8000ms。

## 2.4. 流程再次分析

1. 当到达时间格 2 时，如果此时有个定时为 350ms 的任务，显然第一层时间轮不能满足条件，所以就 时间轮升级 到第二层时间轮中，最终被插入到第二层时间轮中时间格 17 所对应的 TimerTaskList 中；
2. 如果此时又有一个定时为 450ms 的任务，那么显然第二层时间轮也无法满足条件，所以又升级到第三层时间轮中，最终被插入到第三层时间轮中时间格 1 的 TimerTaskList 中；
3. 注意到在到期时间在 [400ms,800ms) 区间的多个任务（比如446ms、455ms以及473ms的定时任务）都会被放入到第三层时间轮的时间格 1 中，时间格 1 对应的TimerTaskList的超时时间为400ms；
4. 随着时间的流逝，当次 TimerTaskList 到期之时，原本定时为 450ms 的任务还剩下 50ms 的时间，还不能执行这个任务的到期操作。这里就有一个 时间轮降级 的操作，会将这个剩余时间为 50ms 的定时任务重新提交到层级时间轮中，此时第一层时间轮的总体时间跨度不够，而第二层足够，所以该任务被放到第二层时间轮到期时间为 [40ms,60ms) 的时间格中；
5. 再经历了 40ms 之后，此时这个任务又被“察觉”到，不过还剩余 10ms，还是不能立即执行到期操作。所以还要再有一次时间轮的降级，此任务被添加到第一层时间轮到期时间为 [10ms,11ms) 的时间格中，之后再经历 10ms 后，此任务真正到期，最终执行相应的到期操作。

## 2.5. 具体实现优化
* 除了第一层时间轮的起始时间(startMs)，其余高层时间轮的起始时间(startMs)都设置为创建此层时间轮时前面第一轮的 currentTime。
每一层的 currentTime 都必须是 tickMs 的整数倍，如果不满足则会将 currentTime 修剪为 tickMs 的整数倍，以此与时间轮中的时间格的到期时间范围对应起来。修剪方法为：currentTime = startMs - (startMs % tickMs)。currentTime 会随着时间推移而推荐，但是不会改变为 tickMs 的整数倍的既定事实。若某一时刻的时间为 timeMs，那么此时时间轮的 currentTime = timeMs - (timeMs % tickMs)，时间每推进一次，每个层级的时间轮的 currentTime 都会依据此公式推进。
* 定时器只需持有TimingWheel的第一层时间轮的引用，并不会直接持有其他高层的时间轮，但是每一层时间轮都会有一个引用（overflowWheel）指向更高一层的应用，以此层级调用而可以实现定时器间接持有各个层级时间轮的引用。
* 借助了JDK中的DelayQueue来协助推进时间轮。具体做法是对于每个使用到的TimerTaskList都会加入到DelayQueue中，DelayQueue会根据TimerTaskList对应的超时时间expiration来排序，最短expiration的TimerTaskList会被排在DelayQueue的队头。

试想一下，DelayQueue中的第一个超时任务列表的expiration为200ms，第二个超时任务为840ms，这里获取DelayQueue的队头只需要O(1)的时间复杂度。如果采用每秒定时推进，那么获取到第一个超时的任务列表时执行的200次推进中有199次属于“空推进”，而获取到第二个超时任务时有需要执行639次“空推进”，这样会无故空耗机器的性能资源。
这里采用DelayQueue来辅助以少量空间换时间，从而做到了“精准推进”。获取到DelayQueue中的超时的任务列表TimerTaskList之后，既可以根据TimerTaskList的expiration来推进时间轮的时间，也可以就获取到的TimerTaskList执行相应的操作。

# 3. 代码模拟
* 定时任务部分
 * 定时任务项（TimerTaskEntry）
 * 定时任务列表（TimerTaskList）
 * 真正的定时任务（TimerTask）：继承实现 run()
* 时间轮主体部分
 * 时间轮（TimingWheel）
* 定时器实现部分
 * 定时器接口（Timer）
 * 定时器实现（TimerLauncher）

TimerTaskEntry
```java
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class TimerTaskEntry implements Comparable<TimerTaskEntry> {

    volatile TimerTaskList timedTaskList;
    TimerTaskEntry next;
    TimerTaskEntry prev;
    private TimerTask timerTask;
    private long expireMs;

    public TimerTaskEntry(TimerTask timedTask, long expireMs) {
        this.timerTask = timedTask;
        this.expireMs = expireMs;
        this.next = null;
        this.prev = null;
    }

    void remove() {
        TimerTaskList currentList = timedTaskList;
        while (currentList != null) {
            currentList.remove(this);
            currentList = timedTaskList;
        }
    }

    @Override
    public int compareTo(TimerTaskEntry o) {
        return ((int) (this.expireMs - o.expireMs));
    }

}
```
TimerTaskList
```java
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Data
@Slf4j
public class TimerTaskList implements Delayed {

    /**
     * TimerTaskList 环形链表使用一个虚拟根节点root
     */
    private TimerTaskEntry root = new TimerTaskEntry(null, -1);
    /**
     * bucket的过期时间
     */
    private AtomicLong expiration = new AtomicLong(-1L);

    {
        root.next = root;
        root.prev = root;
    }

    public long getExpiration() {
        return expiration.get();
    }

    /**
     * 设置bucket的过期时间,设置成功返回true
     *
     * @param expirationMs
     * @return
     */
    boolean setExpiration(long expirationMs) {
        return expiration.getAndSet(expirationMs) != expirationMs;
    }

    public boolean addTask(TimerTaskEntry entry) {
        boolean done = false;
        while (!done) {
            // 如果TimerTaskEntry已经在别的list中就先移除,同步代码块外面移除,避免死锁,一直到成功为止
            entry.remove();
            synchronized (this) {
                if (entry.timedTaskList == null) {
                    // 加到链表的末尾
                    entry.timedTaskList = this;
                    TimerTaskEntry tail = root.prev;
                    entry.prev = tail;
                    entry.next = root;
                    tail.next = entry;
                    root.prev = entry;
                    done = true;
                }
            }
        }
        return true;
    }

    /**
     * 从 TimedTaskList 移除指定的 timerTaskEntry
     *
     * @param entry
     */
    public void remove(TimerTaskEntry entry) {
        synchronized (this) {
            if (entry.getTimedTaskList().equals(this)) {
                entry.next.prev = entry.prev;
                entry.prev.next = entry.next;
                entry.next = null;
                entry.prev = null;
                entry.timedTaskList = null;
            }
        }
    }

    /**
     * 移除所有
     */
    public synchronized void clear(Consumer<TimerTaskEntry> entry) {
        TimerTaskEntry head = root.next;
        while (!head.equals(root)) {
            remove(head);
            entry.accept(head);
            head = root.next;
        }
        expiration.set(-1L);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return Math.max(0, unit.convert(expiration.get() - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
    }

    @Override
    public int compareTo(Delayed o) {
        if (o instanceof TimerTaskList) {
            return Long.compare(expiration.get(), ((TimerTaskList) o).expiration.get());
        }
        return 0;
    }

}

```
TimerTask
```java
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class TimerTask implements Runnable {

    /**
     * 延时时间
     */
    private long delayMs;
    /**
     * 任务所在的entry
     */
    private TimerTaskEntry timerTaskEntry;

    private String desc;

    public TimerTask(String desc, long delayMs) {
        this.desc = desc;
        this.delayMs = delayMs;
        this.timerTaskEntry = null;
    }

    public TimerTaskEntry getTimerTaskEntry() {
        return timerTaskEntry;
    }

    public synchronized void setTimerTaskEntry(TimerTaskEntry entry) {
        // 如果这个TimerTask已经被一个已存在的TimerTaskEntry持有,先移除一个
        if (timerTaskEntry != null && timerTaskEntry != entry) {
            timerTaskEntry.remove();
        }
        timerTaskEntry = entry;
    }

    @Override
    public void run() {
        log.info("============={}任务执行", desc);
    }

}

```
TimingWheel
```java
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;

@Data
@Slf4j
public class TimeWheel {

    /**
     * 基本时间跨度
     */
    private long tickMs;
    /**
     * 时间单位个数
     */
    private int wheelSize;
    /**
     * 总体时间跨度
     */
    private long interval;
    /**
     * 当前所处时间
     */
    private long currentTime;
    /**
     * 定时任务列表
     */
    private TimerTaskList[] buckets;
    /**
     * 上层时间轮
     */
    private volatile TimeWheel overflowWheel;
    /**
     * 一个Timer只有一个DelayQueue,协助推进时间轮
     */
    private DelayQueue<TimerTaskList> delayQueue;


    public TimeWheel(long tickMs, int wheelSize, long currentTime, DelayQueue<TimerTaskList> delayQueue) {
        this.tickMs = tickMs;
        this.wheelSize = wheelSize;
        this.interval = tickMs * wheelSize;
        this.currentTime = currentTime;
        this.buckets = new TimerTaskList[wheelSize];
        this.currentTime = currentTime - (currentTime % tickMs);
        this.delayQueue = delayQueue;
        for (int i = 0; i < wheelSize; i++) {
            buckets[i] = new TimerTaskList();
        }
    }

    public boolean add(TimerTaskEntry entry) {
        long expiration = entry.getExpireMs();
        if (expiration < tickMs + currentTime) {
            // 定时任务到期
            return false;
        } else if (expiration < currentTime + interval) {
            // 扔进当前时间轮的某个槽里,只有时间大于某个槽,才会放进去
            long virtualId = (expiration / tickMs);
            int index = (int) (virtualId % wheelSize);
            TimerTaskList bucket = buckets[index];
            bucket.addTask(entry);
            // 设置bucket 过期时间
            if (bucket.setExpiration(virtualId * tickMs)) {
                // 设好过期时间的bucket需要入队
                delayQueue.offer(bucket);
                return true;
            }
        } else {
            // 当前轮不能满足,需要扔到上一轮
            TimeWheel timeWheel = getOverflowWheel();
            return timeWheel.add(entry);
        }
        return false;
    }

    private TimeWheel getOverflowWheel() {
        if (overflowWheel == null) {
            synchronized (this) {
                if (overflowWheel == null) {
                    overflowWheel = new TimeWheel(interval, wheelSize, currentTime, delayQueue);
                }
            }
        }
        return overflowWheel;
    }

    /**
     * 推进指针
     *
     * @param timestamp
     */
    public void advanceLock(long timestamp) {
        if (timestamp > currentTime + tickMs) {
            currentTime = timestamp - (timestamp % tickMs);
            if (overflowWheel != null) {
                this.getOverflowWheel().advanceLock(timestamp);
            }
        }
    }

}
```
Timer
```java
public interface Timer {

    /**
     * 添加一个新任务
     *
     * @param timerTask
     */
    void add(TimerTask timerTask);

    /**
     * 推动指针
     *
     * @param timeout
     */
    void advanceClock(long timeout);

    /**
     * 等待执行的任务
     *
     * @return
     */
    int size();

    /**
     * 关闭服务,剩下的无法被执行
     */
    void shutdown();

}

```
TimerLauncher
```java
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class TimerLauncher implements Timer {

    /**
     * 底层时间轮
     */
    private TimeWheel timeWheel;
    /**
     * 一个Timer只有一个延时队列
     */
    private DelayQueue<TimerTaskList> delayQueue = new DelayQueue<>();
    /**
     * 过期任务执行线程
     */
    private ExecutorService workerThreadPool;
    /**
     * 轮询delayQueue获取过期任务线程
     */
    private ExecutorService bossThreadPool;


    public TimerLauncher() {
        this.timeWheel = new TimeWheel(1, 20, System.currentTimeMillis(), delayQueue);
        this.workerThreadPool = Executors.newFixedThreadPool(100);
        this.bossThreadPool = Executors.newFixedThreadPool(1);
        // 20ms推动一次时间轮运转
        this.bossThreadPool.submit(() -> {
            while (true) {
                this.advanceClock(20);
            }
        });
    }


    public void addTimerTaskEntry(TimerTaskEntry entry) {
        if (!timeWheel.add(entry)) {
            // 任务已到期
            TimerTask timerTask = entry.getTimerTask();
            log.info("=====任务:{} 已到期,准备执行============", timerTask.getDesc());
            workerThreadPool.submit(timerTask);
        }
    }

    @Override
    public void add(TimerTask timerTask) {
        log.info("=======添加任务开始====task:{}", timerTask.getDesc());
        TimerTaskEntry entry = new TimerTaskEntry(timerTask, timerTask.getDelayMs() + System.currentTimeMillis());
        timerTask.setTimerTaskEntry(entry);
        addTimerTaskEntry(entry);
    }

    /**
     * 推动指针运转获取过期任务
     *
     * @param timeout 时间间隔
     * @return
     */
    @Override
    public synchronized void advanceClock(long timeout) {
        try {
            TimerTaskList bucket = delayQueue.poll(timeout, TimeUnit.MILLISECONDS);
            if (bucket != null) {
                // 推进时间
                timeWheel.advanceLock(bucket.getExpiration());
                // 执行过期任务(包含降级)
                bucket.clear(this::addTimerTaskEntry);
            }
        } catch (InterruptedException e) {
            log.error("advanceClock error");
        }
    }

    @Override
    public int size() {
        return 10;
    }

    @Override
    public void shutdown() {
        this.bossThreadPool.shutdown();
        this.workerThreadPool.shutdown();
        this.timeWheel = null;
    }

}
```
