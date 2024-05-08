package com.lind.common.thread;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadStatusTest {

	static void print(Map<String, String> map) {
		map.put("title", "lind");
	}

	/**
	 * 测试结果，有7个线程执行成功，3个线程走拒绝策略，7个成功的就是池子只能容纳maximumPoolSize+ArrayBlockingQueue的线程.
	 * 调整corePoolSize的值，可以加快任务处理，但对rejected没有影响，是否走拒绝策略的依据是maximumPoolSize+ArrayBlockingQueue与你的任务数量的关系
	 * 任务数量大于了maximumPoolSize+ArrayBlockingQueue，就被走拒绝策略.
	 */
	@SneakyThrows
	@Test
	public void threadPoolExecutor() {
		RejectedExecutionHandler handler = new MyIgnorePolicy();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3),
				handler);
		for (int i = 1; i <= 10; i++) {
			MyTask task = new MyTask(String.valueOf(i));
			executor.execute(task);
		}

		System.in.read(); // 阻塞主线程
	}

	@SneakyThrows
	@Test
	public void mainValueThreadModify() {
		ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(4);
		Map<String, String> map = new HashMap<>();
		map.put("one", "1");
		threadPoolExecutor.submit(() -> {
			print(map);
			log.info(Thread.currentThread().getName() + " map:" + map);
		});
		log.info(Thread.currentThread().getName() + " map:" + map);

	}

	/**
	 * 线程A要等待某个条件满足时(list.size()==5)，才执行操作。线程B则向list中添加元素，改变list 的size。
	 * A,B之间如何通信的呢？也就是说，线程A如何知道 list.size() 已经为5了呢？ 这里用到了Object类的 wait() 和 notify() 方法。
	 * 当条件未满足时(list.size() !=5)，线程A调用wait() 放弃CPU，并进入阻塞状态。---不像②while轮询那样占用CPU
	 * 当条件满足时，线程B调用 notify()通知 线程A，所谓通知线程A，就是唤醒线程A，并让它进入可运行状态。 这种方式的一个好处就是CPU的利用率提高了。
	 * 但是也有一些缺点：比如，线程B先执行，一下子添加了5个元素并调用了notify()发送了通知，而此时线程A还执行；当线程A执行并调用wait()时，那它永远就不可能被唤醒了。因为，线程B已经发了通知了，以后不再发通知了。这说明：通知过早，会打乱程序的执行逻辑。
	 * @throws InterruptedException
	 */
	@Test
	public void aAndB() throws InterruptedException {
		try {
			Object lock = new Object();

			ThreadA a = new ThreadA(lock);
			a.start();

			Thread.sleep(50);

			ThreadB b = new ThreadB(lock);
			b.start();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		TimeUnit.SECONDS.sleep(1000L);
	}

	@Test
	public void runnableState() {
		new Thread(() -> {
			System.out.println("current thread state when doing sth : " + Thread.currentThread().getState());// RUNNABLE
		}).start();
	}

	@Test
	public void BlockedState() throws InterruptedException {
		Object lock = new Object();
		// 启动一个线程获取锁，然后假装很忙，再也不放手
		new Thread(() -> {
			synchronized (lock) {
				while (true) {
				}
			}
		}).start();

		Thread threadB = new Thread(() -> {
			synchronized (lock) {
				System.out.println("lock acquired!");
			}
		});
		threadB.start();// 线程开始后，状态变成RUNNABLE
		TimeUnit.SECONDS.sleep(5L);// 让主线程在这暂停5S，此时B线程已经开始执行，尝试去获取锁，当然是获取不到的
		System.out.println(threadB.getState());// BLOCKED
	}

	@Test
	public void timedWaitingState() throws InterruptedException {
		Thread thread = new Thread(() -> {
			try {
				new ValuableResource().doSthTimedWaiting();
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
		thread.start();
		TimeUnit.SECONDS.sleep(2L);

		System.out.println(thread.getState());// TIMED_WAITING
		TimeUnit.SECONDS.sleep(2L);
		System.out.println(thread.getState());// RUNNABLE
	}

	@Test
	public void createThread() throws InterruptedException {
		ThreadPoolExecutor bizThreadPool = new ThreadPoolExecutor(5, 5, 60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(2000), new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						return new Thread(r, "xxl-job, EmbedServer bizThreadPool-" + r.hashCode());
					}
				}, new RejectedExecutionHandler() {
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						throw new RuntimeException("xxl-job, EmbedServer bizThreadPool is EXHAUSTED!");
					}
				});

		for (int i = 0; i < 20; i++) {
			// invoke
			bizThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println("hello");
					try {
						Thread.sleep(5000);
					}
					catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
		Thread.sleep(50000);
	}

	public static class MyList {

		private static List<String> list = new ArrayList<String>();

		public static void add() {
			list.add("anyString");
		}

		public static int size() {
			return list.size();
		}

	}

	public static class MyIgnorePolicy implements RejectedExecutionHandler {

		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			doLog(r, e);
		}

		private void doLog(Runnable r, ThreadPoolExecutor e) {
			// 可做日志记录等
			System.err.println(r.toString() + " rejected");
		}

	}

	static class MyTask implements Runnable {

		private String name;

		public MyTask(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			try {
				System.out.println(this.toString() + " is running!");
				Thread.sleep(3000); // 让任务执行慢点
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return "MyTask [name=" + name + "]";
		}

	}

	public class ThreadA extends Thread {

		private Object lock;

		public ThreadA(Object lock) {
			super();
			this.lock = lock;
		}

		@Override
		public void run() {
			try {
				synchronized (lock) {
					if (MyList.size() != 5) {
						System.out.println("wait begin " + System.currentTimeMillis());
						lock.wait();
						System.out.println("wait end  " + System.currentTimeMillis());
					}
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public class ThreadB extends Thread {

		private Object lock;

		public ThreadB(Object lock) {
			super();
			this.lock = lock;
		}

		@Override
		public void run() {
			try {
				synchronized (lock) {
					for (int i = 0; i < 10; i++) {
						MyList.add();
						if (MyList.size() == 5) {
							lock.notify();
							System.out.println("已经发出了通知");
						}
						System.out.println("添加了" + (i + 1) + "个元素!");
						Thread.sleep(1000);
					}
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private class ValuableResource {

		private Object lock = new Object();

		/***
		 * 线程获取到锁后，锁调用自己的wait方法向当前捏着自己的线程说，放开我，你去等着 线程就会变成WAITING，注意这里的线程同时也会放弃锁的使用权
		 * @throws InterruptedException
		 */
		public void doSthWaiting() throws InterruptedException {
			synchronized (lock) {
				lock.wait();// 放弃当前锁
			}

		}

		public void doSthTimedWaiting() throws InterruptedException {
			synchronized (lock) {
				lock.wait(3000L);
				while (true) {

				}
			}

		}

	}

}
