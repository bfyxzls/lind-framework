package com.lind.common.core.util;

import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 异步响应式对象.
 */
public class FutureUtils {

	/**
	 * 无返回值.
	 * @param consumer
	 * @param entity
	 * @param <T>
	 * @throws Exception
	 */
	@SneakyThrows
	public static <T> void run(Consumer<T> consumer, T entity) {
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			consumer.accept(entity);
		});
		future.get();
	}

	/**
	 * 无参,有返回值.
	 * @param supplier
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	@SneakyThrows
	public static <T> CompletableFuture<T> run(Supplier<T> supplier) {
		CompletableFuture<T> future = CompletableFuture.supplyAsync(() -> {
			return supplier.get();
		});
		return future;
	}

	/**
	 * 有参数,有返回值.
	 * @param function
	 * @param entity
	 * @param <T>
	 * @param <U>
	 * @return
	 * @throws Exception
	 */
	@SneakyThrows
	public static <T, U> CompletableFuture<U> run(Function<T, U> function, T entity) {
		CompletableFuture<U> future = CompletableFuture.supplyAsync(() -> {
			return function.apply(entity);
		});
		return future;
	}

}
