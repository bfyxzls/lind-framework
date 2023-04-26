package com.lind.common.disruptor;

/**
 * 事件源对象
 */
public class LongEvent {

	private long value;

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public void set(long value) {
		this.value = value;
	}

}
