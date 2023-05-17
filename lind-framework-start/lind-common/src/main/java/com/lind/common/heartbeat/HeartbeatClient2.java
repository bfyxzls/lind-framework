package com.lind.common.heartbeat;

import cn.hutool.core.thread.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 心跳检查.
 *
 * @author lind
 * @date 2023/3/2 11:08
 * @since 1.0.0
 */
public class HeartbeatClient2 extends Thread {

	static Logger logger = LoggerFactory.getLogger(HeartbeatClient2.class);
	static long interval = 5000;

	/**
	 * 程序的入口main方法
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {

		String ip = "192.168.60.138";
		int port = 7474;
		new HeartbeatClient2().startHeartbeatTimer(ip, port);
	}

	/**
	 * 开始心跳检查.
	 * @param ip
	 * @param port
	 */
	private void startHeartbeatTimer(String ip, int port) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// 发送心跳包代码
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), 1000);
					logger.info("端口{}正常", port);
				}
				catch (Exception e) {
					logger.info("端口{}异常！", port);
				}
			}
		}, 0, interval);

	}

}
