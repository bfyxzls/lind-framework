package com.lind.common.classloader;

import com.lind.common.CommonApplication;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JarClassLoaderTest {

	/**
	 * 不同类加载类产生的对象是不同的,即使是同一个类,不同的加载器产生的也是不相等的.
	 */
	@Test
	public void classLoaderTest() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		ClassLoader loader = new ClassLoader() {
			@Override
			public Class<?> loadClass(String name) throws ClassNotFoundException {
				try {
					String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
					InputStream is = getClass().getResourceAsStream(fileName);
					if (is == null) {
						return super.loadClass(name);
					}
					byte[] b = new byte[is.available()];
					is.read(b);
					return defineClass(name, b, 0, b.length);

				}
				catch (IOException e) {
					e.printStackTrace();
					throw new ClassNotFoundException(name);
				}
			}
		};
		Object obj = loader.loadClass("com.lind.common.classloader.JarClassLoaderTest").newInstance();
		System.out.println(obj instanceof JarClassLoaderTest);
		assertFalse(obj instanceof JarClassLoaderTest);
		JarClassLoaderTest self = new JarClassLoaderTest();
		assertTrue(self instanceof JarClassLoaderTest);

	}

	/**
	 * 加载一个动态方法.
	 */
	@Test
	public void loadDynamicMethod() throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException,
			IOException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		String path = "D:\\github\\lind-start\\lind-start-framework\\lind-common\\target\\lind-common-1.0.0.jar";
		JarClassLoader jarClassLoader = new JarClassLoader(new JarFile(new File(path)));

		Enumeration<JarEntry> entries = jarClassLoader.jarFile.entries();
		while (entries.hasMoreElements()) {
			String name = entries.nextElement().getName();
			System.out.println("name: " + name);
		}
		Class clazz1 = jarClassLoader.loadClass("com.lind.common.CommonApplication");
		Object obj1 = clazz1.newInstance();
		Method method1 = clazz1.getDeclaredMethod("print", null);
		method1.invoke(obj1, null);
		System.out.println(clazz1.getClassLoader().getClass().getName());

	}

	/**
	 * 加载接口方法.
	 */
	@Test
	public void loadInterfaceMethod() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			NoSuchMethodException, InvocationTargetException {
		JarClassLoader.joinJar(CommonApplication.class,
				"file:///D:\\github\\classloadhot\\target\\classloadhot-1.0-SNAPSHOT.jar");
		// Class clazz1 =
		// JarClassLoader.getSystemClassLoader().loadClass("com.tool.Demo");
		Class clazz1 = this.getClass().getClassLoader().loadClass("com.tool.Demo");
		Object obj1 = clazz1.newInstance();
		Method method1 = clazz1.getDeclaredMethod("print", null);
		method1.invoke(obj1, null);
	}

}
