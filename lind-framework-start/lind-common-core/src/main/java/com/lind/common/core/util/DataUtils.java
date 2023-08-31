package com.lind.common.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 数据集并行处理工具
 */
public class DataUtils {

	/**
	 * 并行处理线程数字
	 */
	static final int THREAD_COUNT = 50;

	/**
	 * 单线程中处理的集合的长度,50个线程，每个线程处理2条，如果处理时间为1S，则需要2S的时间.
	 */
	static final int INNER_LIST_LENGTH = 2;
	static Logger logger = LoggerFactory.getLogger(DataUtils.class);

	/**
	 * 大集合拆分.
	 * @param list
	 * @param len
	 * @param <T>
	 * @return
	 */
	private static <T> List<List<T>> splitList(List<T> list, int len) {
		if (list == null || list.size() == 0 || len < 1) {
			return null;
		}
		List<List<T>> result = new ArrayList<List<T>>();
		int size = list.size();
		int count = (size + len - 1) / len;
		for (int i = 0; i < count; i++) {
			List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
			result.add(subList);
		}
		return result;
	}

	/**
	 * 并行处理.
	 * @param list 大集合
	 * @param pageSize 单页数据大小
	 * @param consumer 处理程序
	 * @param <T>
	 */
	public static <T> void fillDataByPage(List<T> list, int pageSize, Consumer<T> consumer) {

		List<List<T>> innerList = new ArrayList<>();
		splitList(list, pageSize).forEach(o -> innerList.add(o));
		int totalPage = innerList.size();
		AtomicInteger i = new AtomicInteger();
		innerList.forEach(items -> {
			ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
			i.getAndIncrement();
			Collection<BufferInsert<T>> bufferInserts = new ArrayList<>();
			splitList(items, INNER_LIST_LENGTH).forEach(o -> {
				bufferInserts.add(new BufferInsert(o, consumer));
			});

			try {
				executor.invokeAll(bufferInserts);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			executor.shutdown();
			logger.info("【当前数据页:{}/{}】", i.get(), totalPage);
		});

	}

	/**
	 * 将大集合拆分成小集合，然后串行处理.
	 * @param list
	 * @param pageSize
	 * @param consumer
	 * @param <T>
	 */
	public static <T> void fillDataListByPage(List<T> list, int pageSize, Consumer<List<T>> consumer) {
		List<List<T>> innerList = new ArrayList<>();
		splitList(list, pageSize).forEach(o -> innerList.add(o));
		int totalPage = innerList.size();
		AtomicInteger i = new AtomicInteger();
		innerList.forEach(o -> {
			consumer.accept(o);
			i.getAndIncrement();
			logger.info("【当前数据页:{}/{}】", i.get(), totalPage);
		});
	}

	/**
	 * 多线程并发处理数据.
	 *
	 * @param <T>
	 */
	static class BufferInsert<T> implements Callable<Integer> {

		/**
		 * 要处理的数据列表.
		 */
		List<T> items;

		/**
		 * 处理程序.
		 */
		Consumer<T> consumer;

		public BufferInsert(List<T> items, Consumer<T> consumer) {
			this.items = items;
			this.consumer = consumer;
		}

		@Override
		public Integer call() {
			for (T item : items) {
				this.consumer.accept(item);
			}
			return 1;
		}

	}

}
