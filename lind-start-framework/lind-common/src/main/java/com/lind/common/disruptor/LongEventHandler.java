package com.lind.common.disruptor;

import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件处理程序
 */
public class LongEventHandler implements EventHandler<LongEvent> {

	Logger logger = LoggerFactory.getLogger(LongEventHandler.class);

	@Override
	public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
		logger.info("LongEventHandler: {}", longEvent);
		Thread.sleep(1000);
	}

}