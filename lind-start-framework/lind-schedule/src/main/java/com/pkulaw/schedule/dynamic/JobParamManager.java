package com.pkulaw.schedule.dynamic;

/**
 * @author Shengzhao Li
 */
public abstract class JobParamManager {

	private static final String MONITORING_INSTANCE_JOB_NAME_PREFIX = "monitoring-instance-";

	protected JobParamManager() {
	}

	public static String generateMonitoringInstanceJobName(String key) {
		return MONITORING_INSTANCE_JOB_NAME_PREFIX + key;
	}

}
