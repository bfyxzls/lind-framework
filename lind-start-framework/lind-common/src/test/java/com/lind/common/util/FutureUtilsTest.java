package com.lind.common.util;

import com.lind.common.util.FutureUtils;
import org.junit.Test;

import java.util.function.Consumer;

public class FutureUtilsTest {

	@Test
	public void run() {
		FutureUtils.run((a) -> {
			System.out.println("a=" + a);
		}, "1");
	}

	@Test
	public void result() {
		System.out.println(FutureUtils.run(() -> {
			return "ok";
		}));
	}

	@Test
	public void runResult() {
		FutureUtils.run((a) -> {
			System.out.println("a=" + a);
			return a;
		}, "lind").thenAccept(new Consumer<String>() {
			@Override
			public void accept(String s) {
				System.out.println("s=" + s);
			}
		});
	}

}
