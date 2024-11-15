package com.lind.common.lb.impl;

import com.lind.common.lb.RpcLoadBalance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * lfu最不常使用(Least Frequently Used)
 * 当缓存已满，需要替换某个缓存项时，选择访问频率最低的那个项进行替换
 *
 */
public class RpcLoadBalanceLFUStrategy extends RpcLoadBalance {

	private ConcurrentMap<String, HashMap<String, Integer>> jobLfuMap = new ConcurrentHashMap<String, HashMap<String, Integer>>();

	private long CACHE_VALID_TIME = 0;

	public String doRoute(String serviceKey, TreeSet<String> addressSet) {

		// cache clear
		if (System.currentTimeMillis() > CACHE_VALID_TIME) {
			jobLfuMap.clear();
			CACHE_VALID_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
		}

		// lfu item init
		HashMap<String, Integer> lfuItemMap = jobLfuMap.get(serviceKey); // Key排序可以用TreeMap+构造入参Compare；Value排序暂时只能通过ArrayList；
		if (lfuItemMap == null) {
			lfuItemMap = new HashMap<String, Integer>();
			jobLfuMap.putIfAbsent(serviceKey, lfuItemMap); // 避免重复覆盖
		}

		// put new
		for (String address : addressSet) {
			if (!lfuItemMap.containsKey(address) || lfuItemMap.get(address) > 1_000_000) {
				lfuItemMap.put(address, 0);
			}
		}

		// remove old
		List<String> delKeys = new ArrayList<>();
		for (String existKey : lfuItemMap.keySet()) {
			if (!addressSet.contains(existKey)) {
				delKeys.add(existKey);
			}
		}
		if (delKeys.size() > 0) {
			for (String delKey : delKeys) {
				lfuItemMap.remove(delKey);
			}
		}

		// load least userd count address
		List<Map.Entry<String, Integer>> lfuItemList = new ArrayList<Map.Entry<String, Integer>>(lfuItemMap.entrySet());
		Collections.sort(lfuItemList, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});

		Map.Entry<String, Integer> addressItem = lfuItemList.get(0);
		String minAddress = addressItem.getKey();
		addressItem.setValue(addressItem.getValue() + 1);

		return minAddress;
	}

	@Override
	public String route(String serviceKey, TreeSet<String> addressSet) {
		String finalAddress = doRoute(serviceKey, addressSet);
		return finalAddress;
	}

}
