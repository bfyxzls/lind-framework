package com.lind.minio;

import com.lind.minio.config.MinioConfig;
import com.lind.minio.util.MinioUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lind 地址：http://192.168.4.26:9090
 * @date 2023/5/5 16:04
 * @since 1.0.0
 */
@SpringBootTest(classes = { MinioConfig.class })
@TestPropertySource("classpath:application.yml") // 配置文件注入
public class MinioUtilTest {

	@Autowired
	MinioUtil minioUtil;

	@Test
	public void createBucket() throws Exception {
		minioUtil.createBucket("lind");
	}

	@Test
	public void readFromFile() throws IOException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.txt");
		System.out.println(new String(minioUtil.fromStreamToBytes(inputStream)));
	}

	@Test
	public void uploadFile() {
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.txt");
			minioUtil.uploadFile(inputStream, "lind", "test");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void listBuckets() {
		try {
			minioUtil.listBuckets().forEach(System.out::println);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void listFiles() {
		try {
			minioUtil.listFiles("lind").forEach(System.out::println);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void download() throws Exception {
		InputStream inputStream = minioUtil.download("lind", "test");
		System.out.println(new String(minioUtil.fromStreamToBytes(inputStream)));
	}

}
