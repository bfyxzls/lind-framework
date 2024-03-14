package com.lind.upload.execption;

import com.lind.common.core.exception.BaseException;

/**
 * 文件信息异常类
 *
 * @author ruoyi
 */
public class FileException extends BaseException {

	private static final long serialVersionUID = 1L;

	public FileException(String code, Object[] args, String msg) {
		super("file", code, args, msg);
	}

}
