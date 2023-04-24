package com.lind.common.pattern.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 具体对目标的实现.
 */
public class ConcreteTarget implements Target {

	private static Logger LOG = LoggerFactory.getLogger(ConcreteTarget.class);

	@Override
	public void request() {
		LOG.info("ConcreteTarget.request()");
	}

}
