package com.lind.common.core.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ConsistenceHashUtilsTest {

	public void init() {
		final String IP_PREFIX = "192.168.0.";
		// 每台真实机器节点上保存的记录条数
		Map<String, Integer> map = new HashMap<String, Integer>();

		// 真实机器节点, 模拟10台
		List<ConsistenceHashUtils.Node<String>> nodes = new ArrayList<ConsistenceHashUtils.Node<String>>();
		for (int i = 1; i <= 10; i++) {
			map.put(IP_PREFIX + i, 0); // 初始化记录
			ConsistenceHashUtils.Node<String> node = new ConsistenceHashUtils.Node<String>(IP_PREFIX + i, "node" + i);
			nodes.add(node);
		}

		ConsistenceHashUtils.IHashService iHashService = new ConsistenceHashUtils.HashService();
		// 每台真实机器引入100个虚拟节点
		ConsistenceHashUtils.ConsistentHash<ConsistenceHashUtils.Node<String>> consistentHash = new ConsistenceHashUtils.ConsistentHash<ConsistenceHashUtils.Node<String>>(
				iHashService, 500, nodes);

		// 将5000条记录尽可能均匀的存储到10台机器节点上
		for (int i = 0; i < 5000; i++) {
			// 产生随机一个字符串当做一条记录，可以是其它更复杂的业务对象,比如随机字符串相当于对象的业务唯一标识
			String data = UUID.randomUUID().toString() + i;
			// 通过记录找到真实机器节点
			ConsistenceHashUtils.Node<String> node = consistentHash.get(data);
			// 再这里可以能过其它工具将记录存储真实机器节点上，比如MemoryCache等
			// ...
			// 每台真实机器节点上保存的记录条数加1
			map.put(node.getIp(), map.get(node.getIp()) + 1);
		}

		// 打印每台真实机器节点保存的记录条数
		for (int i = 1; i <= 10; i++) {
			System.out.println(IP_PREFIX + i + "节点记录条数：" + map.get(IP_PREFIX + i));
		}
	}

	@Test
	public void test() {
		test();
	}

}
