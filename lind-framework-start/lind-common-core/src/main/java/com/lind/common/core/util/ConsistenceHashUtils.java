package com.lind.common.core.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性hash算法.
 */
public class ConsistenceHashUtils {

	public interface IHashService {

		Long hash(String key);

	}

	public static class HashService implements IHashService {

		/**
		 * MurMurHash算法,性能高,碰撞率低
		 * @param key String
		 * @return Long
		 */
		public Long hash(String key) {
			ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
			int seed = 0x1234ABCD;

			ByteOrder byteOrder = buf.order();
			buf.order(ByteOrder.LITTLE_ENDIAN);

			long m = 0xc6a4a7935bd1e995L;
			int r = 47;

			long h = seed ^ (buf.remaining() * m);

			long k;
			while (buf.remaining() >= 8) {
				k = buf.getLong();

				k *= m;
				k ^= k >>> r;
				k *= m;

				h ^= k;
				h *= m;
			}

			if (buf.remaining() > 0) {
				ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
				finish.put(buf).rewind();
				h ^= finish.getLong();
				h *= m;
			}

			h ^= h >>> r;
			h *= m;
			h ^= h >>> r;

			buf.order(byteOrder);
			return h;

		}

	}

	public static class Node<T> {

		private String ip;

		private String name;

		public Node(String ip, String name) {
			this.ip = ip;
			this.name = name;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		/**
		 * 使用IP当做hash的Key
		 * @return String
		 */
		@Override
		public String toString() {
			return ip;
		}

	}

	public static class ConsistentHash<T> {

		// Hash函数接口
		private final IHashService iHashService;

		// 每个机器节点关联的虚拟节点数量
		private final int numberOfReplicas;

		// 环形虚拟节点
		private final SortedMap<Long, T> circle = new TreeMap<Long, T>();

		public ConsistentHash(IHashService iHashService, int numberOfReplicas, Collection<T> nodes) {
			this.iHashService = iHashService;
			this.numberOfReplicas = numberOfReplicas;
			for (T node : nodes) {
				add(node);
			}
		}

		/**
		 * 增加真实机器节点
		 * @param node T
		 */
		public void add(T node) {
			for (int i = 0; i < this.numberOfReplicas; i++) {
				circle.put(this.iHashService.hash(node.toString() + i), node);
			}
		}

		/**
		 * 删除真实机器节点
		 * @param node T
		 */
		public void remove(T node) {
			for (int i = 0; i < this.numberOfReplicas; i++) {
				circle.remove(this.iHashService.hash(node.toString() + i));
			}
		}

		public T get(String key) {
			if (circle.isEmpty())
				return null;

			long hash = iHashService.hash(key);

			// 沿环的顺时针找到一个虚拟节点
			if (!circle.containsKey(hash)) {
				SortedMap<Long, T> tailMap = circle.tailMap(hash);
				hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
			}
			return circle.get(hash);
		}

	}

}
