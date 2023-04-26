package com.lind.common.pattern.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 具体的一个适配器，去适配NewProject这个类.
 */
public class NewProjectAdapter implements Target {

	private static Logger LOG = LoggerFactory.getLogger(NewProjectAdapter.class);

	@Override
	public void request() {
		NewProject newProject = new NewProject();
		LOG.info("Adapter.request");
		newProject.onRequest();
	}

}
