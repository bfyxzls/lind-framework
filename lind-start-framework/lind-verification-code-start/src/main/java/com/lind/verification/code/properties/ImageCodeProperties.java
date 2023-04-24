package com.lind.verification.code.properties;

import lombok.Data;

/**
 * 图片验证码配置项
 *
 * @author paascloud.net @gmail.com
 */
@Data
public class ImageCodeProperties {

	/**
	 * 验证码长度
	 */
	private int length = 6;

	/**
	 * 过期时间
	 */
	private int expireIn = 60;

}
