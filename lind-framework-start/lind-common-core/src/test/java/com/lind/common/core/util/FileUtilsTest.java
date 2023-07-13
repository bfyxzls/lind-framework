package com.lind.common.core.util;

import lombok.SneakyThrows;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class FileUtilsTest {

	private static final Logger logger = LoggerFactory.getLogger(FileUtilsTest.class);
	static String path = "D:\\tools\\jdk8-windowsx64.zip";
	static String pathObj = "D:\\jar.zip";

	private final AtomicLong maxFileId = new AtomicLong();

	@SneakyThrows
	@Test
	public void bio() {
		StopWatch stopwatch = StopWatch.createStarted();
		byte[] buffer = FileUtils.readResourceByteArray(path);
		FileUtils.writeResourceFromByteArrayNIO(pathObj, buffer);
		stopwatch.stop();
		System.out.println(stopwatch.getTime(TimeUnit.MILLISECONDS) + "ms");
	}

	@SneakyThrows
	@Test
	public void nio() {
		StopWatch stopwatch = StopWatch.createStarted();
		byte[] buffer = FileUtils.readResourceByteArrayNIO(path);
		FileUtils.writeResourceFromByteArrayNIO(pathObj, buffer);
		System.out.println("NIO:" + stopwatch.getTime(TimeUnit.MILLISECONDS) + "ms");
	}

	public synchronized long nextDiskFileId() {
		return maxFileId.incrementAndGet();
	}

	@Test
	public void generateId() {
		for (int i = 0; i < 10; i++) {
			System.out.printf("data.%020d%n", nextDiskFileId());
		}
	}

	/**
	 * 读取资源文件
	 */
	@Test
	public void readResourceStream() throws IOException {

		InputStream resource = FileUtilsTest.class.getClassLoader().getResourceAsStream("zzl.html");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int bufSize = 1024;
		byte[] buffer = new byte[bufSize];
		int len = 0;
		while (-1 != (len = resource.read(buffer, 0, bufSize))) {
			bos.write(buffer, 0, len);
		}
		logger.info(bos.toString());
	}

	@Test
	public void readBigFile() throws IOException {
		FileUtils.splitBigFile();
	}

	@Test
	public void recursiveDel() {
		FileUtils.deleteRecursively(new File("d:\\test-del"));
	}

	@Test
	public void appendFile() {
		File file = new File("d:\\append.txt");
		FileUtils.writeFileContent(file, "hello".getBytes());
		FileUtils.writeFileContent(file, "world".getBytes());
		Assert.assertEquals("world", new String(FileUtils.readFileContent(file)));
	}

	@Test
	public void randomAccessFile() throws IOException {
		File f = new File("d:\\append.txt");
		// fileSize(8B)+ blockCount(4B) + blockIndexOffset(8B) + blockIndexOffset(8B) +
		// DISK_FILE_MAGIC
		// (8B)
		final int TRAILER_SIZE = 8 + 4 + 8 + 8 + 8;
		RandomAccessFile in = new RandomAccessFile(f, "r");

		long fileSize = f.length();
		assert fileSize > TRAILER_SIZE;
		in.seek(fileSize - TRAILER_SIZE);// 跳过前36个字符

		byte[] buffer = new byte[8];
		assert in.read(buffer) == buffer.length;

	}

	@Test
	public void readToEnd() throws IOException {
		String msg = FileUtils.readToEnd(FileUtilsTest.class.getClassLoader().getResourceAsStream("zzl.txt"),
				Charset.defaultCharset());
		System.out.println(msg);
	}

}
