package com.lind.kafka;

import com.lind.kafka.anno.MqProducer;
import com.lind.kafka.anno.MqSend;

@MqProducer
public interface MessageDataSend {

	@MqSend(topic = "ok_bobo")
	void sendDataMessage(UserDTO messageEntity);

	@MqSend(topic = "ok_bobo_key")
	void sendDataMessage(UserDTO messageEntity, String key);

}
