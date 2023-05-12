package com.lind.common.heartbeat;

import cn.hutool.core.thread.NamedThreadFactory;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HeartbeatClient {

	static long sleepTime = 1000; // 发送心跳包间隔时间
	static int timeout = 3000;
	static int maxFailures = 3; // 最大失败次数
	static int failures = 0; // 当前失败次数

	static Logger logger = LoggerFactory.getLogger(HeartbeatClient2.class);

	static ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(2,
			new NamedThreadFactory("lind-framework-heartbeat", true));

	ScheduledFuture heartbeatTimer;

	public static void main(String[] args) throws IOException {
		send("localhost", 80);
	}

	private static void updateConfigFile(){
		// 更新配置文件的代码，可以根据实际情况自行编写
		logger.error("心跳失败，更新配置文件");
	}

	@SneakyThrows
	public static void send(String ip, int port) {
		// 建立连接
		Socket socket = new Socket();
		socket.setSoTimeout(timeout);
		socket.connect(new InetSocketAddress(ip, port), timeout);
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		scheduled.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				// 发送心跳包代码
				try {
					// 发送心跳包
					dos.writeUTF("heartbeat");
					dos.flush();

					// 接收响应
					String response = dis.readUTF();
					if ("OK".equals(response)) {
						failures = 0; // 重置失败计数器
					}
				}
				catch (IOException e) {
					failures++; // 连接失败，增加失败次数
					if (failures >= maxFailures) {
						// 心跳包失败次数超过阈值，更新配置文件
						updateConfigFile();
					}
				}
			}
		}, 0, sleepTime, TimeUnit.MILLISECONDS); // interval为心跳包发送间隔
	}

}
