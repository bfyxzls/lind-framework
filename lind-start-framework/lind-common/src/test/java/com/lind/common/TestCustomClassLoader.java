package com.lind.common;

import com.lind.common.classloader.CustomClassLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestCustomClassLoader {

	private CustomClassLoader customClassLoader;

	@Before
	public void setUp() {
		customClassLoader = new CustomClassLoader();
	}

	/**
	 * com.ourownjava.corejava.util.ClassToLoad is in class path so the system class
	 * loader should load it.
	 * @throws ClassNotFoundException
	 */
	@Test
	public void shoudlLoadClassUsingSystemClassLoader() throws ClassNotFoundException {
		final Class<?> clazz = customClassLoader.loadClass("com.lind.common.classloader.ClassToLoad");
		Assertions.assertNotNull(clazz);
		Assertions.assertEquals("sun.misc.Launcher$AppClassLoader", clazz.getClassLoader().getClass().getName());
	}

	/**
	 * org.sanju.corejava.util.ClassDoesNotExistInClassPath is not in class path, so the
	 * custom class loader should load it.
	 * @throws ClassNotFoundException
	 */
	@Test
	public void shouldLoadClassUsingCustomClassLoader() throws ClassNotFoundException {
		final Class<?> clazz = customClassLoader.loadClass("org.sanju.corejava.util.ClassDoesNotExistInClassPath");
		Assertions.assertNotNull(clazz);
		Assertions.assertEquals("com.ourownjava.corejava.util.CustomClassLoader",
				clazz.getClassLoader().getClass().getName());
	}

}
