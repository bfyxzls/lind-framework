package com.lind.common.util;

import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceUtilsTest {

	@Test
	public void readResourcesFile() throws IOException {
		// 文件需要在main/resources目录下，而test/resources不可以
		File file = ResourceUtils.getFile("classpath:rsa_pcks8_pem.private");
		InputStream inputStream = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

}
