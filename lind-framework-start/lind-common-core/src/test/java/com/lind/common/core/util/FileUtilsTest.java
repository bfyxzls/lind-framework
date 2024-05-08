package com.lind.common.core.util;

import com.lind.common.core.util.file.FileUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileUtilsTest {

	private static final Logger logger = LoggerFactory.getLogger(FileUtilsTest.class);
	static String path = "D:\\tools\\jdk8-windowsx64.zip";
	static String pathObj = "D:\\jar.zip";

	private final AtomicLong maxFileId = new AtomicLong();

	private static void delFolder(File folder) {
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {

					if (file.isDirectory()) {
						if ((file.getName().equals("main") || file.getName().equals("Main"))) {
							deleteFolder(file);
						}
						else {
							delFolder(file);
						}
					}
				}
			}
		}
	}

	private static void deleteFolder(File folder) {
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						deleteFolder(file);
					}
					else {
						file.delete();
					}
				}
			}
			folder.delete();
		}
	}

	private static void processFolder(File folder) {
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						processFolder(file);
					}
					else if (file.getName().endsWith(".jar")) {
						File mainFolder = new File(file.getParentFile().getParent(), "main");
						if (!mainFolder.exists()) {
							mainFolder.mkdir();
						}

						try {
							String fileNameNoEx = file.getName().substring(0, file.getName().lastIndexOf("-"));
							Files.copy(file.toPath(), new File(mainFolder, file.getName()).toPath(),
									StandardCopyOption.REPLACE_EXISTING);
							String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
									+ "<module name=\"org.apache.shardingsphere." + fileNameNoEx
									+ "\" xmlns=\"urn:jboss:module:1.3\">\n" + "    <resources>\n"
									+ "        <resource-root path=\"" + file.getName() + "\"/>\n"
									+ "    </resources>\n" + " <dependencies></dependencies>\n</module>";

							FileUtils.writeFileContent(new File(mainFolder, "module.xml"), content.getBytes());
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private static void processFolder2(File folder) {
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						processFolder2(file);
					}
					else if (file.getName().endsWith("-4.1.1.jar")) {
						File mainFolder = new File(file.getParentFile().getParentFile().getParent(), "main");
						if (!mainFolder.exists()) {
							mainFolder.mkdir();
						}

						try {
							String fileNameNoEx = file.getName().substring(0, file.getName().lastIndexOf("-"));
							Files.copy(file.toPath(), new File(mainFolder, file.getName()).toPath(),
									StandardCopyOption.REPLACE_EXISTING);
							// String content = "<?xml version=\"1.0\"
							// encoding=\"UTF-8\"?>\n"
							// + "<module name=\"org.apache.shardingsphere." +
							// fileNameNoEx
							// + "\" xmlns=\"urn:jboss:module:1.3\">\n" + " <resources>\n"
							// + " <resource-root path=\"" + file.getName() + "\"/>\n"
							// + " </resources>\n" + "
							// <dependencies></dependencies>\n</module>";
							System.out.println("<resource-root path=\"" + file.getName() + "\"/>");
							// FileUtils.writeFileContent(new File(mainFolder,
							// "module.xml"), content.getBytes());
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

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

		InputStream resource = FileUtilsTest.class.getClassLoader().getResourceAsStream("user.json");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int bufSize = 1024;
		byte[] buffer = new byte[bufSize];
		int len = 0;
		while (-1 != (len = resource.read(buffer, 0, bufSize))) {
			bos.write(buffer, 0, len);
		}
		logger.info(bos.toString());
	}

	@BeforeEach
	public void init() {
		FileUtils.writeFileContent(new File("d:\\test-del"), "hello".getBytes());
		FileUtils.writeFileContent(new File("d:\\append.txt"), "hello".getBytes());

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
		assertEquals("world", new String(FileUtils.readFileContent(file)));
	}

	@Test
	public void readToEnd() throws IOException {
		String msg = FileUtils.readToEnd(FileUtilsTest.class.getClassLoader().getResourceAsStream("user.json"),
				Charset.defaultCharset());
		System.out.println(msg);
	}

	@Test
	public void copyShardingsphereToMain() {
		String path = "d:\\shardingsphere";
		File rootFolder = new File(path);
		delFolder(rootFolder);
		processFolder2(rootFolder);
	}

}
