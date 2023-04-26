package com.lind.kafka.util;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;

/**
 * 重复执行类
 */
public class RetryUtils {

	final static Logger logger = LoggerFactory.getLogger(RetryUtils.class);

	@SneakyThrows
	public static void reDo(int errorRetry, Acknowledgment ack, Runnable runnable) {
		int retry = 0;
		while (retry++ < errorRetry) {
			try {
				runnable.run();
				ack.acknowledge();
				return;
			}
			catch (Exception e) {
				ack.nack(retry * 1000);
				logger.error(e.getMessage());
			}
		}
		ack.acknowledge();
		logger.error("we repeat {},bug it always fail.", retry);

		logger.debug("reDo success");
	}

}
