package com.lind.common.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 事件源生产工厂
 */
public class LongEventFactory implements EventFactory<LongEvent> {

	@Override
	public LongEvent newInstance() {
		return new LongEvent();
	}

}