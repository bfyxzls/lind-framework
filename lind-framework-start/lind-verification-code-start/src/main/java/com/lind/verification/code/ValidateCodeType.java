package com.lind.verification.code;

/**
 * 校验码类型
 *
 * @author paascloud.net @gmail.com
 */
public enum ValidateCodeType {

	/**
	 * 短信验证码
	 */
	SMS {
		@Override
		public String getParamNameOnValidate() {
			return CodeConstants.DEFAULT_PARAMETER_NAME_CODE_SMS;
		}
	},
	/**
	 * 图片验证码
	 */
	IMAGE {
		@Override
		public String getParamNameOnValidate() {
			return CodeConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
		}
	},
	/**
	 * 邮箱验证码
	 */
	EMAIL {
		@Override
		public String getParamNameOnValidate() {
			return CodeConstants.DEFAULT_PARAMETER_NAME_CODE_EMAIL;
		}
	},
	/**
	 * 图像流
	 */
	IMAGESTREAM {
		@Override
		public String getParamNameOnValidate() {
			return CodeConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE_STREAM;
		}
	};

	/**
	 * 校验时从请求中获取的参数的名字
	 * @return param name on validate
	 */
	public abstract String getParamNameOnValidate();

}
