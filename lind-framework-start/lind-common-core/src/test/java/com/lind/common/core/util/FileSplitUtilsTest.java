package com.lind.common.core.util;

import com.lind.common.core.util.file.FileSplitUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static com.lind.common.core.util.file.FileSplitUtils.fileJoin2;

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
