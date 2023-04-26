package com.lind.common.classloader;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class ClassloaderTest {

	@Test
	public void readOuter() throws IOException {
		Assert.notNull(JarClassLoader.getSourceInputStream("/d:/pkulaw-upgrade-programt.jar", "resource.json"));
	}

}
