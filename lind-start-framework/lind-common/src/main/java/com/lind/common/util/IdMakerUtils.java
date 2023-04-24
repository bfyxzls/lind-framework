package com.lind.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 分布式ID生成器,长度9个字节,18个十六进制数. 模仿Mongodb ObjectId生成规则.
 */
public class IdMakerUtils {

	private static final Logger LOG = LoggerFactory.getLogger(IdMakerUtils.class);

	private static final AtomicInteger _nextInc = new AtomicInteger(1);

	/**
	 * 得到当前最大ID偏移量.
	 * @return .
	 */
	public static int getCurrentInc() {
		return _nextInc.get();
	}

	/**
	 * 生成某个服务的ID号.
	 * @param serviceId .
	 * @return .
	 */
	public static String generateId(Integer serviceId) {
		StringBuilder idString = new StringBuilder();

		// 4字节，8个16进制字符，1个十六进制字符=4个二进制字符，1字节=8位=8个二进制字符=2个十六进制字符
		int time = (int) (System.currentTimeMillis() / 1000);
		String timeStr = Integer.toHexString(time);
		idString.append(timeStr);

		// 1字节,2位Hex
		// 后面数字为2^n-1,可以保证结果在ff(0~255)范围内,当serviceId>255，值会定小于255的值，就是说我们的serviceId的取值只有256个
		serviceId = serviceId & 0xFF;
		String serviceIdStr = String.format("%02x", serviceId);
		idString.append(serviceIdStr);

		// 4字节,8位Hex
		int inc = _nextInc.getAndIncrement();
		inc = inc & 0x7fffffff;
		String incString = String.format("%08x", inc);
		idString.append(incString);
		return idString.toString();
	}

	/**
	 * Checks if a string could be an <code>ShardableObjectId</code>.
	 * @return whether the string could be a shardable object id
	 */
	public static boolean isValid(String s) {
		if (s == null) {
			return false;
		}

		final int len = s.length();
		if (len == 18) {
			for (int i = 0; i < len; i++) {
				char c = s.charAt(i);
				if (c >= '0' && c <= '9') {
					continue;
				}
				if (c >= 'a' && c <= 'f') {
					continue;
				}
				if (c >= 'A' && c <= 'F') {
					continue;
				}
				return false;
			}
		}
		else {
			return false;
		}

		return true;
	}

}
