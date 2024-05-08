package com.lind.common.http;

import cn.hutool.socket.SocketRuntimeException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author lind
 * @date 2023/2/7 10:08
 * @since 1.0.0
 */
public class SocketTest {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void telnet() {
		String host = "192.168.60.138";
		int port = 7474;
		int timeout = 60;
		Socket socket = new Socket();

		try {
			socket.connect(new InetSocketAddress(host, port), timeout);
			logger.info("status:{}", socket.isConnected());
		}
		catch (IOException e) {
			throw new SocketRuntimeException(String.format("DNS resolution failed for url in %s:%s", host, port));
		}
		finally {
			try {
				socket.close();
			}
			catch (IOException e) {
				// nothing to do
			}
		}

	}

}
