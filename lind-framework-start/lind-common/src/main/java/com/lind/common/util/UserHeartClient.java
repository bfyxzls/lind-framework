package com.lind.common.util;

import cn.hutool.core.thread.NamedThreadFactory;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 心跳检查.
 *
 * @author lind
 * @date 2023/3/2 11:08
 * @since 1.0.0
 */

public class UserHeartClient extends Thread {

	ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(2,
			new NamedThreadFactory("dubbo-remoting-client-heartbeat", true));

	ScheduledFuture heartbeatTimer;

	/**
	 * 程序的入口main方法
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {

		new UserHeartClient().scheduled.scheduleAtFixedRate(() -> {
			System.out.println("hello" + Thread.currentThread().getName());
		}, 0, 1, TimeUnit.SECONDS);
		new UserHeartClient().startHeartbeatTimer();
		String ip = "192.168.60.138";
		int port = 7474;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), 1000);
			System.out.println("端口：" + port + "正常！");
		}
		catch (Exception e) {
			System.out.println("端口：" + port + "异常！");
		}

		Thread.sleep(5000);
	}

	private void startHeartbeatTimer() {
		heartbeatTimer = scheduled.scheduleWithFixedDelay(() -> {
			System.out.println("startHeartbeatTimer start...");
		}, 0, 500, TimeUnit.MILLISECONDS);

	}

}
