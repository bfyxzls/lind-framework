package com.lind.common.bean.property_switch_bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lind
 * @date 2022/10/17 10:02
 * @since 1.0.0
 */
@Data
@ConfigurationProperties("lind.cfg") // 配置属性在配置文件中的前缀
public class LindProperty {

	/**
	 * 设置微信公众号的appid. 配置项：lind.cfg.appId
	 */
	private String appId;

	private Type Type;

	public enum Type {

		ORDER, PRODUCT

	}

}
