package com.lind.spi.factory;

import com.lind.spi.provider.Provider;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author lind
 * @date 2023/1/5 14:57
 * @since 1.0.0
 */
@Slf4j
public class DefaultTest {

	@Test
	public void serviceLoader() {
		ServiceLoader<ProviderFactory> s = ServiceLoader.load(ProviderFactory.class);
		Iterator<ProviderFactory> iterable = s.iterator();
		while (iterable.hasNext()) {
			Provider provider = iterable.next().create();
			log.info("hello:{}", provider.login());
		}
	}

	@Test
	public void spi() {
		SpiFactory.initClassLoader("d:\\jar");
		List<DefaultProviderFactory> s = SpiFactory.getProviderFactory(DefaultProviderFactory.class);
		Iterator<DefaultProviderFactory> iterable = s.iterator();
		while (iterable.hasNext()) {
			Provider provider = iterable.next().create();
			log.info("spi hello:{}", provider.login());
		}
	}

	// 观察某个文件夹下是否有文件变化
	@Test
	public void watchDir() {
		try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
			// 给path路径加上文件观察服务
			Paths.get("d:\\watch").register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
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

}
