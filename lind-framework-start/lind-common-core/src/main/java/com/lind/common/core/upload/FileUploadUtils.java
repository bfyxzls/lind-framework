package com.lind.common.core.upload;

import com.lind.common.core.id.Seq;
import com.lind.common.core.util.DateUtils;
import com.lind.common.core.util.StringUtils;
import com.lind.common.core.util.file.MimeTypeUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 文件上传工具类
 *
 * @author ruoyi
 */
@Component
public class FileUploadUtils {

	// 默认大小 50M
	public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

	// 默认的文件名最大长度 100
	public static final int DEFAULT_FILE_NAME_LENGTH = 100;

	final static String[] DEFAULT_ALLOWED_EXTENSION = { "jpg", "jpeg", "png", "gif" };

	private static final String RESOURCE_PREFIX = "/profile";

	// 设置目标缩放尺寸-宽度
	@Value("${upload.targetWidth:144}")
	private int targetWidth;

	@Value("${upload.targetHeight:144}")
	private int targetHeight;

	// 默认上传的地址
	@Value("${upload.defaultBaseDir:d:/upload}")
	private String defaultBaseDir;

	/**
	 * 编码文件名
	 */
	public static final String extractFilename(MultipartFile file) {
		return String.format("%s/%s_%s.%s", DateUtils.datePath(), FilenameUtils.getBaseName(file.getOriginalFilename()),
				Seq.getId(Seq.uploadSeqType), getExtension(file));
	}

	public static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException {
		File desc = new File(uploadDir + File.separator + fileName);

		if (!desc.exists()) {
			if (!desc.getParentFile().exists()) {
				desc.getParentFile().mkdirs();
			}
		}
		return desc;
	}

	/**
	 * 文件大小校验
	 * @param file 上传的文件
	 * @return
	 */
	public static final void assertAllowed(MultipartFile file, String[] allowedExtension) {
		long size = file.getSize();
		if (size > DEFAULT_MAX_SIZE) {
			throw new IllegalArgumentException(String.format("超过上传文件大小限制:%s", DEFAULT_MAX_SIZE / 1024 / 1024));
		}

		String fileName = file.getOriginalFilename();
		String extension = getExtension(file);
		if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
			throw new IllegalArgumentException("文件格式不支持");
		}
	}

	/**
	 * 判断MIME类型是否是允许的MIME类型
	 * @param extension
	 * @param allowedExtension
	 * @return
	 */
	public static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
		for (String str : allowedExtension) {
			if (str.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取文件名的后缀
	 * @param file 表单文件
	 * @return 后缀名
	 */
	public static final String getExtension(MultipartFile file) {
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		if (StringUtils.isEmpty(extension)) {
			extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
		}
		return extension;
	}

	/**
	 * 图像缩放.
	 * @param imageData
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 * @throws IOException
	 */
	private static BufferedImage createResizedImage(InputStream imageData, int targetWidth, int targetHeight)
			throws IOException {
		return Thumbnails.of(imageData).size(targetWidth, targetHeight).asBufferedImage();
	}

	/**
	 * 根据文件路径上传
	 * @param baseDir 相对应用的基目录
	 * @param file 上传的文件
	 * @return 文件名称
	 * @throws IOException
	 */
	public String upload(String baseDir, MultipartFile file) throws IOException {
		try {
			return upload(baseDir, file, DEFAULT_ALLOWED_EXTENSION);
		}
		catch (Exception e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	/**
	 * 文件上传
	 * @param baseDir
	 * @param file
	 * @param allowedExtension
	 * @return
	 */
	public String upload(String baseDir, MultipartFile file, String[] allowedExtension) throws IOException {
		int fileNamelength = Objects.requireNonNull(file.getOriginalFilename()).length();
		if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
			throw new IllegalArgumentException("文件名超长" + FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
		}

		assertAllowed(file, allowedExtension);

		// 缩放
		if (file.getContentType().contains("png") || file.getContentType().contains("jpeg")) {
			BufferedImage resizedImage = createResizedImage(file.getInputStream(), targetWidth, targetHeight);
			file = new BufferedImageMultipartFile(resizedImage, file.getOriginalFilename(), file.getContentType());
		}

		String fileName = extractFilename(file);

		String absPath = getAbsoluteFile(baseDir, fileName).getAbsolutePath();
		file.transferTo(Paths.get(absPath));
		return getPathFileName(baseDir, fileName);
	}

	public final String getPathFileName(String uploadDir, String fileName) throws IOException {
		int dirLastIndex = defaultBaseDir.length() + 1;
		String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
		return RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
	}

	/**
	 * 以默认配置进行文件上传
	 * @param file 上传的文件
	 * @return 文件名称
	 * @throws Exception
	 */
	public final String upload(MultipartFile file) throws IOException {
		try {
			return upload(defaultBaseDir, file, DEFAULT_ALLOWED_EXTENSION);
		}
		catch (Exception e) {
			throw new IOException(e.getMessage(), e);
		}
	}

}
