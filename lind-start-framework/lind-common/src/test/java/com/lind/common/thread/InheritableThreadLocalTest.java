package com.lind.common.thread;

import org.junit.Test;

/**
 * 子线程中访问主线程的对象.
 */
public class InheritableThreadLocalTest implements Runnable {

	static InheritableThreadLocal<String> map = new InheritableThreadLocal<String>();

	@Test
	public void test() throws InterruptedException {
		System.out.println("main start");
		map.set("hello main");
		new InheritableThreadLocalTest().run();
		Thread.sleep(3000);
		System.out.println("main end");
	}

	@Override
	public void run() {
		System.out.println("子纯种：" + map.get());
	}

}
