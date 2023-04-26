package com.lind.common.lb.impl;

import com.lind.common.lb.RpcLoadBalance;

import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询
 *
 */
public class RpcLoadBalanceRoundStrategy extends RpcLoadBalance {

	private ConcurrentMap<String, AtomicInteger> routeCountEachJob = new ConcurrentHashMap<String, AtomicInteger>();

	private long CACHE_VALID_TIME = 0;

	private int count(String serviceKey) {
		// cache clear
		if (System.currentTimeMillis() > CACHE_VALID_TIME) {
			routeCountEachJob.clear();
			CACHE_VALID_TIME = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
		}

		// count++
		AtomicInteger count = routeCountEachJob.get(serviceKey);
		if (count == null || count.get() > 1000000) {
			// 初始化时主动Random一次，缓解首次压力
			count = new AtomicInteger(new Random().nextInt(100));
		}
		else {
			// count++
			count.addAndGet(1);
		}

		routeCountEachJob.put(serviceKey, count);
		return count.get();
	}

	@Override
	public String route(String serviceKey, TreeSet<String> addressSet) {
		// arr
		String[] addressArr = addressSet.toArray(new String[addressSet.size()]);

		// round
		String finalAddress = addressArr[count(serviceKey) % addressArr.length];
		return finalAddress;
	}

}
