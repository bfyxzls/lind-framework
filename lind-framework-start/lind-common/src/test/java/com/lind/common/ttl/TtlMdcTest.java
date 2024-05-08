package com.lind.common.ttl;

import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2023/3/7 16:22
 * @since 1.0.0
 */
public class TtlMdcTest {

	@Test
	public void mainWriteSubRead() {
		TtlMdc.getInstance().put("hello", "你好");
		new Thread(() -> {
			System.out.println(TtlMdc.getInstance().get("hello"));
		}).start();
	}

}
