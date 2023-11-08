package com.lind.common.env;

import com.lind.common.core.util.StringUtils;
import com.lind.common.encrypt.AESNetUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.util.HashMap;

/**
 * 处理配置中的敏感信息，读配置时完成解密操作.
 *
 * @author lind
 * @date 2023/10/9 8:33
 * @since 1.0.0
 */
public class MpwEnvironmentPostProcessor implements EnvironmentPostProcessor {

	static final String MPW = "mpw.key";
	static final String MPW_PREFIX = "mpw:";

	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String mpwKey = null;
		for (PropertySource<?> ps : environment.getPropertySources()) {
			if (ps instanceof SimpleCommandLinePropertySource) { // jvm启动时命令行参数
				SimpleCommandLinePropertySource source = (SimpleCommandLinePropertySource) ps;
				mpwKey = source.getProperty(MPW);
				break;
			}
			if (ps instanceof OriginTrackedMapPropertySource) {// application.yml
				OriginTrackedMapPropertySource source = (OriginTrackedMapPropertySource) ps;
				if (source.containsProperty(MPW))
					mpwKey = source.getProperty(MPW).toString();
				break;
			}
		}

		if (StringUtils.isNotBlank(mpwKey)) {
			HashMap<String, Object> map = new HashMap<>();
			for (PropertySource<?> ps : environment.getPropertySources()) {
				if (ps instanceof OriginTrackedMapPropertySource) {
					OriginTrackedMapPropertySource source = (OriginTrackedMapPropertySource) ps;
					for (String name : source.getPropertyNames()) {
						Object value = source.getProperty(name);
						if (value instanceof String) {
							String str = (String) value;
							if (str.startsWith(MPW_PREFIX)) {
								String decrypt = AESNetUtils.decrypt(str.substring(MPW_PREFIX.length()), mpwKey);
								map.put(name, decrypt);

							}
						}
					}
				}
			}

			if (MapUtils.isNotEmpty(map))
				environment.getPropertySources().addFirst(new MapPropertySource("custom-encrypt", map));
		}
	}

}
