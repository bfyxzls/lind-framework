package com.lind.common.classloader;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 自定义类加载器. 动态修改运行时某个包下的类实现，使用新的jar去替换它
 */
public class RsaClassLoader {

	@SneakyThrows
	public static Class findClassLoader(String packageUrl, String name) throws ClassNotFoundException {
		URL url = new URL(packageUrl);
		ClassLoader loader = new URLClassLoader(new URL[] { url }) {
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
		return loader.loadClass(name);
	}

}
