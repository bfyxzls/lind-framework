/*
 * Copyright (c) 2018. paascloud.net All Rights Reserved.
 * 项目名称：paascloud快速搭建企业级分布式微服务平台
 * 类名称：ValidateCodeBeanConfig.java
 * 创建人：刘兆明
 * 联系方式：paascloud.net@gmail.com
 * 开源地址: https://github.com/paascloud
 * 博客地址: http://blog.paascloud.net
 * 项目官网: http://paascloud.net
 */

package com.lind.verification.code.common;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.lind.verification.code.email.DefaultEmailCodeSender;
import com.lind.verification.code.email.EmailCodeGenerator;
import com.lind.verification.code.email.EmailCodeSender;
import com.lind.verification.code.image.ImageCodeGenerator;
import com.lind.verification.code.image.ImageStreamCodeGenerator;
import com.lind.verification.code.properties.ValidateCodeProperties;
import com.lind.verification.code.sms.DefaultSmsCodeSender;
import com.lind.verification.code.sms.SmsCodeGenerator;
import com.lind.verification.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Properties;

/**
 * 验证码自动装配
 **/
@Configuration
@EnableConfigurationProperties({ ValidateCodeProperties.class })
public class ValidateCodeBeanConfig {

	@Autowired
	private ValidateCodeProperties validateCodeProperties;

	// @Bean(name = "captchaProducer")
	public DefaultKaptcha getKaptchaBean() {
		DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
		Properties properties = new Properties();
		properties.setProperty("kaptcha.border", "yes");
		properties.setProperty("kaptcha.border.color", "105,179,90");
		properties.setProperty("kaptcha.textproducer.font.color", "blue");
		properties.setProperty("kaptcha.image.width", "125");
		properties.setProperty("kaptcha.image.height", "45");
		properties.setProperty("kaptcha.session.key", "code");
		properties.setProperty("kaptcha.textproducer.char.length", "4");
		properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
		Config config = new Config(properties);
		defaultKaptcha.setConfig(config);
		return defaultKaptcha;
	}

	/**
	 * 图片验证码图片生成器
	 * @return validate code generator
	 */
	@Bean("imageValidateCodeGenerator")
	@Primary
	public ValidateCodeGenerator imageValidateCodeGenerator() {
		ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
		codeGenerator.setSecurityProperties(validateCodeProperties.getImage());
		codeGenerator.setCaptchaProducer(getKaptchaBean());
		return codeGenerator;
	}

	/**
	 * 图片验证码图片生成器
	 * @return validate code generator
	 */
	@Bean("imageStreamValidateCodeGenerator")
	public ValidateCodeGenerator imageStreamValidateCodeGenerator() {
		ImageStreamCodeGenerator codeGenerator = new ImageStreamCodeGenerator();
		codeGenerator.setSecurityProperties(validateCodeProperties.getImage());
		codeGenerator.setCaptchaProducer(getKaptchaBean());
		return codeGenerator;
	}

	@Bean
	public ValidateCodeGenerator smsCodeGenerator() {
		return new SmsCodeGenerator();
	}

	@Bean
	public ValidateCodeGenerator emailCodeGenerator() {
		return new EmailCodeGenerator();
	}

	/**
	 * 短信验证码发送器
	 * @return sms code sender
	 */
	@Bean
	public SmsCodeSender smsCodeSender() {
		return new DefaultSmsCodeSender();
	}

	/**
	 * 邮箱验证码发送器
	 * @return sms code sender
	 */
	@Bean
	public EmailCodeSender emailCodeSender() {
		return new DefaultEmailCodeSender();
	}

}
