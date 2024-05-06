package com.lind.common.websecurity;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lind
 * @date 2024/5/6 9:47
 * @since 1.0.0
 */
@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<XFrameOptionsFilter> xFrameOptions() {
		FilterRegistrationBean<XFrameOptionsFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new XFrameOptionsFilter());
		registrationBean.addUrlPatterns("/*"); // 设置过滤的URL模式
		registrationBean.setOrder(0); // 设置过滤器顺序
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<CspFilter> cspFilter() {
		FilterRegistrationBean<CspFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new CspFilter());
		registrationBean.addUrlPatterns("/*"); // 设置过滤的URL模式
		registrationBean.setOrder(1); // 设置过滤器顺序，越小优先级越高
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<XssProtectionFilter> xssProtectionFilter() {
		FilterRegistrationBean<XssProtectionFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new XssProtectionFilter());
		registrationBean.addUrlPatterns("/*"); // 设置过滤的URL模式
		registrationBean.setOrder(2); // 设置过滤器顺序
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<HSTSFilter> hstsFilter() {
		FilterRegistrationBean<HSTSFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new HSTSFilter());
		registrationBean.addUrlPatterns("/*"); // 设置过滤的URL模式
		registrationBean.setOrder(3); // 设置过滤器顺序
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<XRobotsTagFilter> xRobotsTagFilter() {
		FilterRegistrationBean<XRobotsTagFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new XRobotsTagFilter());
		registrationBean.addUrlPatterns("/*"); // 设置过滤的URL模式
		registrationBean.setOrder(4); // 设置过滤器顺序，越小优先级越高
		return registrationBean;
	}

}
