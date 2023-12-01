package com.lind.common.core.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 区域ID的生成，给出一个数字，将它在一个整数区间内生成，并进行相互转化. 0x0000FFFFFFFFFFFFL为这个区间的自增数=它是最大值-初始值的差
 * 0x0000FFFFFFFFFFFFL=((0x0007000000000000-1)|0x0007000000000000)-0x0007000000000000
 *
 * @author lind
 * @date 2023/11/24 10:43
 * @since 1.0.0
 */
public class RangeGenerator {

	private static final Logger logger = LoggerFactory.getLogger(RangeGenerator.class);

	private Long startId;

	private Long maxRangeId;

	public RangeGenerator(Long startId) {
		this.startId = startId;
		this.maxRangeId = (startId - 1 | startId) - startId;
		logger.debug("startId:{},maxRangeId:{}", startId, maxRangeId);
	}

	/**
	 * 通过ID得到rangeId.
	 * @param id
	 * @return
	 */
	public Long getRangeId(Long id) {
		return id | startId;
	}

	/**
	 * 通过rangeId得到ID.
	 * @param rangeId
	 * @return
	 */
	public Long getId(Long rangeId) {
		return rangeId & maxRangeId;
	}

}
