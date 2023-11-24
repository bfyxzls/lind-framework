package com.lind.common.core.id;

/**
 * 区域ID的生成，给出一个数字，将它在一个整数区间内生成，并进行相互转化. 0x0000FFFFFFFFFFFFL为这个区间的自增数=它是最大值-初始值的差
 * 0x0000FFFFFFFFFFFFL=((0x0007000000000000-1)|0x0007000000000000)-0x0007000000000000
 *
 * @author lind
 * @date 2023/11/24 10:43
 * @since 1.0.0
 */
public class RangeGenerator {

	/**
	 * 通过ID得到rangeId.
	 * @param id
	 * @return
	 */
	public static Long getRangeId(Long id) {
		return id | 0x0007000000000000L;
	}

	/**
	 * 通过rangeId得到ID.
	 * @param rangeId
	 * @return
	 */
	public static Long getId(Long rangeId) {
		return rangeId & 0x0000FFFFFFFFFFFFL;
	}

}
