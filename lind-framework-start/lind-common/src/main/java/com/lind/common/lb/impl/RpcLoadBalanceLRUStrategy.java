package com.lind.common.lb.impl;

import com.lind.common.lb.RpcLoadBalance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * lru最近最少使用(Least Recently Used)
 * 如果该页面不在缓存中且缓存已满，则将链表尾部的页面淘汰，并将新页面添加到链表头部。这样就保证了最近访问的页面总是位于链表头部，而最久未被访问的页面总是位于链表尾部
 *
 */
public class RpcLoadBalanceLRUStrategy extends RpcLoadBalance {

	private ConcurrentMap<String, LinkedHashMap<String, String>> jobLRUMap = new ConcurrentHashMap<String, LinkedHashMap<String, String>>();

	private long CACHE_VALID_TIME = 0;

	public String doRoute(String serviceKey, TreeSet<String> addressSet) {

		// cache clear
		if (System.currentTimeMillis() > CACHE_VALID_TIME) {
			jobLRUMap.clear();
			CACHE_VALID_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
		}

		// init lru
		LinkedHashMap<String, String> lruItem = jobLRUMap.get(serviceKey);
		if (lruItem == null) {
			/**
			 * LinkedHashMap
			 * a、accessOrder：ture=访问顺序排序（get/put时排序）/ACCESS-LAST；false=插入顺序排期/FIFO；
			 * b、removeEldestEntry：新增元素时将会调用，返回true时会删除最老元素；可封装LinkedHashMap并重写该方法，比如定义最大容量，超出是返回true即可实现固定长度的LRU算法；
			 */
			lruItem = new LinkedHashMap<String, String>(16, 0.75f, true) {
				@Override
				protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
					if (super.size() > 1000) {
						return true;
					}
					else {
						return false;
					}
				}
			};
			jobLRUMap.putIfAbsent(serviceKey, lruItem);
		}

		// put new
		for (String address : addressSet) {
			if (!lruItem.containsKey(address)) {
				lruItem.put(address, address);
			}
		}
		// remove old
		List<String> delKeys = new ArrayList<>();
		for (String existKey : lruItem.keySet()) {
			if (!addressSet.contains(existKey)) {
				delKeys.add(existKey);
			}
		}
		if (delKeys.size() > 0) {
			for (String delKey : delKeys) {
				lruItem.remove(delKey);
			}
		}

		// load
		String eldestKey = lruItem.entrySet().iterator().next().getKey();
		String eldestValue = lruItem.get(eldestKey);
		return eldestValue;
	}

	@Override
	public String route(String serviceKey, TreeSet<String> addressSet) {
		String finalAddress = doRoute(serviceKey, addressSet);
		return finalAddress;
	}

}
