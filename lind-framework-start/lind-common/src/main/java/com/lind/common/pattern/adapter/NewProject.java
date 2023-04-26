package com.lind.common.pattern.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 新的项目，它希望作为Service.doSomethings方法，而人家方法参数是Target实例，所以它需要有个适合器.
 */
public class NewProject {

	private static Logger LOGGER = LoggerFactory.getLogger(ConcreteTarget.class);

	public void onRequest() {
		LOGGER.info("new.onRequest()");
	}

}
