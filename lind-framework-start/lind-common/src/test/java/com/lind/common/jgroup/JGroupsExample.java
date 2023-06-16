package com.lind.common.jgroup;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author lind
 * @date 2023/6/8 11:10
 * @since 1.0.0
 */
@Component
public class JGroupsExample {

	private JChannel channel;

	@PostConstruct
	public void init() throws Exception {
		channel = new JChannel(); // 创建 JGroups 通道

		// 设置 ReceiverAdapter 作为消息接收器
		channel.setReceiver(new ReceiverAdapter() {
			public void receive(Message msg) {
				System.out.println("Received message: " + msg.getObject());
			}
		});

		channel.connect("myCluster"); // 连接到指定的集群名称
	}

	public void sendMessage(String message) throws Exception {
		Message msg = new Message(null, message); // 创建消息
		channel.send(msg); // 发送消息
	}

}
