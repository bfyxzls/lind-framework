package com.lind.common.thread;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.github.phantomthief.pool.KeyAffinityExecutor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StopWatch;
import org.testng.collections.Sets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadPoolUtilsTest {

	static ExecutorService executorService = Executors.newFixedThreadPool(4);
	static KeyAffinityExecutor executor = KeyAffinityExecutor.newSerializingExecutor(8, 200, "MY-POOL");
	static boolean SYNCHRONIZED = false;

	public static void executeByOldPool(List<Person> personList) {
		personList.stream().forEach(p -> executorService.execute(() -> {
			System.out.println(JSON.toJSONString(p));
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}));
	}

	public static void executeByAffinitydPool(List<Person> personList) {
		personList.stream().forEach(p -> executor.executeEx(p.getId(), () -> {
			System.out.println(JSON.toJSONString(p));
		}));
	}

	public static void main(String[] args) throws InterruptedException {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("main");
		final List<Callable<Void>> runnablesToTasks = new LinkedList<>();
		runnablesToTasks.add(() -> {
			try {
				Thread.sleep(5000);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			System.out.println("a5000..." + Thread.currentThread().getName());
			return null;
		});
		runnablesToTasks.add(() -> {
			try {
				Thread.sleep(6000);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			System.out.println("a6000..." + Thread.currentThread().getName());
			return null;
		});
		runnablesToTasks.add(() -> {
			try {
				Thread.sleep(5500);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			System.out.println("a5500..." + Thread.currentThread().getName());
			return null;
		});
		final ExecutorService service = SYNCHRONIZED ? Executors.newSingleThreadExecutor()
				: Executors.newFixedThreadPool(4);

		service.invokeAll(runnablesToTasks);
		service.shutdown();
		Boolean isClose = service.awaitTermination(3, TimeUnit.MINUTES);
		if (!isClose)
			service.shutdown();

		stopWatch.stop();
		System.out.println("result:" + stopWatch.prettyPrint());
	}

	/**
	 * 普通线程池无法保证有序性.
	 * @throws InterruptedException
	 */
	@Test
	public void normalPool() throws InterruptedException {
		// 1.创建列表
		List<Person> personList = new ArrayList<>();
		personList.add(Person.builder().id(1).data("1s").build());
		personList.add(Person.builder().id(2).data("2s").build());
		personList.add(Person.builder().id(1).data("11s").build());
		personList.add(Person.builder().id(3).data("3s").build());
		personList.add(Person.builder().id(1).data("111s").build());
		personList.add(Person.builder().id(2).data("22s").build());
		personList.add(Person.builder().id(3).data("33s").build());
		personList.add(Person.builder().id(1).data("1111s").build());

		// 2.使用普通线程池执行
		executeByOldPool(personList);

		Thread.sleep(1000);
	}

	/**
	 * Simple pool保存了有序性
	 * @throws InterruptedException
	 */
	@Test
	public void phantomthiefSimplePool() throws InterruptedException {
		// 1.创建列表
		List<Person> personList = new ArrayList<>();
		personList.add(Person.builder().id(1).data("1s").build());
		personList.add(Person.builder().id(2).data("2s").build());
		personList.add(Person.builder().id(1).data("11s").build());
		personList.add(Person.builder().id(3).data("3s").build());
		personList.add(Person.builder().id(1).data("111s").build());
		personList.add(Person.builder().id(2).data("22s").build());
		personList.add(Person.builder().id(3).data("33s").build());
		personList.add(Person.builder().id(1).data("1111s").build());

		executeByAffinitydPool(personList);

		Thread.sleep(1000);
	}

	@Test
	public void newFixedThreadPool() {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
		executor.submit(() -> {
			Thread.sleep(1000);
			return null;
		});
		executor.submit(() -> {
			Thread.sleep(1000);
			return null;
		});
		executor.submit(() -> {
			Thread.sleep(1000);
			return null;
		});

		assertEquals(2, executor.getPoolSize());// 线程池核心数大小
		assertEquals(1, executor.getQueue().size());// 阻塞队列大小
	}

	@Test
	public void newCachedThreadPool() {
		/**
		 * We can create another preconfigured ThreadPoolExecutor with the
		 * Executors.newCachedThreadPool() method. This method does not receive a number
		 * of threads at all. We set the corePoolSize to 0 and set the maximumPoolSize to
		 * Integer.MAX_VALUE. Finally, the keepAliveTime is 60 seconds:
		 */
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		executor.submit(() -> {
			Thread.sleep(1000);
			return null;
		});
		executor.submit(() -> {
			Thread.sleep(1000);
			return null;
		});
		executor.submit(() -> {
			Thread.sleep(1000);
			return null;
		});

		assertEquals(3, executor.getPoolSize());
		assertEquals(0, executor.getQueue().size());
	}

	@Test
	public void newSingleThreadExecutor() {
		/**
		 * Additionally, this ThreadPoolExecutor is decorated with an immutable wrapper,
		 * so it can't be reconfigured after creation. Note that this is also the reason
		 * we can't cast it to a ThreadPoolExecutor.
		 */
		AtomicInteger counter = new AtomicInteger();

		ExecutorService executor = Executors.newSingleThreadExecutor();// 不能转换成一个ThreadPoolExecutor，它是只读的。
		executor.submit(() -> {
			counter.set(1);
		});
		executor.submit(() -> {
			counter.compareAndSet(1, 2);
		});
	}

	@Test
	public void newScheduledThreadPool() throws InterruptedException {
		/**
		 * The following code shows how to run a task after 500 milliseconds delay and
		 * then repeat it every 100 milliseconds. After scheduling the task, we wait until
		 * it fires three times using the CountDownLatch lock. Then we cancel it using the
		 * Future.cancel() method:
		 * 下面的代码展示了如何在500毫秒延迟后运行任务，然后每100毫秒重复一次。在调度任务之后，我们使用CountDownLatch锁等待它触发三次。
		 * 然后我们使用Future.cancel()方法取消它:
		 */
		CountDownLatch lock = new CountDownLatch(3);

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
		ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> {
			System.out.println("Hello World");
			lock.countDown();
		}, 500, 1000, TimeUnit.MILLISECONDS);

		lock.await(10000, TimeUnit.MILLISECONDS);
		future.cancel(true);
	}

	@Test
	public void forkJoinPool() {
		/**
		 * Now if we want to sum all values in a tree in parallel, we need to implement a
		 * RecursiveTask<Integer> interface. Each task receives its own node and adds its
		 * value to the sum of values of its children. To calculate the sum of children
		 * values, task implementation does the following:
		 *
		 * streams the children set maps over this stream, creating a new CountingTask for
		 * each element runs each subtask by forking it collects the results by calling
		 * the join method on each forked task sums the results using the
		 * Collectors.summingInt collector
		 */
		TreeNode tree = new TreeNode(5, new TreeNode(3),
				new TreeNode(2, new TreeNode(3), new TreeNode(8), new TreeNode(4)));

		ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
		int sum = forkJoinPool.invoke(new CountingTask(tree));
		System.out.println("sum=" + sum);
	}

	/**
	 * 一般性的线程池.
	 */
	@Test
	public void standard() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		// 核心线程大小 默认区 CPU 数量
		taskExecutor.setCorePoolSize(2);
		// 最大线程大小 默认区 CPU * 2 数量
		taskExecutor.setMaxPoolSize(2 * 2);
		// 队列最大容量
		taskExecutor.setQueueCapacity(10);
		taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		// AwaitTerminationSeconds:该方法调用会被阻塞，表示主线等待各子线程的时间
		taskExecutor.setAwaitTerminationSeconds(60);
		taskExecutor.setKeepAliveSeconds(60);
		taskExecutor.setThreadNamePrefix("Standard-Thread-");
		taskExecutor.initialize();
		List<Callable<Integer>> retryWriterThreads = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			int finalI = i;
			retryWriterThreads.add(() -> {
				System.out.format("i=%s,id=%s\n", String.valueOf(finalI), Thread.currentThread().getName());
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				return 0;
			});
		}
		for (Callable<Integer> item : retryWriterThreads) {
			taskExecutor.submit(item);
		}

		taskExecutor.shutdown();
	}

	@Test
	public void single() {
		Executors.newSingleThreadExecutor().execute(() -> {
			HttpUtil.get("http://localhost:81/exit");
		});
	}

	static class TreeNode {

		int value;

		Set<TreeNode> children;

		TreeNode(int value, TreeNode... children) {
			this.value = value;
			this.children = Sets.newHashSet(children);
		}

	}

	public static class CountingTask extends RecursiveTask<Integer> {

		private final TreeNode node;

		public CountingTask(TreeNode node) {
			this.node = node;
		}

		@Override
		protected Integer compute() {
			return node.value + node.children.stream().map(childNode -> new CountingTask(childNode).fork())
					.collect(Collectors.summingInt(ForkJoinTask::join));
		}

	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	static class Person {

		private Integer id;

		private String data;

	}

}
