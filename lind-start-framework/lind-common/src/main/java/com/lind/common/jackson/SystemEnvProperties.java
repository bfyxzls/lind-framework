package com.lind.common.jackson;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class SystemEnvProperties extends Properties {

	private final Map<String, String> overrides;

	public SystemEnvProperties(Map<String, String> overrides) {
		this.overrides = overrides;
	}

	public SystemEnvProperties() {
		this.overrides = Collections.EMPTY_MAP;
	}

	@Override
	public String getProperty(String key) {
		if (overrides.containsKey(key)) {
			return overrides.get(key);
		}
		else if (key.startsWith("env.")) {
			return System.getenv().get(key.substring(4));
		}
		else {
			return System.getProperty(key);
		}
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		return value != null ? value : defaultValue;
	}

}
