package com.lind.common.core.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static com.lind.common.core.util.FileSplitUtils.fileJoin2;
import static com.lind.common.core.util.FileSplitUtils.splitFile;

/**
 * @author lind
 * @date 2023/7/13 14:39
 * @since 1.0.0
 */
public class FileSplitUtilsTest {

	private static final String INPUT_FILE_PATH = "d:\\webshu_0_to_20221226.csv.4";

	@Test
	public void main() throws IOException {
		File input = new File(INPUT_FILE_PATH);
		fileJoin2(splitFile(input, 1802));
	}

}
