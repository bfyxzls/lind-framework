package com.lind.common.core.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.lind.common.core.util.FileSplitUtils.fileJoin2;
import static com.lind.common.core.util.FileSplitUtils.splitFile;

/**
 * @author lind
 * @date 2023/7/13 14:39
 * @since 1.0.0
 */
public class FileSplitUtilsTest {

	@Test
	public void main() throws IOException {
		InputStream inputStream = FileSplitUtilsTest.class.getClassLoader().getResourceAsStream("big.txt");
		fileJoin2(FileSplitUtils.splitByInputStream(inputStream, 10));
	}

}
