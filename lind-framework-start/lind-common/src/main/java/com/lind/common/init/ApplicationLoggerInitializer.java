/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lind.common.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author lengleng
 * @date 2019-06-25
 * <p>
 * 通过环境变量的形式注入 logging.file 自动维护 Spring Boot Admin Logger Viewer
 */
public class ApplicationLoggerInitializer implements EnvironmentPostProcessor, Ordered {

	/**
	 * 该接口一般用于读取配置信息，包括系统环境变量配置、启动时命令参数配置等等，可以根据配置选择使用对应环境的配置(有多套环境配置文件)，
	 * 一般来说我们可以根据环境变量或启动参数中的配置，确定本项目使用的配置文件，然后读取配置文件将配置信息存入System.setProperty(key,value)中，
	 * 然后在需要读取(使用)配置的位置，使用System.getProperty(key)来获取value，这样就可以达到不同的环境使用不同的配置的目的。
	 * 例如：我们可以在启动命令参数或环境变量中配置nacos的server地址：REGISTRY_URL=xxx.xxx.xx.xx:8848,然后使用environment.getProperty("REGISTRY_URL")
	 * 获取值，然后使用System.setProperties()设置到系统变量中去,使用@ConfigurationProperties注解可以读取系统变量中的配置，
	 * 所以NacosConfigProperties类就可以读取到我们设置的nacos的地址，然后读取nacos配置中心中配置的yml文件，从而实现不同的环境使用不同的nacos配置和注册中心。
	 * System.setProperty()设置的配置，也可以在yml文件中使用${key}来获取。
	 * @param environment
	 * @param application
	 */
	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String appName = environment.getProperty("spring.application.name");
		String logBase = environment.getProperty("LOGGING_PATH", "logs");

		// spring boot admin 直接加载日志
		System.setProperty("logging.file.name", String.format("%s/%s/debug.log", logBase, appName));
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
