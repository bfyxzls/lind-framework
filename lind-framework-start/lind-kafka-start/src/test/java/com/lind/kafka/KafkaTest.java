package com.lind.kafka;

import com.lind.kafka.anno.EnableMqKafka;
import com.lind.kafka.entity.MessageEntity;
import com.lind.kafka.producer.MessageSender;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableMqKafka
@ActiveProfiles("dev")
public class KafkaTest {

	@Autowired
	MessageSender messageSender;

	@Autowired
	MessageDataSend messageDataSend;

	@Autowired
	private KafkaListenerEndpointRegistry kafkaRegistry;

	// MessageListenerTest.messageReceive批量收消息，自动ack，当有异常时，消息不会被消费.
	@SneakyThrows
	@Test
	public void testReceivingKafkaEvents() throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			MessageEntity testMessageEntity = new MessageEntity();
			UserDTO userDTO = new UserDTO();
			userDTO.setTitle("世界你好" + new Date());
			testMessageEntity.setData(userDTO);
			messageSender.send("lind-demo", testMessageEntity);
		}
		TimeUnit.SECONDS.sleep(10);
	}

	@Test
	public void resume() throws InterruptedException {
		kafkaRegistry.getListenerContainer("demo1").resume();
		TimeUnit.SECONDS.sleep(10);

	}

	@Test
	public void annoSender() {

		UserDTO userDTO = new UserDTO();
		userDTO.setTitle("世界你好" + new Date());
		messageDataSend.sendDataMessage(userDTO, "bo");
	}

	@SneakyThrows
	@Test
	public void sleep1000() {
		MessageEntity testMessageEntity = new MessageEntity();
		UserDTO userDTO = new UserDTO();
		userDTO.setTitle("世界你好1" + new Date());
		testMessageEntity.setData(userDTO);
		messageSender.send("sleep-test", testMessageEntity);
		Thread.sleep(10);
		userDTO.setTitle("世界你好2" + new Date());
		testMessageEntity.setData(userDTO);
		messageSender.send("sleep-test", testMessageEntity);
		Thread.sleep(10);
		userDTO.setTitle("世界你好3" + new Date());
		testMessageEntity.setData(userDTO);
		messageSender.send("sleep-test", testMessageEntity);

		messageSender.send("sleep-test1", testMessageEntity);

		TimeUnit.SECONDS.sleep(10 * 5);
	}

	/**
	 * 让消费时长大于max.poll.interval.ms的时间，是否有重复消费情况
	 */
	@SneakyThrows
	@Test
	public void manual() {
		UserDTO userDTO = new UserDTO();
		MessageEntity testMessageEntity = new MessageEntity();
		testMessageEntity.setData(userDTO);
		messageSender.send("sleep3", testMessageEntity);
		messageSender.send("sleep3", testMessageEntity);

		TimeUnit.SECONDS.sleep(10 * 30);
	}

}
