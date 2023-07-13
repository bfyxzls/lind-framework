package com.lind.common.core.util;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author lind
 * @date 2023/5/19 15:06
 * @since 1.0.0
 */
public class StreamUtilsTest {

	@Test
	public void readToEnd() throws IOException {
		String msg = StreamUtils.readToEnd(FileUtilsTest.class.getClassLoader().getResourceAsStream("zzl.html"),
				Charset.defaultCharset());
		System.out.println(msg);
	}

}
