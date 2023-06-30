package com.lind.common.pattern.pipeline.impl;

import com.lind.common.pattern.pipeline.AbstractHandler;
import com.lind.common.pattern.pipeline.Request;
import com.lind.common.pattern.chinaorder.handler.CouponHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author lind
 * @date 2023/6/28 17:41
 * @since 1.0.0
 */
@Component
public class RateLimitHandler extends AbstractHandler<Request> {

	private static final Logger logger = LoggerFactory.getLogger(CouponHandler.class);

	public RateLimitHandler() {
		this.setSortNumber(2);
	}

	@Override
	protected void execute(Request body) {
		logger.info("RateLimitHandler 处理");

	}

}
