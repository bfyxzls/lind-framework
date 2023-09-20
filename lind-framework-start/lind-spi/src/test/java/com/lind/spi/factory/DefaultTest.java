package com.lind.spi.factory;

import com.lind.spi.provider.Provider;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

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

}
