/*
 * Copyright (c) 2018. paascloud.net All Rights Reserved.
 * 项目名称：paascloud快速搭建企业级分布式微服务平台
 * 类名称：ImageCodeProcessor.java
 * 创建人：刘兆明
 * 联系方式：paascloud.net@gmail.com
 * 开源地址: https://github.com/paascloud
 * 博客地址: http://blog.paascloud.net
 * 项目官网: http://paascloud.net
 */

package com.lind.verification.code.image;

import com.lind.verification.code.ValidateCodeGenerator;
import com.lind.verification.code.ValidateCodeRepository;
import com.lind.verification.code.impl.AbstractValidateCodeProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 图片验证码处理器
 */
@Component("imagestreamValidateCodeProcessor")
public class ImageStreamCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

	/**
	 * Instantiates a new Abstract validate code processor.
	 * @param validateCodeGenerators the validate code generators
	 * @param validateCodeRepository the validate code repository
	 */
	public ImageStreamCodeProcessor(Map<String, ValidateCodeGenerator> validateCodeGenerators,
			ValidateCodeRepository validateCodeRepository) {
		super(validateCodeGenerators, validateCodeRepository);
	}

	/**
	 * 发送图形验证码，将其写到响应中
	 * @param request the request
	 * @param imageCode the image code
	 * @throws Exception the exception
	 */
	@Override
	protected void send(ServletWebRequest request, ImageCode imageCode) throws Exception {
		HttpServletResponse response = request.getResponse();
		response.setContentType("image/jpeg");// 等同于response.setHeader("Content-Type",
												// "image/jpeg");
		response.setDateHeader("expries", -1);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
	}

}
