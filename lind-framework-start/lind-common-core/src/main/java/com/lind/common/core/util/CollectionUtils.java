/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lind.common.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 集合工具类，会有一些不错的算法.
 */
public class CollectionUtils {

	public static String join(Collection<String> strings) {
		return join(strings, ",");
	}

	public static String join(Collection<String> strings, String separator) {
		Iterator<String> iter = strings.iterator();
		StringBuilder sb = new StringBuilder();
		if (iter.hasNext()) {
			sb.append(iter.next());
			while (iter.hasNext()) {
				sb.append(separator).append(iter.next());
			}
		}
		return sb.toString();
	}

	// Return true if all items from col1 are in col2 and viceversa. Order is not taken
	// into account
	public static <T> boolean collectionEquals(Collection<T> col1, Collection<T> col2) {
		if (col1.size() != col2.size()) {
			return false;
		}

		for (T item : col1) {
			if (!col2.contains(item)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 拆分集合.
	 * @param <T> .
	 * @param resList 要拆分的集合
	 * @param count 每个集合的元素个数
	 * @return 返回拆分后的各个集合
	 */
	public static <T> List<List<T>> split(List<T> resList, int count) {

		if (resList == null || count < 1) {
			return null;
		}
		List<List<T>> ret = new ArrayList<>();
		int size = resList.size();
		if (size <= count) { // 数据量不足count指定的大小
			ret.add(resList);
		}
		else {
			int pre = size / count;
			int last = size % count;
			// 前面pre个集合，每个大小都是count个元素
			for (int i = 0; i < pre; i++) {
				List<T> itemList = new ArrayList<T>();
				for (int j = 0; j < count; j++) {
					itemList.add(resList.get(i * count + j));
				}
				ret.add(itemList);
			}
			// last的进行处理
			if (last > 0) {
				List<T> itemList = new ArrayList<T>();
				for (int i = 0; i < last; i++) {
					itemList.add(resList.get(pre * count + i));
				}
				ret.add(itemList);
			}
		}
		return ret;

	}

	/**
	 * 平均分配集合.
	 * @param destination 分配给谁
	 * @param source 被分配的对象列表
	 */
	public static <T> Map<String, List<T>> allocateAvg(Map<String, Integer> destination, List<T> source) {
		int desLen = destination.size();
		if (desLen == 0) {
			return new HashMap<>();
		}
		int customerNum = source.size();
		Map<String, List<T>> allocations = new HashMap<>();
		List<String> personIds = destination.entrySet().stream().map(stringIntegerEntry -> stringIntegerEntry.getKey())
				.collect(Collectors.toList());
		while (customerNum >= 1) {
			String id = personIds.get(--desLen);
			List<T> allocatedObjs = allocations.get(id);
			if (org.springframework.util.CollectionUtils.isEmpty(allocatedObjs)) {
				allocatedObjs = new ArrayList<>();
			}
			allocatedObjs.add(source.get(--customerNum));
			allocations.put(id, allocatedObjs);
			if (Objects.equals(allocatedObjs.size(), destination.get(id))) {
				personIds.remove(id);
				desLen--;
			}
			desLen = desLen <= 0 ? personIds.size() : desLen;
		}
		return allocations;
	}

}
