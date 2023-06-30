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
public class BlackHandler extends AbstractHandler<Request> {

	private static final Logger logger = LoggerFactory.getLogger(CouponHandler.class);

	public BlackHandler() {
		this.setSortNumber(1);
	}

	@Override
	public void execute(Request body) {
		logger.info("BlackHandler 处理");
	}

}
