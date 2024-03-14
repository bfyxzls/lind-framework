package com.lind.upload.controller;

import com.lind.common.core.util.file.FileUtils;
import com.lind.common.exception.HttpCodeEnum;
import com.lind.common.rest.CommonResult;
import com.lind.upload.dto.SysFile;
import com.lind.upload.service.ISysFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件请求处理
 *
 * @author ruoyi
 */
@RestController
public class SysFileController {

	private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

	@Autowired
	private ISysFileService sysFileService;

	/**
	 * 文件上传请求
	 */
	@PostMapping("upload")
	public CommonResult<SysFile> upload(MultipartFile file) {
		try {
			// 上传并返回访问地址
			String url = sysFileService.uploadFile(file);
			SysFile sysFile = new SysFile();
			sysFile.setName(FileUtils.getName(url));
			sysFile.setUrl(url);
			return CommonResult.ok(sysFile);
		}
		catch (Exception e) {
			log.error("上传文件失败", e);
			return CommonResult.failure(HttpCodeEnum.ILLEGAL_ARGUMENT, e.getMessage());
		}
	}

}
