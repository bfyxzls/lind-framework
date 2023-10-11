package com.lind.upms.utils.file;

/**
 * @author lind
 * @date 2023/8/30 13:30
 * @since 1.0.0
 */

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BufferedImageMultipartFile implements MultipartFile {

	private final BufferedImage bufferedImage;

	private final String name;

	private final String originalFilename;

	private final String contentType;

	// 原型扩展名
	private final String fileExt;

	public BufferedImageMultipartFile(BufferedImage bufferedImage, String originalFilename, String contentType) {
		this.bufferedImage = bufferedImage;
		this.name = "image";
		this.originalFilename = originalFilename;
		this.contentType = contentType;
		fileExt = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getOriginalFilename() {
		return originalFilename;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public long getSize() {
		return -1;
	}

	@Override
	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, fileExt, bos);
		return bos.toByteArray();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		byte[] bytes = getBytes();
		return new ByteArrayInputStream(bytes);
	}

	@Override
	public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
		// Transfer the image data to the specified file
		// Implementation depends on your requirements
		ImageIO.write(bufferedImage, fileExt, dest);
	}

}
