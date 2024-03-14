package com.lind.common.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 位运算技巧. 一个数和2^n-1进行`与运算`，结果一定在2^n-1范围之内，并且相邻的数，结果也是相邻的.
 */
public class BinFlagUtils {

	public static final String NUMBER_NEED_2_POWER_MSG = "this parameter need is 2 power,for example (1,2,4,8,16...)";

	/**
	 * 拆分2的N次幂的和，返回2的N次幂列表. 如：9，将返回【1,8】
	 * @param grant
	 * @return
	 */
	public static List<Integer> splitBinPower(Integer grant) {
		return splitBinPower(grant.longValue()).stream().map(Long::intValue).collect(Collectors.toList());
	}

	/**
	 * 拆分2的N次幂的和，返回2的N次幂列表. 如：9，将返回【1,8】
	 * @param grant
	 * @return
	 */
	public static List<Long> splitBinPower(Long grant) {
		List<Long> result = new ArrayList<>();
		if (grant != null && grant > 0) {
			String numStr = Long.toBinaryString(grant);
			StringBuffer bf = new StringBuffer();
			for (int i = 0; i < numStr.length(); i++) {
				if (numStr.charAt(i) != '0') {
					bf.append(numStr.length() - 1 - i);
				}
			}
			int arr[] = new int[bf.length()];
			for (int i = 0; i < bf.length(); i++) {
				arr[i] = bf.charAt(i) - '0';
				result.add((long) Math.pow(2, arr[i]));
			}
		}
		return result;
	}

	/**
	 * 是否为2的幂数.
	 * @param value
	 * @return
	 */
	public static Boolean isTowPower(Long value) {
		return value > 0 && (value & (value - 1)) == 0;
	}

	/**
	 * 是否为2的幂数.
	 * @param value
	 * @return
	 */
	public static Boolean isTowPower(Integer value) {
		return isTowPower(value.longValue());
	}

	/**
	 * 是否包含某个数.
	 * @param value
	 * @return
	 */
	public static Boolean hasValue(Integer source, Integer value) {
		return hasValue(source.longValue(), value.longValue());
	}

	/**
	 * 是否包含某个数.
	 * @param source
	 * @param value
	 * @return
	 */
	public static Boolean hasValue(Long source, Long value) {
		if (!isTowPower(value)) {
			throw new IllegalArgumentException(NUMBER_NEED_2_POWER_MSG);
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
		return increaseValueList(source.longValue(), values.stream().map(Long::valueOf).collect(Collectors.toList()))
				.intValue();
	}

	/**
	 * 列表在原值上累加返回.
	 * @param source
	 * @param values
	 * @return
	 */
	public static Long increaseValueList(Long source, List<Long> values) {
		if (values != null) {
			for (Long o : values) {
				if (!isTowPower(o)) {
					throw new IllegalArgumentException(NUMBER_NEED_2_POWER_MSG);
				}
				source = (source | o);
			}

		}
		return source;
	}

	public static Integer increaseValue(Integer source, Integer value) {
		return increaseValue(source.longValue(), value.longValue()).intValue();
	}

	/**
	 * 在原值上累加某个数.
	 * @param source
	 * @param value
	 * @return
	 */
	public static Long increaseValue(Long source, Long value) {
		return increaseValueList(source, Arrays.asList(value));
	}

	/**
	 * 在原值上累减某个数.
	 * @param source
	 * @param value
	 * @return
	 */
	public static Integer decreaseValue(Integer source, Integer value) {
		return decreaseValue(source.longValue(), value.longValue()).intValue();
	}

	/**
	 * 在原值上累减某个数.
	 * @param source
	 * @param value
	 * @return
	 */
	public static Long decreaseValue(Long source, Long value) {
		if (!isTowPower(value)) {
			throw new IllegalArgumentException(NUMBER_NEED_2_POWER_MSG);
		}
		return source & (~value);
	}

}
