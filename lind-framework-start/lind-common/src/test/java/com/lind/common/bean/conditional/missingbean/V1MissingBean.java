package com.lind.common.bean.conditional.missingbean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 如果MissingBean有默认的实现，通过@ConditionalOnMissingBean(MissingBean.class)的话，则V1MissingBean就不能使用@Bean的方式进行注入了
 * 通过@Bean再注入一个V1MissingBean，spring会找到两个实现，这时，需要将它声明为@Primary才行，如果通过@Component声明，就是正常的。
 * 通过@ConditionalOnProperty注解来控制它，可以通过配置属性来装配它，做到default和v1的切换.
 */
@Component
@ConditionalOnProperty(value = "test.v1.enabled", matchIfMissing = true)
public class V1MissingBean implements MissingBean {

	@Override
	public void hello() {
		System.out.println("v1 MissingBean");
	}

}
