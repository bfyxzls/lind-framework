package com.lind.common.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * 配置文件辅助对象.
 */
public class ConfigFileUtils {

	private static final Logger logger = LoggerFactory.getLogger(ConfigFileUtils.class);

	private static ConfigFileUtils instance = null;

	private Properties properties = new Properties();

	private ConfigFileUtils() {
		try {
			// 读取resources/application.properties文件
			InputStream fis = ConfigFileUtils.class.getClassLoader().getResourceAsStream("application.properties");
			properties.load(fis);
			this.printInfo();
		}
		catch (IOException e) {
			logger.info("Manage properties field: " + e.getMessage());
		}
	}

	public static ConfigFileUtils getInstance() {
		if (instance == null) {
			instance = new ConfigFileUtils();
		}
		return instance;
	}

	private void printInfo() {
		Set<Object> propKeySet = properties.keySet();
		for (Object propertyKey : propKeySet) {
			String propValue = properties.getProperty(propertyKey.toString());
			if (propValue != null) {
				System.out.println("--------------------====" + propertyKey.toString() + ":" + propValue);
			}
		}
	}

	public String getStrPropertyValue(String name) {
		String var = System.getenv(name);
		if (var != null) {
			logger.info("System variable, key: " + name + ", value: " + var);
			return var;
		}
		var = properties.getProperty(name);
		logger.info("User variable, key: " + name + ", value: " + var);
		return var;
	}

	public int getIntPropertyValue(String key, int defaultValue) {
		String data = getStrPropertyValue(key);
		try {
			int valor = Integer.parseInt(data);
			return valor;
		}
		catch (Exception e) {
			logger.info("Get Int Property Value field: " + e.getMessage());
			return defaultValue;
		}
	}

}
