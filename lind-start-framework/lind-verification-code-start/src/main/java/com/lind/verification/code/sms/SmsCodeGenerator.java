package com.lind.verification.code.sms;

import com.lind.verification.code.ValidateCode;
import com.lind.verification.code.ValidateCodeGenerator;
import com.lind.verification.code.properties.ValidateCodeProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码生成器
 *
 * @author paascloud.net @gmail.com
 */
@Component("smsValidateCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

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
		return new ValidateCode(code, validateCodeProperties.getSms().getExpireIn());
	}

}
