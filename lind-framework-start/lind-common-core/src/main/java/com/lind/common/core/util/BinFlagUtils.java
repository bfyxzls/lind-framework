package com.lind.common.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 位运算技巧. 一个数和s^n-1进行`与运算`，结果一定在2^n-1范围之内，并且相邻的数，结果也是相邻的.
 */
public class BinFlagUtils {

	/**
	 * 拆分2的N次幂的和，返回2的N次幂列表. 如：9，将返回【1,8】
	 * @param grant
	 * @return
	 */
	public static List<Integer> splitBinPower(Integer grant) {
		List<Integer> result = new ArrayList<>();
		if (grant != null && grant > 0) {
			String numStr = Integer.toBinaryString(grant);
			StringBuffer bf = new StringBuffer();
			for (int i = 0; i < numStr.length(); i++) {
				if (numStr.charAt(i) != '0') {
					bf.append(numStr.length() - 1 - i);
				}
			}
			int arr[] = new int[bf.length()];
			for (int i = 0; i < bf.length(); i++) {
				arr[i] = bf.charAt(i) - '0';
				result.add((int) Math.pow(2, arr[i]));
			}
		}
		return result;
	}

	/**
	 * 是否为2的幂数.
	 * @param value
	 * @return
	 */
	public static Boolean isTowPower(Integer value) {
		return value > 0 && (value & (value - 1)) == 0;
	}

	/**
	 * 是否包含某个数.
	 * @param value
	 * @return
	 */
	public static Boolean hasValue(Integer source, Integer value) {
		if (!isTowPower(value)) {
			throw new IllegalArgumentException("button grant number need 2 power(1,2,4,8,16...)");
		}
		return (source & value) > 0;
	}

	/**
	 * 列表累加后结果返回.
	 * @param values
	 * @return
	 */
	public static Integer increaseValueList(List<Integer> values) {
		return increaseValueList(0, values);
	}

	/**
	 * 列表在原值上累加返回.
	 * @param source
	 * @param values
	 * @return
	 */
	public static Integer increaseValueList(Integer source, List<Integer> values) {
		if (values != null) {
			for (Integer o : values) {
				if (!isTowPower(o)) {
					throw new IllegalArgumentException("button grant number need 2 power(1,2,4,8,16...)");
				}
				source = (source | o);
			}

		}
		return source;
	}

	/**
	 * 减去某个数.
	 * @param source
	 * @param value
	 * @return
	 */
	public static Integer decreaseValue(Integer source, Integer value) {
		if (!isTowPower(value)) {
			throw new IllegalArgumentException("button grant number need 2 power(1,2,4,8,16...)");
		}
		return source & (~value);
	}

}
