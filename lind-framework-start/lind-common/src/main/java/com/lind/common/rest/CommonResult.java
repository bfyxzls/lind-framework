package com.lind.common.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lind.common.exception.HttpCodeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * 通用返回结果. 服务端：500 客户认证和授权端：未认证401，权限不足403 业务型异常：400
 **/
@Getter
@Setter
public class CommonResult<T> implements Serializable {

	private static final long serialVersionUID = 1573835682597272725L;

	/**
	 * 业务错误码.
	 */
	private String code;

	private String message;

	/**
	 * 数据.
	 */
	private T data;

	private CommonResult() {
	}

	private CommonResult(String code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	/**
	 * 根据状态码和消息构建结果.
	 */
	public static <T> CommonResult<T> build(String code, String msg, T data) {
		return new CommonResult<>(code, msg, data);
	}

	/**
	 * 成功返回.
	 */
	public static CommonResult<Object> ok() {
		return ok(null);
	}

	/**
	 * 成功返回并带相应数据.
	 */
	public static <T> CommonResult<T> ok(T data) {
		HttpCodeEnum success = HttpCodeEnum.SUCCESS;
		return new CommonResult<T>(success.getCode(), success.getMessage(), data);
	}

	/**
	 * fail.
	 */
	public static <T> CommonResult<T> failure() {
		CommonResult<T> result = new CommonResult<>();
		result.code = HttpCodeEnum.FAILURE.getCode();
		result.message = HttpCodeEnum.FAILURE.getMessage();

		return result;
	}

	/**
	 * failure.
	 **/
	public static <T> CommonResult<T> failure(HttpCodeEnum httpCodeEnum) {
		CommonResult<T> result = new CommonResult<>();
		result.code = httpCodeEnum.getCode();
		result.message = httpCodeEnum.getMessage();
		return result;
	}

	/**
	 * failure.
	 **/
	public static <T> CommonResult<T> failure(HttpCodeEnum httpCodeEnum, String message) {
		CommonResult<T> result = new CommonResult<>();
		result.code = httpCodeEnum.getCode();
		result.message = message;
		return result;
	}

	/**
	 * 失败无响应数据自定义状态码及消息.
	 */
	public static <T> CommonResult<T> failure(String code, String message) {
		CommonResult<T> result = new CommonResult<>();
		result.code = code;
		result.message = message;

		return result;
	}

	/**
	 * 服务端异常.
	 **/
	public static <T> CommonResult<T> serverFailure(String message) {
		CommonResult<T> result = new CommonResult<>();
		result.code = HttpCodeEnum.INTERNAL_SERVER_ERROR.getCode();
		result.message = message;
		return result;
	}

	/**
	 * clientFailure.
	 */
	public static <T> CommonResult<T> clientFailure(String message) {
		CommonResult<T> result = new CommonResult<>();
		result.code = HttpCodeEnum.ILLEGAL_ARGUMENT.getCode();
		result.message = message;
		return result;
	}

	/**
	 * clientFailure.
	 */
	public static <T> CommonResult<T> clientFailure(String message, String code) {
		CommonResult<T> result = new CommonResult<>();
		result.code = code;
		result.message = message;
		return result;
	}

	/**
	 * 权限不足，返回码403
	 * @param message
	 * @param <T>
	 * @return
	 */
	public static <T> CommonResult<T> forbiddenFailure(@Nullable String message) {
		return failure(HttpCodeEnum.FORBIDDEN.getCode(), message);
	}

	/**
	 * 未认证，返回码401
	 * @param <T>
	 * @return
	 */
	public static <T> CommonResult<T> unauthorizedFailure(@Nullable String message) {
		return failure(HttpCodeEnum.UNAUTHORIZED.getCode(), message);
	}

	/**
	 * 返回数据是否成功.
	 */
	@JsonIgnore
	public boolean isSuccess() {
		return HttpCodeEnum.SUCCESS.getCode().equals(this.code);
	}

	/**
	 * 判断是否包含数据.
	 */
	public boolean hasData() {
		return Objects.nonNull(data);
	}

}
