package com.lind.common.classloader;

import cn.hutool.core.io.IoUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 外部Jar类型加载器.
 */
public class JarClassLoader extends ClassLoader {

	final static Logger LOGGER = LoggerFactory.getLogger(JarClassLoader.class);

	public JarFile jarFile;

	public ClassLoader parent;

	public JarClassLoader(JarFile jarFile) {
		super(Thread.currentThread().getContextClassLoader());
		this.parent = Thread.currentThread().getContextClassLoader();
		this.jarFile = jarFile;
	}

	public JarClassLoader(JarFile jarFile, ClassLoader parent) {
		super(parent);
		this.parent = parent;
		this.jarFile = jarFile;
	}

	/**
	 * 获取jar包所在路径URL格式
	 * @return jar包所在路径
	 */
	public static String getMainJarPath() {
		ApplicationHome home = new ApplicationHome(JarClassLoader.class);
		String path = home.getSource().toURI().toString();
		if (path.endsWith(".jar")) {
			path = path.substring(0, path.lastIndexOf("/") + 1);
		}
		LOGGER.info("getMainJarPath:{}", path);
		return path;
	}

	/**
	 * 读取包
	 * @param packageUrl 包的相对路径，应该是和当前运行的jar在同级或者下级目录
	 * @param clazz
	 * @param name 类名
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static <U> U joinOuterJarClass(Class<U> clazz, String packageUrl, String name) {
		try {

			LOGGER.info("packageUrl:{}", packageUrl);
			URL url = new URL(packageUrl);
			// IDEA调试时与java运行时的ClassLoader是不同的,所以需要使用当前环境下的ClassLoader
			ClassLoader loader = new URLClassLoader(new URL[] { url }, clazz.getClassLoader()) {
				@Override
				public Class<?> loadClass(String name) throws ClassNotFoundException {
					InputStream inputStream = null;
					try {
						String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
						inputStream = getClass().getResourceAsStream(fileName);
						if (inputStream == null) {
							return super.loadClass(name);
						}
						byte[] b = new byte[inputStream.available()];
						inputStream.read(b);
						return defineClass(name, b, 0, b.length);

					}
					catch (IOException e) {
						throw new ClassNotFoundException(name);
					}
					finally {
						try {
							if (inputStream != null) {
								inputStream.close();
							}
						}
						catch (IOException e) {
							throw new RuntimeException(e);
						}
					}
				}
			};
			Object obj = loader.loadClass(name).newInstance();
			for (Class<?> c : obj.getClass().getInterfaces()) {
				LOGGER.info(c.getName());
			}
			return clazz.cast(obj);
		}
		catch (RuntimeException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("findClassLoader.error");
	}

	/**
	 * 将jar包添加到当前类加载器. 在当前类加载器 this.getClass().getClassLoader()中，是可以使用这些类的.
	 * @param path
	 * @param clazz
	 */
	@SneakyThrows
	public static void joinJar(Class clazz, String path) {
		// 将本地jar文件加载至classloader
		URLClassLoader loader = (URLClassLoader) clazz.getClassLoader();
		URL targetUrl = new URL(path);

		boolean isLoader = false;
		for (URL url : loader.getURLs()) {
			if (url.equals(targetUrl)) {
				isLoader = true;
				break;
			}
		}
		// 如果没有加载
		if (!isLoader) {
			Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			add.setAccessible(true);
			add.invoke(loader, targetUrl);
		}
	}

	/**
	 * 得到资源文件流
	 */
	@SneakyThrows
	public static byte[] getSourceInputStream(String jar, String resourceName) {
		JarFile jarFile = new JarFile(jar);
		final InputStream fileResource = jarFile.getClass().getResourceAsStream(resourceName);
		byte[] bytes = IoUtil.readBytes(fileResource);
		return bytes;
	}

	public String classNameToJarEntry(String name) {
		String s = name.replaceAll("\\.", "\\/");
		StringBuilder stringBuilder = new StringBuilder(s);
		stringBuilder.append(".class");
		return stringBuilder.toString();

	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			Class c = null;
			if (null != jarFile) {
				String jarEntryName = classNameToJarEntry(name);
				JarEntry entry = jarFile.getJarEntry(jarEntryName);
				if (null != entry) {
					InputStream is = jarFile.getInputStream(entry);
					int availableLen = is.available();
					int len = 0;
					byte[] bt1 = new byte[availableLen];
					while (len < availableLen) {
						len += is.read(bt1, len, availableLen - len);
					}
					c = defineClass(name, bt1, 0, bt1.length);
				}
				else {
					if (parent != null) {
						return parent.loadClass(name);
					}
				}
			}
			return c;
		}
		catch (IOException e) {
			throw new ClassNotFoundException("Class " + name + " not found.");
		}
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		InputStream is = null;
		try {
			if (null != jarFile) {
				JarEntry entry = jarFile.getJarEntry(name);
				if (entry != null) {
					is = jarFile.getInputStream(entry);
				}
				if (is == null) {
					is = super.getResourceAsStream(name);
				}
			}
		}
		catch (IOException e) {
			// logger.error(e.getMessage());
			System.out.println(e.getMessage());
		}
		return is;
	}

}
