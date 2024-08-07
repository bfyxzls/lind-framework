package com.lind.common.classloader;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * 类加载器测试.
 */
@Slf4j
public class ClassloaderTest {

	/**
	 * Note: 读取jar包内部的文件. Warning: 读取jar包内部的文件，需要使用getResourceAsStream，而不是getResource.
	 * @throws IOException
	 */
	@Test
	public void readOuter() throws IOException {
		Assert.notNull(JarClassLoader.getSourceInputStream("/d:/demo-upgrade-programt.jar", "resource.json"));
	}

}
