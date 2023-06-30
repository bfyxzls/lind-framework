package com.lind.common.bean.property_switch_bean;

import com.lind.common.bean.property_switch_bean.service.Lind;
import com.lind.common.bean.property_switch_bean.service.LindOrder;
import com.lind.common.bean.property_switch_bean.service.LindProduct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lind
 * @date 2022/10/17 10:05
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(LindProperty.class) // 开启@ConfigurationProperties
public class LindAutoConfig {

	@Bean
	@ConditionalOnMissingBean // lind的Bean的默认实现行为
	public Lind lind(LindProperty lindProperty) {
		LindProperty.Type type = lindProperty.getType();
		Lind lind;
		switch (type) {
			case ORDER:
				lind = new LindOrder();
				break;
			case PRODUCT:
				lind = new LindProduct();
				break;
			default:
				throw new IllegalArgumentException("not support type");

		}
		return lind;
	}

}
