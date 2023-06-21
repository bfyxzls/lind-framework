package com.lind.common.core.util;

import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 掩码计算 将一个数字inc，放到一个长整形的区间中，区间起始值为mask，通过或运算【inc|mask】得到在这个区间里的值rangeInc.
 * 将这个区间里最大值max【(mask-1)|mask】，去减去起始值mask，得到这个区间所能存放的inc最大值range【max-inc】，然后通过并算，可以得到inc【rangeInc
 * & range】
 *
 * @author lind
 * @date 2022/11/17 14:05
 * @since 1.0.0
 */
public class MaskUtil {

	org.slf4j.Logger logger = LoggerFactory.getLogger(MaskUtil.class);

	private Long mask;

	private Long max;

	private Long range;

	/**
	 * 掩码值.
	 * @param mask
	 */
	public MaskUtil(Long mask) {
		this.mask = mask;
		this.max = (mask - 1) | mask;
		this.range = max - mask;
		logger.info("init:{},max:{},range:{}", mask, max, range);
	}

	/**
	 * 通过inc得到RangeInc
	 * @param inc
	 * @return
	 */
	public Long toRangeInc(Long inc) {
		Assert.isTrue(inc < range, String.format("您的inc变量已经超出范围，最大为%s", range - 1));
		return inc | mask;
	}

	/**
	 * 通过RangeInc得到inc
	 * @param rangeInc
	 * @return
	 */
	public Long toInc(Long rangeInc) {
		return rangeInc & range;
	}

}
