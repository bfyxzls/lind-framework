package com.lind.common.thread;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.*;

/**
 * 线程池生产者. 1.首先检测线程池运行状态，如果不是 RUNNING，则直接拒绝，线程池要保证在 RUNNING 的状态下执行任务。 2.如果 workerCount <
 * corePoolSize，则创建并启动一个线程来执行新提交的任务。 3.如果 workerCount >=
 * corePoolSize，且线程池内的阻塞队列未满，则将任务添加到该阻塞队列中。 4.如果 workerCount >= corePoolSize
 * 且线程池内的阻塞队列已满,并且workerCount < maximumPoolSize，，则创建并启动一个线程来执行新提交的任务。 5.如果 workerCount >=
 * maximumPoolSize，并且线程池内的阻塞队列已满，则根据拒绝策略来处理该任务 , 默认的处理方式是直接抛异常。 线程花瓶
 * ---------------------------------【maximumPoolSize最大线程数】 -----------------------------
 * --------------------- --------------- --------【BlockingQueue阻塞队列】 -------- --------
 * -------- -------- --------------【corePoolSize核心线程数】 -------------- --------------
 * -------------- --------------
 */
public class ThreadPoolBuilder {

	/**
	 * 初始池大小
	 */
	private int corePoolSize;

	/**
	 * 最大池大小（允许同时执行的最大线程数）
	 */
	private int maxPoolSize = Integer.MAX_VALUE;

	/**
	 * 线程存活时间，即当池中线程多于初始大小时，多出的线程保留的时长
	 */
	private long keepAliveTime = TimeUnit.SECONDS.toNanos(60);

	/**
	 * 队列，用于存在未执行的线程
	 */
	private BlockingQueue<Runnable> workQueue;

	/**
	 * 线程工厂，用于自定义线程创建
	 */
	private ThreadFactory threadFactory;

	/**
	 * 线程名前缀（如果 {@linkplain ThreadFactory}为空，则使用该名称生成对应的线程工厂）
	 * ，会自动在线程名后面加上-%d以标识线程，如threadName = rpc-thread；<br/>
	 * 那么创建出来的线程名就类似rpc-thread-1，rpc-thread-2等）
	 */
	private String threadName;

	/**
	 * 当线程阻塞（block）时的异常处理器，所谓线程阻塞即线程池和等待队列已满，无法处理线程时采取的策略
	 */
	private RejectedExecutionHandler handler;

	/**
	 * 核心线程执行超时后是否回收线程
	 */
	private Boolean allowCoreThreadTimeOut;

	private ThreadPoolBuilder() {
	}

	/**
	 * 创建ThreadPoolBuilder，开始构建
	 * @return {@link ThreadPoolBuilder}
	 */
	public static ThreadPoolBuilder newBuilder() {
		return new ThreadPoolBuilder();
	}

	/**
	 * 构建ThreadPoolExecutor
	 * @param builder {@link ThreadPoolBuilder}
	 * @return {@link ThreadPoolExecutor}
	 */
	private static ThreadPoolExecutor build(ThreadPoolBuilder builder) {
		final int corePoolSize = builder.corePoolSize;
		final int maxPoolSize = builder.maxPoolSize;
		final long keepAliveTime = builder.keepAliveTime;
		final BlockingQueue<Runnable> workQueue;
		// 如果builder.workQueue为空，就执行if代码，如果corePoolSize为0则要使用SynchronousQueue避免无限阻塞
		if (builder.workQueue == null) {
			if (corePoolSize <= 0) {
				workQueue = new SynchronousQueue<Runnable>();
			}
			else {
				workQueue = new LinkedBlockingQueue();
			}
		}
		else {
			workQueue = builder.workQueue;
		}

		String threadName;
		final ThreadFactory threadFactory = (builder.threadFactory != null) ? builder.threadFactory
				: !StringUtils.isEmpty(threadName = builder.threadName)
						? ThreadFactoryBuilder.newBuilder(threadName).build() : Executors.defaultThreadFactory();

		RejectedExecutionHandler handler = builder.handler == null ? new ThreadPoolExecutor.AbortPolicy()
				: builder.handler;

		final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
				TimeUnit.NANOSECONDS, workQueue, threadFactory, handler);
		if (builder.allowCoreThreadTimeOut != null) {
			threadPoolExecutor.allowCoreThreadTimeOut(builder.allowCoreThreadTimeOut);
		}
		return threadPoolExecutor;
	}

	/**
	 * 设置初始池大小，默认0
	 * @param corePoolSize 初始池大小
	 * @return this
	 */
	public ThreadPoolBuilder corePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
		return this;
	}

	/**
	 * 设置最大池大小（允许同时执行的最大线程数）
	 * @param maxPoolSize 最大池大小（允许同时执行的最大线程数）
	 * @return this
	 */
	public ThreadPoolBuilder maxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
		return this;
	}

	/**
	 * 设置线程存活时间，即当池中线程多于初始大小时，多出的线程保留的时长
	 * @param keepAliveTime 线程存活时间
	 * @param unit 单位
	 * @return this
	 */
	public ThreadPoolBuilder keepAliveTime(long keepAliveTime, TimeUnit unit) {
		return keepAliveTime(unit.toNanos(keepAliveTime));
	}

	/**
	 * 设置线程存活时间，即当池中线程多于初始大小时，多出的线程保留的时长，单位纳秒
	 * @param keepAliveTime 线程存活时间，单位纳秒
	 * @return this
	 */
	public ThreadPoolBuilder keepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
		return this;
	}

	/**
	 * 设置队列，用于存在未执行的线程<br>
	 * 可选队列有：
	 *
	 * <pre>
	 * 1. SynchronousQueue    它将任务直接提交给线程而不保持它们。当运行线程小于maxPoolSize时会创建新线程，否则触发异常策略
	 * 2. LinkedBlockingQueue 无界队列，当运行线程大于corePoolSize时始终放入此队列，此时maximumPoolSize无效
	 * 3. ArrayBlockingQueue  有界队列，相对无界队列有利于控制队列大小，队列满时，运行线程小于maxPoolSize时会创建新线程，否则触发异常策略
	 * </pre>
	 * @param workQueue 队列
	 * @return this
	 */
	public ThreadPoolBuilder workQueue(BlockingQueue<Runnable> workQueue) {
		this.workQueue = workQueue;
		return this;
	}

	/**
	 * 使用{@link SynchronousQueue} 做为等待队列（非公平策略）<br>
	 * 它将任务直接提交给线程而不保持它们。当运行线程小于maxPoolSize时会创建新线程，否则触发异常策略
	 * @return this
	 */
	public ThreadPoolBuilder useSynchronousQueue() {
		return useSynchronousQueue(false);
	}

	/**
	 * 使用{@link SynchronousQueue} 做为等待队列<br>
	 * 它将任务直接提交给线程而不保持它们。当运行线程小于maxPoolSize时会创建新线程，否则触发异常策略
	 * @param fair 是否使用公平访问策略
	 * @return this
	 */
	public ThreadPoolBuilder useSynchronousQueue(boolean fair) {
		return workQueue(new SynchronousQueue<Runnable>(fair));
	}

	/**
	 * 设置线程工厂，用于自定义线程创建
	 * @param threadFactory 线程工厂
	 * @return this
	 * @see ThreadFactoryBuilder
	 */
	public ThreadPoolBuilder threadFactory(ThreadFactory threadFactory) {
		this.threadFactory = threadFactory;
		return this;
	}

	/**
	 * 设置线程名
	 * @param threadName 线程名称(ThreadFactory不存在情况下有效)，会自动在线程名后面加上-%d以标识线程，如threadName =
	 * rpc-thread；<br/>
	 * 那么创建出来的线程名就类似rpc-thread-1，rpc-thread-2等）
	 * @return this
	 * @see ThreadFactoryBuilder
	 */
	public ThreadPoolBuilder threadName(String threadName) {
		this.threadName = threadName;
		return this;
	}

	/**
	 * 设置当线程阻塞（block）时的异常处理器，所谓线程阻塞即线程池和等待队列已满，无法处理线程时采取的策略
	 * <p>
	 * @param handler {@link RejectedExecutionHandler}
	 * @return this
	 */
	public ThreadPoolBuilder rejectHandler(RejectedExecutionHandler handler) {
		this.handler = handler;
		return this;
	}

	/**
	 * 设置线程执行超时后是否回收线程
	 * @param allowCoreThreadTimeOut 线程执行超时后是否回收线程
	 * @return this
	 */
	public ThreadPoolBuilder allowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
		this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
		return this;
	}

	/**
	 * 构建ThreadPoolExecutor
	 */
	public ThreadPoolExecutor build() {
		return build(this);
	}

}
