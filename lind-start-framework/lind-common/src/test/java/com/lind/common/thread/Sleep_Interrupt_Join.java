package com.lind.common.thread;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2022/11/17 9:41
 * @since 1.0.0
 */
public class Sleep_Interrupt_Join {

	public static class Test3 {

		private int i = 10;

		private Object object = new Object();

		public static void main(String[] args) throws IOException {
			Test3 t = new Test3();
			MyThread thread1 = t.new MyThread();
			MyThread thread2 = t.new MyThread();
			thread1.start();
			thread2.start();
			try {
				System.out.println("线程" + Thread.currentThread().getName() + "等待");
				thread2.join();// 调用thread.join方法，则main方法会等待thread线程执行完毕或者等待一定的时间。
				// 如果调用的是无参join方法，则等待thread执行完毕，如果调用的是指定了时间参数的join方法，则等待一定的时间。
				// 实际上调用join方法是调用了Object的wait方法。
				// wait方法会让线程进入阻塞状态，并且会释放线程占有的锁，并交出CPU执行权限。
				System.out.println("线程" + Thread.currentThread().getName() + "继续执行");
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		class MyThread extends Thread {

			@Override
			public void run() {
				// synchronized (object){ //同步锁，当前线程运行完才释放锁。
				i++;
				System.out.println("i:" + i);
				try {
					System.out.println("线程" + Thread.currentThread().getName() + "进入睡眠状态");
					Thread.currentThread().sleep(10000); // sleep相当于让线程睡眠，交出CPU，让CPU去执行其他的任务.sleep方法不会释放锁，
					// 也就是说如果当前线程持有对某个对象的锁，则即使调用sleep方法，其他线程也无法访问这个对象
					// 如果调用了sleep方法，必须捕获InterruptedException异常或者将该异常向上层抛出。当线程睡眠时间满后，不一定会立即得到执行，
					// 因为此时可能CPU正在执行其他的任务。所以说调用sleep方法相当于让线程进入阻塞状态
				}
				catch (InterruptedException e) {
					System.out.println("进程睡眠失败");
				}
				System.out.println("线程" + Thread.currentThread().getName() + "睡眠结束");
				i++;
				System.out.println("i:" + i);
				// }
			}

		}

	}

	public static class Test4 {

		private int i = 0;

		public static void main(String[] args) throws IOException {
			Test4 t = new Test4();
			MyThread thread0 = t.new MyThread();
			MyThread thread1 = t.new MyThread();
			thread0.start();
			thread1.start();
			thread1.interrupt();
			// 单独调用interrupt方法可以使得处于阻塞状态的线程抛出一个异常，
			// 也就说，它可以用来中断一个正处于阻塞状态的线程；
			// 另外，通过interrupt方法和isInterrupted()方法来停止正在运行的线程。
			// 直接调用interrupt方法不能中断正在运行中的线程。
		}

		class MyThread extends Thread {

			@Override
			public void run() {
				i++;
				System.out.println("i:" + i);
				try {
					System.out.println("线程" + Thread.currentThread().getName() + "进入睡眠");
					Thread.currentThread().sleep(10000);
					System.out.println("线程" + Thread.currentThread().getName() + "睡眠完毕");
				}
				catch (InterruptedException e) {
					System.out.println("线程" + Thread.currentThread().getName() + "中断异常");
				}
				System.out.println("run方法执行完毕");
				i++;
				System.out.println("i:" + i);
			}

		}

	}

	static class JoinTest {

		public static int a = 0;

		public static void main(String... args) throws InterruptedException {

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 10; i++)
						a++;
				}
			});

			thread.start();

			// 在main线程里执行了join，所以main线程获得了join的锁
			// 后面join源码分析里执行wait是main线程（不要搞乱了）
			thread.join(10000);
			System.out.println("a=" + a);
			// 如果不加 join 正常情况下应该是小于10的
			// 但是加了 join 后，它会等待thread线程执行结束后再执行主线程
			// 所以答案等于10
		}

	}

	static class InterruptTest {

		static boolean stop = true;

		public static void main(String... args) {

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (stop) {
						try {
							System.out.println("start");
							Thread.sleep(100000);
							System.out.println("stop");
						}
						catch (InterruptedException e) {
							stop = false;
						}
					}
				}
			});
			thread.start();
			thread.interrupt();
		}

	}

	static class DaemonTest {

		static Integer i = 0;

		private volatile static boolean toStop = false;

		public static void main(String... args) throws InterruptedException {

			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					System.out.println(Thread.currentThread().getId() + "=i=" + i);
					Thread daemonThread = new Thread(() -> {
						while (toStop) {
							System.out.print("我是守护线程" + "\n");
							try {
								i++;
								Thread.sleep(100);
							}
							catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
					// 设置为守护线程
					daemonThread.setDaemon(true);
					daemonThread.start();
					System.out.print("开始运行了" + "\n");
				}
			});

			ThreadPoolExecutor callbackThreadPool = new ThreadPoolExecutor(1, 20, 5L, TimeUnit.SECONDS,
					new LinkedBlockingQueue<Runnable>(30), r -> new Thread(r, "lind ThreadPool-" + r.hashCode()),
					(r, executor) -> {
						r.run();
						System.err.println("lind ThreadPool, match threadpool rejected handler(run now).");

					});
			callbackThreadPool.execute(thread);
			toStop = true;

		}
		// 正常来说daemonThread会一直循环
		// 当时当他设置为守护线程后 thread执行结束后 daemonThread也随之结束。

	}

}
