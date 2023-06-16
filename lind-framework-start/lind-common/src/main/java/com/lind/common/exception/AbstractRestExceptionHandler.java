package com.lind.common.exception;

import com.lind.common.rest.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * 全局异常拦截器. 如果某个模块需要,直接继承然后加入@RestControllerAdvice即可
 */
@Slf4j
public class AbstractRestExceptionHandler {

	/**
	 * Exception 服务端异常500.
	 * @param e 异常
	 * @return CommonResult
	 */
	@ExceptionHandler({ Exception.class, RuntimeException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public CommonResult handleException(Exception e) {
		String message = "系统内部异常";
		log.error(message, e);
		return CommonResult.serverFailure(message);
	}

	/**
	 * 统一处理请求参数校验(普通传参400).
	 * @param e ConstraintViolationException
	 * @return CommonResult
	 */
	@ExceptionHandler(value = ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResult handleConstraintViolationException(ConstraintViolationException e) {
		StringBuilder message = new StringBuilder();
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		for (ConstraintViolation<?> violation : violations) {
			Path path = violation.getPropertyPath();
			String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
			message.append(pathArr[1]).append(violation.getMessage()).append(",");
		}
		message = new StringBuilder(message.substring(0, message.length() - 1));
		log.error(message.toString());
		return CommonResult.clientFailure(message.toString());
	}

	/**
	 * 统一处理请求参数校验(json)400.
	 * @param e ConstraintViolationException
	 * @return CommonResult
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResult handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		StringBuilder message = new StringBuilder();
		for (FieldError error : e.getBindingResult().getFieldErrors()) {
			message.append(error.getField()).append(error.getDefaultMessage()).append(",");
		}
		message = new StringBuilder(message.substring(0, message.length() - 1));
		log.error(message.toString());
		return CommonResult.clientFailure(message.toString());
	}

	/**
	 * LindException业务型异常.
	 * @param lindException 业务型异常
	 * @return CommonResult
	 */
	@ExceptionHandler(value = LindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResult businessErrorException(LindException lindException) {
		if (lindException.getCode() != null)
			return CommonResult.clientFailure(lindException.getMessage(), lindException.getCode());
		return CommonResult.clientFailure(lindException.getMessage());
	}

	/**
	 * NoSuchElementException.
	 * @param e 异常
	 * @return CommonResult
	 */
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResult handlerNoSuchElementException(NoSuchElementException e) {
		String message = e.getMessage();
		log.error(message);
		return CommonResult.clientFailure(message);
	}

	/**
	 * IllegalArgumentException.
	 * @param e 异常
	 * @return CommonResult
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResult<String> handlerIllegalArgumentException(IllegalArgumentException e) {
		String message = e.getMessage();
		log.error(message);
		return CommonResult.clientFailure(message);
	}

}
