package com.lind.common.proxy.invocation_handler;

/**
 * @author lind
 * @date 2022/12/26 9:29
 * @since 1.0.0
 */
public class Helloimplements implements IHello {

	@Override
	public void sayHello(String name) {
		System.out.println("Hello " + name);
	}

	@Override
	public void sayGoogBye(String name) {
		System.out.println(name + " GoodBye!");
	}

}
