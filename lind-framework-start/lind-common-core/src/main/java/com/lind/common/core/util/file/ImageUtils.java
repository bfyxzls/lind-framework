package com.lind.common.core.util.file;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import static com.lind.common.core.util.file.FileUtils.closeQuietly;

/**
 * 图片处理工具类
 *
 * @author ruoyi
 */
public class ImageUtils {

	private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

	public static byte[] convertInputStreamToByteArray(InputStream inputStream) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int bytesRead;

		while (true) {
			try {
				if (!((bytesRead = inputStream.read(buffer)) != -1))
					break;
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
			baos.write(buffer, 0, bytesRead);
		}

		return baos.toByteArray();
	}

	public static byte[] getImage(String imagePath) {
		InputStream is = getFile(imagePath);
		try {
			return convertInputStreamToByteArray(is);
		}
		catch (Exception e) {
			log.error("图片加载异常 {}", e);
			return null;
		}
		finally {
			closeQuietly(is);
		}
	}

	public static InputStream getFile(String imagePath) {
		try {
			byte[] result = readFile(imagePath);
			result = Arrays.copyOf(result, result.length);
			return new ByteArrayInputStream(result);
		}
		catch (Exception e) {
			log.error("获取图片异常 {}", e);
		}
		return null;
	}

	/**
	 * 读取文件为字节数据
	 * @param url 地址
	 * @return 字节数据
	 */
	public static byte[] readFile(String url) {
		InputStream in = null;
		try {
			// 网络地址
			URL urlObj = new URL(url);
			URLConnection urlConnection = urlObj.openConnection();
			urlConnection.setConnectTimeout(30 * 1000);
			urlConnection.setReadTimeout(60 * 1000);
			urlConnection.setDoInput(true);
			in = urlConnection.getInputStream();
			return convertInputStreamToByteArray(in);
		}
		catch (Exception e) {
			log.error("访问文件异常 {}", e);
			return null;
		}
		finally {
			closeQuietly(in);
		}
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

}
