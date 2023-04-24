package com.lind.mybatis.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 数据处理辅助类.
 */
@SuppressWarnings("unchecked")
public class DataHelper {

	/**
	 * 每个线程处理的集合的长度.
	 */
	public static final int PER_THREAD_TASK_LEN = 50;

	/**
	 * 程序池的大小.
	 */
	static final int THREAD_COUNT = 50;
	static Logger logger = LoggerFactory.getLogger(DataHelper.class);

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

	@SneakyThrows
	public static <T> void fillDataByPage(int total, int pageSize, BaseMapper<T> mapper, QueryWrapper<T> query,
			Consumer<T> consumer) {
		fillDataByPage(1, total, pageSize, mapper, query, consumer);
	}

	/**
	 * 分页处理逻辑.
	 * @param total
	 * @param mapper
	 * @param query
	 * @param consumer
	 * @param <T>
	 */
	@SneakyThrows
	public static <T> void fillDataByPage(int pageIndex, int total, int pageSize, BaseMapper<T> mapper,
			QueryWrapper<T> query, Consumer<T> consumer) {
		Integer totalPage = total / pageSize;

		if (totalPage < pageSize) {
			if (total % pageSize != 0) {
				totalPage++;
			}
		}
		else if (totalPage % pageSize != 0) {
			totalPage++;
		}
		for (int i = pageIndex; i <= totalPage; i++) {
			IPage<T> pages = mapper.selectPage(new Page<>(i, pageSize), query);
			List<T> list = pages.getRecords();
			ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
			Collection<BufferInsert<T>> bufferInserts = new ArrayList<>();
			splitList(list, PER_THREAD_TASK_LEN).forEach(o -> {
				bufferInserts.add(new BufferInsert(o, consumer));
			});

			executor.invokeAll(bufferInserts);
			executor.shutdown();

			logger.info("【当前数据页:{}/{}】", i, totalPage);
		}
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
