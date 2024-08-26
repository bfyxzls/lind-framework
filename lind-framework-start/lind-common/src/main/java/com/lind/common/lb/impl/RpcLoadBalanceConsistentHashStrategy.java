package com.lind.common.lb.impl;

import com.lind.common.lb.RpcLoadBalance;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * consistent hash
 * 一致性哈希（Consistent Hashing）算法是一种用于分布式系统中数据分片和负载均衡的算法。它的主要目的是在动态增减节点的情况下，尽可能地减少数据重新分布的影响
 *
 * @author xuxueli 2018-12-04
 */
public class RpcLoadBalanceConsistentHashStrategy extends RpcLoadBalance {

	// 虚拟节点，避免出现数据倾斜
	private int VIRTUAL_NODE_NUM = 100;

	/**
	 * get hash code on 2^32 ring (md5散列的方式计算hash值)
	 * @param key
	 * @return
	 */
	public long hash(String key) {

		// md5 byte
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 not supported", e);
		}
		md5.reset();
		byte[] keyBytes = null;
		try {
			keyBytes = key.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unknown string :" + key, e);
		}

		md5.update(keyBytes);
		byte[] digest = md5.digest();

		// hash code, Truncate to 32-bits，digest[3]&0xFF相当于将有符号的byte转换为无符号的int
		// 在 Java 中，byte 类型是有符号的，取值范围是 -128 到 127，而使用 & 0xFF 操作可以将其转换为0到255之间的无符号整数。
		long hashCode = ((long) (digest[3] & 0xFF) << 24) | ((long) (digest[2] & 0xFF) << 16)
				| ((long) (digest[1] & 0xFF) << 8) | (digest[0] & 0xFF);
		long truncateHashCode = hashCode & 0xffffffffL;// 保留低32位(这个long类型的数字很特殊，它高32位都是0，低32位都是1)
		return truncateHashCode;
	}

	public String doRoute(String serviceKey, TreeSet<String> addressSet) {
		TreeMap<Long, String> addressRing = new TreeMap<Long, String>();
		for (String address : addressSet) {
			for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
				long addressHash = hash("SHARD-" + address + "-NODE-" + i);
				addressRing.put(addressHash, address);
			}
		}

		long jobHash = hash(serviceKey);
		// 得到大于当前hash值的所有map
		SortedMap<Long, String> lastRing = addressRing.tailMap(jobHash);
		if (!lastRing.isEmpty()) {
			return lastRing.get(lastRing.firstKey());
		}
		return addressRing.firstEntry().getValue();
	}

	@Override
	public String route(String serviceKey, TreeSet<String> addressSet) {
		String finalAddress = doRoute(serviceKey, addressSet);
		return finalAddress;
	}

}
