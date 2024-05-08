package com.lind.common.classloader;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * @author lind
 * @date 2022/11/10 14:20
 * @since 1.0.0
 */
public class RsaClassLoaderTest {

	/**
	 * 我们做一个实验，动态修改这个jar，看在动态调用它的方法时，这是可以热加载的.
	 */
	@SneakyThrows
	@Test
	public void test() {
		while (true) {
			Class clazz = RsaClassLoader.findClassLoader("file:///D:\\classloadhot-1.0-SNAPSHOT.jar", "com.tool.Demo");
			Object account = clazz.newInstance();
			Object ret = account.getClass().getMethod("print", new Class[] {}).invoke(account);
			System.out.println(ret);
			Thread.sleep(1000);
		}
	}

}
