package com.lind.common.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class CommonUtils {

	/**
	 * 平均分配.
	 * @param persons 分配给谁
	 * @param customerObjs 被分配的对象列表
	 */
	public static <T> Map<String, List<T>> allocateAvg(Map<String, Integer> persons, List<T> customerObjs) {
		int personsNum = persons.size();
		if (personsNum == 0) {
			return Maps.newHashMap();
		}
		int customerNum = customerObjs.size();
		Map<String, List<T>> allocations = new HashMap<>();
		List<String> personIds = persons.entrySet().stream().map(stringIntegerEntry -> stringIntegerEntry.getKey())
				.collect(Collectors.toList());
		while (customerNum >= 1) {
			String id = personIds.get(--personsNum);
			List<T> allocatedObjs = allocations.get(id);
			if (CollectionUtils.isEmpty(allocatedObjs)) {
				allocatedObjs = new ArrayList<>();
			}
			allocatedObjs.add(customerObjs.get(--customerNum));
			allocations.put(id, allocatedObjs);
			if (Objects.equals(allocatedObjs.size(), persons.get(id))) {
				personIds.remove(id);
				personsNum--;
			}
			personsNum = personsNum <= 0 ? personIds.size() : personsNum;
		}
		return allocations;
	}

	/**
	 * 获取当前时间秒数.
	 */
	public static String date2utcStr() {
		long miliSeconds = new Date().getTime();
		return String.valueOf(miliSeconds / 1000L);
	}

	/**
	 * 根据当前字符串获取毫秒数.
	 */
	public static String date2utcStr(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = sdf.parse(date);
			long miliSeconds = d.getTime();
			return String.valueOf(miliSeconds / 1000L);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return "error";
	}

	/**
	 * 毫秒数转 LocalDateTime.
	 */
	public static LocalDateTime instant2LocalDateTime(Long time) {
		if (time == null || time == 0) {
			return LocalDateTime.now();
		}
		Date date = new Date(time * 1000);
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		return LocalDateTime.ofInstant(instant, zone);
	}

	/**
	 * 如果传入的时间为null，则默认为最小时间.
	 */
	public static LocalDateTime ifNullToMinTime(Long epochMilli) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		if (epochMilli == null) {
			return LocalDateTime.parse("1970-01-01 00:00:00", df);
		}
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
	}

	/**
	 * 如果传入的时间为null,则默认为最大时间.
	 */
	public static LocalDateTime ifNullToMaxTime(Long epochMilli) {
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		if (epochMilli == null) {
			return LocalDateTime.parse("2999-01-01 00:00:00", df);
		}
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
	}

	/**
	 * 默认字符串.
	 */
	public static String stringNulltoDefault(String value) {
		if (value == null) {
			return "";
		}
		return value;
	}

	/**
	 * 得到一个随机字符串.
	 */
	public static String getRandomString() {
		return UUID.randomUUID().toString();
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
	 * 实际替换动作.
	 */
	public static String replaceStar(String param) {
		if (param == null) {
			return "";
		}
		String userNameAfterReplaced = "";
		int nameLength = param.length();
		if (nameLength < 3 && nameLength > 0) {
			userNameAfterReplaced = param;
		}
		else {
			int num1;
			int num2;
			int num3;
			num2 = (int) Math.ceil(nameLength / 3.0);
			num1 = (int) Math.floor(nameLength / 3.0);
			num3 = nameLength - num1 - num2;
			String star = StringUtils.repeat("*", num2);
			userNameAfterReplaced = param.replaceAll("(.{" + num1 + "})(.{" + num2 + "})(.{" + num3 + "})",
					"$1" + star + "$3");
		}
		return userNameAfterReplaced;
	}

}
