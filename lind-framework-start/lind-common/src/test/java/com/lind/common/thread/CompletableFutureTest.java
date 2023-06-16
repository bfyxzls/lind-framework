package com.lind.common.thread;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 异步操作
 */
@Slf4j
public class CompletableFutureTest {

	static Integer total = 0;
	static AtomicInteger atomicTotal = new AtomicInteger(0);

	/**
	 * 有返回值
	 * @throws Exception
	 */
	public static void supplyAsync() throws Exception {
		CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			}
			catch (InterruptedException e) {
			}
			System.out.println("run end ...");
			return System.currentTimeMillis();
		});

		long time = future.get();
		System.out.println("time = " + time);
	}

	/**
	 * handle对线程处理结果进行加工
	 * @throws Exception
	 */
	@Test
	public void handle() throws Exception {
		CompletableFuture<Integer> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {

			@Override
			public Integer get() {
				int i = 10 / 1;
				return new Random().nextInt(10);
			}
		}).handle(new BiFunction<Integer, Throwable, Integer>() {
			@Override
			public Integer apply(Integer param, Throwable throwable) {
				int result = -1;
				if (throwable == null) {
					result = param * 2;
				}
				else {
					System.out.println(throwable.getMessage());
				}
				return result;
			}
		});
		System.out.println(future.get());
	}

	/**
	 * thenApply完成线程之前的结果依赖
	 * @throws Exception
	 */
	@Test
	public void thenApply() throws Exception {
		CompletableFuture<Long> future = CompletableFuture.supplyAsync(new Supplier<Long>() {
			@Override
			public Long get() {
				long result = new Random().nextInt(100);
				System.out.println("result1=" + result);
				return result;
			}
		}).thenApply(new Function<Long, Long>() {
			@Override
			public Long apply(Long t) {
				long result = t * 5;
				System.out.println("result2=" + result);
				return result;
			}
		});

		long result = future.get();
		System.out.println(result);
	}

	/**
	 * 无返回值
	 */
	@Test
	public void runAsync() throws Exception {
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			}
			catch (InterruptedException e) {
			}
			System.out.println("run end ...");
		});

		future.get();
	}

	/**
	 * whenComplete 和 whenCompleteAsync 的区别： whenComplete：是执行当前任务的线程执行完成后，继续执行
	 * whenComplete的任务。 whenCompleteAsync：是执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行。
	 * @throws Exception
	 */
	@Test
	public void whenComplete() throws Exception {
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			}
			catch (InterruptedException e) {
			}
			if (new Random().nextInt() % 2 >= 0) {
				int i = 12 / 1;
			}
			System.out.println("run end ..." + Thread.currentThread().getId());
		});

		future.whenComplete(new BiConsumer<Void, Throwable>() {
			@SneakyThrows
			@Override
			public void accept(Void t, Throwable action) {
				Thread.sleep(10);// 占用这个线程
				System.out.println("whenComplete执行完成！" + Thread.currentThread().getId());
			}

		});

		// 异步执行
		future.whenCompleteAsync(new BiConsumer<Void, Throwable>() {
			@Override
			public void accept(Void t, Throwable action) {
				System.out.println("whenCompleteAsync执行完成！" + Thread.currentThread().getId());
			}

		});

		future.exceptionally(new Function<Throwable, Void>() {
			@Override
			public Void apply(Throwable t) {
				System.out.println("执行失败！" + t.getMessage());
				return null;
			}
		});

		TimeUnit.SECONDS.sleep(2);
	}

	/**
	 * 接收任务的处理结果，并消费处理，无返回结果
	 * @throws Exception
	 */
	@Test
	public void thenAccept() throws Exception {
		CompletableFuture<Void> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
			@Override
			public Integer get() {
				return new Random().nextInt(10);
			}
		}).thenAccept(integer -> {
			System.out.println(integer);
		});
		future.get();
	}

	/**
	 * 不关心任务的处理结果。只要上面的任务执行完成，就开始执行 thenRun
	 * @throws Exception
	 */
	@Test
	public void thenRun() throws Exception {
		CompletableFuture<Void> future = CompletableFuture.supplyAsync(new Supplier<Integer>() {
			@Override
			public Integer get() {
				return new Random().nextInt(10);
			}
		}).thenRun(() -> {
			System.out.println("thenRun ...");
		});
		future.get();
	}

	/**
	 * thenCombine 会把 两个 CompletionStage 的任务都执行完成后，把两个任务的结果一块交给 thenCombine 来处理。
	 * @throws Exception
	 */
	@Test
	public void thenCombine() throws Exception {
		CompletableFuture<String> future1 = CompletableFuture.supplyAsync(new Supplier<String>() {
			@Override
			public String get() {
				return "hello";
			}
		});
		CompletableFuture<String> future2 = CompletableFuture.supplyAsync(new Supplier<String>() {
			@Override
			public String get() {
				return "hello";
			}
		});
		CompletableFuture<String> result = future1.thenCombine(future2, new BiFunction<String, String, String>() {
			@Override
			public String apply(String t, String u) {
				return t + " " + u;
			}
		});
		System.out.println(result.get());
	}

	/**
	 * 当两个CompletionStage都执行完成后，把结果一块交给thenAcceptBoth来进行消耗
	 * @throws Exception
	 */
	@Test
	public void thenAcceptBoth() throws Exception {
		CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
			@Override
			public Integer get() {
				int t = new Random().nextInt(3);
				try {
					TimeUnit.SECONDS.sleep(t);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("f1=" + t);
				return t;
			}
		});

		CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
			@Override
			public Integer get() {
				int t = new Random().nextInt(3);
				try {
					TimeUnit.SECONDS.sleep(t);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("f2=" + t);
				return t;
			}
		});
		f1.thenAcceptBoth(f2, new BiConsumer<Integer, Integer>() {
			@Override
			public void accept(Integer t, Integer u) {
				System.out.println("f1=" + t + ";f2=" + u + ";");
			}
		});
	}

	/**
	 * 两个CompletionStage，谁执行返回的结果快，我就用那个CompletionStage的结果进行下一步的转化操作。
	 * @throws Exception
	 */
	@Test
	public void applyToEither() throws Exception {
		CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
			@Override
			public Integer get() {
				int t = 3;
				try {
					TimeUnit.SECONDS.sleep(t);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("f1=" + t);
				return t;
			}
		});
		CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
			@Override
			public Integer get() {
				int t = 5;
				try {
					TimeUnit.SECONDS.sleep(t);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("f2=" + t);
				return t;
			}
		});

		CompletableFuture<Integer> result = f1.applyToEither(f2, new Function<Integer, Integer>() {
			@Override
			public Integer apply(Integer t) {
				System.out.println(t);
				return t * 2;
			}
		});

		System.out.println(result.get());
	}

	/**
	 * thenCompose 方法允许你对两个 CompletionStage 进行流水线操作，第一个操作完成时，将其结果作为参数传递给第二个操作。
	 * @throws Exception
	 */
	public void thenCompose() throws Exception {
		CompletableFuture<Integer> f = CompletableFuture.supplyAsync(new Supplier<Integer>() {
			@Override
			public Integer get() {
				int t = new Random().nextInt(3);
				System.out.println("t1=" + t);
				return t;
			}
		}).thenCompose(new Function<Integer, CompletionStage<Integer>>() {
			@Override
			public CompletionStage<Integer> apply(Integer param) {
				return CompletableFuture.supplyAsync(new Supplier<Integer>() {
					@Override
					public Integer get() {
						int t = param * 2;
						System.out.println("t2=" + t);
						return t;
					}
				});
			}

		});
		System.out.println("thenCompose result : " + f.get());
	}

	/**
	 * 多任务并行,2秒执行完这4个任务.
	 * @throws Exception
	 */
	@Test
	public void multiTask() throws Exception {

		Runnable runnable1 = () -> {
			try {
				log.info("Executing in thread {}", Thread.currentThread().getName());
				total++;
				atomicTotal.incrementAndGet();
				Thread.sleep(2000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.info("Completed in thread {}", Thread.currentThread().getName());

		};
		// 创建一个任务列表
		List<Runnable> tasks = new ArrayList<>();

		// 多任务并行之后，total将出现线程安全问题
		for (int i = 0; i < 1000; i++) {
			tasks.add(runnable1);
		}
		// 创建线程池
		ExecutorService executorService = Executors.newFixedThreadPool(tasks.size());

		// 使用 CompletableFuture 来提交任务并获取结果
		List<CompletableFuture<Void>> futures = new ArrayList<>();
		for (Runnable task : tasks) {
			CompletableFuture<Void> future = CompletableFuture.runAsync(task, executorService);
			futures.add(future);
		}

		// 等待所有任务完成
		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();

		// 关闭线程池
		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.MINUTES);
		log.info("total={},atomicTotal={}", total, atomicTotal.get());// total<1000,but
																		// atomicTotal=1000
		log.info("All tasks completed.");
	}

}
