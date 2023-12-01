package com.lind.verification.code.sms;

import com.lind.verification.code.common.ValidateCode;
import com.lind.verification.code.common.ValidateCodeGenerator;
import com.lind.verification.code.properties.ValidateCodeProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码生成器
 *
 * @author paascloud.net @gmail.com
 */
public class SmsCodeGenerator implements ValidateCodeGenerator {

	static final Logger logger = LoggerFactory.getLogger(SmsCodeGenerator.class);

	@Autowired
	private ValidateCodeProperties validateCodeProperties;

	/**
	 * Generate validate code.
	 * @param request the request
	 * @return the validate code
	 */
	@Override
	public ValidateCode generate(ServletWebRequest request) {
		String code = RandomStringUtils.randomNumeric(validateCodeProperties.getSms().getLength());
		logger.info(code);
		return new ValidateCode(code, validateCodeProperties.getSms().getExpireIn());
	}

}
