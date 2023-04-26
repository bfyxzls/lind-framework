package com.lind.common.lindbase;

import cn.hutool.core.date.DateTime;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ConcurrentSkipListMap达到一定大小就持久化到磁盘上，跳跃表的查找，删除，插入的复杂都是O(logN)，无锁设计，多线程使用它，如果是单线程可以使用TreeMap
 * 而磁盘如果希望快速被检索，我们可以借助LSM树，使用顺序写，提升写的性能
 */
public class Store {

	static final Long limitSize = 10000L;

	// 块存储大小
	private final AtomicLong dataSize = new AtomicLong();

	// 正在持久化
	private final AtomicBoolean isSnapshotFlushing = new AtomicBoolean(false);

	private final ReentrantReadWriteLock updateLock = new ReentrantReadWriteLock();

	Logger logger = LoggerFactory.getLogger(Store.class);

	// 多线程场景下性能比较好的跳跃表
	private volatile ConcurrentSkipListMap<Entity, Entity> kvMap;

	private ExecutorService pool;

	public Store(ExecutorService pool) {
		this.pool = pool;
		dataSize.set(0);
		this.kvMap = new ConcurrentSkipListMap<>();
	}

	@SneakyThrows
	public void addTry(Entity kv) {
		int i = 3;
		while (i-- > 0) {
			try {
				add(kv);
			}
			catch (IOException ex) {
				Thread.sleep(10 * i);
				logger.info("try..." + i);
			}
		}
	}

	public void add(Entity kv) throws IOException {

		flushIfNeeded(true);
		updateLock.readLock().lock();
		try {
			Entity prevKeyValue = kvMap.put(kv, kv);
			if (prevKeyValue == null) {
				dataSize.addAndGet(kv.getSerializeSize());
			}
			else {
				dataSize.addAndGet(kv.getSerializeSize() - prevKeyValue.getSerializeSize());
			}
		}
		finally {
			updateLock.readLock().unlock();
		}
		flushIfNeeded(false);
	}

	public SeekIter<Entity> createIterator() throws IOException {
		return new IteratorWrapper(kvMap);
	}

	private void flushIfNeeded(boolean shouldBlocking) throws IOException {
		if (dataSize.get() > limitSize) {
			if (isSnapshotFlushing.get() && shouldBlocking) {
				throw new IOException("Memstore is full, currentDataSize=" + dataSize.get() + "B, maxMemstoreSize="
						+ limitSize + "B, please wait until the flushing is finished.");
			}
			else if (isSnapshotFlushing.compareAndSet(false, true)) {
				pool.submit(new FlusherTask());
			}
		}
	}

	interface Iter<Entity> {

		boolean hasNext() throws IOException;

		Entity next() throws IOException;

	}

	interface SeekIter<Entity> extends Iter<Entity> {

		/**
		 * Seek to the smallest key value which is greater than or equals to the given key
		 * value.
		 * @param kv
		 */
		void seekTo(Entity kv) throws IOException;

	}

	public static class IteratorWrapper implements SeekIter<Entity> {

		private SortedMap<Entity, Entity> sortedMap;

		private Iterator<Entity> it;

		public IteratorWrapper(SortedMap<Entity, Entity> sortedMap) {
			this.sortedMap = sortedMap;
			this.it = sortedMap.values().iterator();
		}

		@Override
		public boolean hasNext() throws IOException {
			return it != null && it.hasNext();
		}

		@Override
		public Entity next() throws IOException {
			return it.next();
		}

		/**
		 * tailMap与Entity的自定义比较器有关，在Entity里，如果key值相等，就比较feq的值，feq值返回大于等于它的元素。
		 * @param kv
		 * @throws IOException
		 */
		@Override
		public void seekTo(Entity kv) throws IOException {
			it = sortedMap.tailMap(kv).values().iterator();
		}

	}

	private class FlusherTask implements Runnable {

		@SneakyThrows
		@Override
		public void run() {
			// Step.1 memstore snpashot
			updateLock.writeLock().lock();
			try {
				System.out.print("持久化");
				File file = new File("d:\\zzlBase\\" + DateTime.now().getTime() + ".tmp");
				if (!file.getParentFile().exists()) {// 父路径不存在
					file.getParentFile().mkdirs();// 创建父路径
				}
				OutputStream output = new FileOutputStream(file, true);
				kvMap.forEach((i, o) -> {
					try {
						output.write(o.toBytes());// 输出数据
					}
					catch (IOException ex) {

					}
				});
				output.close();// 关闭流

				kvMap = new ConcurrentSkipListMap<>();
				dataSize.set(0); // 持久化之后清空它

			}
			catch (IOException exception) {

			}
			finally {
				updateLock.writeLock().unlock();
				isSnapshotFlushing.compareAndSet(true, false);
			}
		}

	}

}
