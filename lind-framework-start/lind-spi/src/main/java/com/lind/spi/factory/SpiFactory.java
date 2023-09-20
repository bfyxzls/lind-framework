package com.lind.spi.factory;

import cn.hutool.core.io.FileUtil;
import com.lind.spi.util.DynamicClassLoader;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * spi插件加载器.
 */
public class SpiFactory {

	/**
	 * Parameters of the method to add an URL to the System classes.
	 */
	private static final Class<?>[] parameters = new Class[] { URL.class };

	public static List<DynamicClassLoader> dynamicClassLoaders = new ArrayList<>();

	/**
	 * 初始化时，应该对插件读出后，写到一个Map里，插件名称是key，DynamicClassLoader是value.
	 * @param path
	 */
	@SneakyThrows
	static void initClassLoader(String path) {
		for (File file : FileUtil.loopFiles(path)) {
			System.out.println("load jar:" + file.getName());
			URL url = file.toURI().toURL();
			DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(new URL[] { url },
					ClassLoader.getSystemClassLoader());
			dynamicClassLoaders.add(dynamicClassLoader);
		}
	}

	/**
	 * 目录监控.
	 * @param path
	 */
	public static void watchDir(String path) {
		initClassLoader(path);
		try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
			// 给path路径加上文件观察服务
			Paths.get(path).register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
			while (true) {
				final WatchKey key = watchService.take();
				for (WatchEvent<?> watchEvent : key.pollEvents()) {
					final WatchEvent.Kind<?> kind = watchEvent.kind();
					if (kind == StandardWatchEventKinds.OVERFLOW) {
						continue;
					}
					final WatchEvent<Path> watchEventPath = (WatchEvent<Path>) watchEvent;
					final Path filename = watchEventPath.context();
					System.out.println(kind + " -> " + filename);
					initClassLoader(path);
				}
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		}
		catch (IOException | InterruptedException ex) {
			System.err.println(ex);
		}
	}

	/**
	 * Adds a file to the classpath.
	 * @param s a String pointing to the file
	 * @throws IOException
	 */
	public static void addFile(String s) throws IOException {
		File f = new File(s);
		addFile(f);
	}

	/**
	 * Adds a file to the classpath
	 * @param f the file to be added
	 * @throws IOException
	 */
	public static void addFile(File f) throws IOException {
		addURL(f.toURI().toURL());
	}

	/**
	 * 加载jar到当前的classLoader.
	 * @param u the URL pointing to the content to be added
	 * @throws IOException
	 */
	public static void addURL(URL u) throws IOException {
		URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class<?> sysclass = URLClassLoader.class;
		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysloader, u);
		}
		catch (Throwable t) {
			t.printStackTrace();
			throw new IOException("Error, could not add URL to system classloader");
		}
	}

	/**
	 * 从具体的providerFactory中得到指定的providerFactory.
	 * @param clazz
	 * @param id
	 * @param classLoader
	 * @param <U>
	 * @return
	 */
	public static <U extends ProviderFactory> U getProviderFactory(Class<U> clazz, String id, ClassLoader classLoader) {
		ServiceLoader<U> load = ServiceLoader.load(clazz, classLoader);
		for (U providerFactory : load) {
			System.out.println("getProviderFactory:" + providerFactory.getId());
		}
		for (U providerFactory : load) {
			if (providerFactory.getId().equalsIgnoreCase(id)) {
				return providerFactory;
			}
		}
		return null;
	}

	/**
	 * 返回所有具体的providerFactory列表.
	 * @param clazz
	 * @param classLoader
	 * @param <U>
	 * @return
	 */
	public static <U extends ProviderFactory> List<U> getProviderFactory(Class<U> clazz, ClassLoader classLoader) {
		ServiceLoader<U> load = ServiceLoader.load(clazz, classLoader);
		List<U> list = new ArrayList<>();
		for (U providerFactory : load) {
			list.add(providerFactory);
		}
		return list;
	}

	/**
	 * 从所有providerFactory中得到指定的providerFactory.
	 * @param id
	 * @param classLoader
	 * @return
	 */
	public static ProviderFactory getProviderFactory(String id, ClassLoader classLoader) {
		return getProviderFactory(ProviderFactory.class, id, classLoader);
	}

	/**
	 * 返回所有providerFactory列表.
	 * @param classLoader
	 * @return
	 */
	public static List<ProviderFactory> getProviderFactory(ClassLoader classLoader) {
		return getProviderFactory(ProviderFactory.class, classLoader);
	}

	/**
	 * 返回所有具体的providerFactory列表，使用dynamicClassLoaders加载器
	 * @param clazz
	 * @param <U>
	 * @return
	 */
	public static <U extends ProviderFactory> U getProviderFactory(Class<U> clazz, String id) {
		for (ClassLoader classLoader : dynamicClassLoaders) {
			ServiceLoader<U> load = ServiceLoader.load(clazz, classLoader);
			for (U providerFactory : load) {
				if (providerFactory.getId().equals(id)) {
					return providerFactory;
				}
			}
		}
		return null;
	}

	/**
	 * 返回所有具体的providerFactory工厂，使用dynamicClassLoaders加载器
	 * @param clazz
	 * @param <U>
	 * @return
	 */
	public static <U extends ProviderFactory> List<U> getProviderFactory(Class<U> clazz) {
		List<U> list = new ArrayList<>();
		for (ClassLoader classLoader : dynamicClassLoaders) {
			ServiceLoader<U> load = ServiceLoader.load(clazz, classLoader);
			List<String> idList = list.stream().map(o -> o.getId()).collect(Collectors.toList());
			for (U providerFactory : load) {
				if (!idList.contains(providerFactory.getId())) {
					list.add(providerFactory);
				}
			}
		}
		return list;
	}

}
