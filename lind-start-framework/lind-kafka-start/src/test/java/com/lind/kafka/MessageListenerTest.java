package com.lind.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lind.kafka.entity.MessageEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.utils.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Optional;

@Component
@Slf4j
public class MessageListenerTest {

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * byteBuffer 转 byte数组
	 * @param buffer
	 * @return
	 */
	public static byte[] bytebuffer2ByteArray(ByteBuffer buffer) {
		// 重置 limit 和postion 值
		buffer.flip();
		// 获取buffer中有效大小
		int len = buffer.limit() - buffer.position();

		byte[] bytes = new byte[len];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = buffer.get();

		}

		return bytes;
	}

	/**
	 * byte 数组转byteBuffer
	 * @param byteArray
	 */
	public static ByteBuffer byte2Byffer(byte[] byteArray) {

		// 初始化一个和byte长度一样的buffer
		ByteBuffer buffer = ByteBuffer.allocate(byteArray.length);
		// 数组放到buffer中
		buffer.put(byteArray);
		// 重置 limit 和postion 值 否则 buffer 读取数据不对
		buffer.flip();
		return buffer;
	}

	@SneakyThrows
	@KafkaListener(id = "demo1", topics = "lind-demo", groupId = "default", containerFactory = "batchFactory")
	public void messageReceive(String message) {
		MessageEntity<UserDTO> userDTOMessageEntity = objectMapper.readValue(message,
				new TypeReference<MessageEntity<UserDTO>>() {
				});
		int a = 1 / 0;
		System.out.println("default demo" + userDTOMessageEntity.getData());
	}

	@SneakyThrows
	@KafkaListener(topics = "demo", groupId = "order-service")
	public void orderMessageReceive(String message) {
		MessageEntity<UserDTO> userDTOMessageEntity = objectMapper.readValue(message,
				new TypeReference<MessageEntity<UserDTO>>() {
				});
		System.out.println("order" + userDTOMessageEntity.getData());
	}

	/**
	 * 一个消费者执行时间长，会出现消息积压，这与partition数量无关，partition可以避免单个broken的topic过多
	 * @param record
	 */
	@SneakyThrows
	@KafkaListener(id = "sleep1", topics = "sleep-test", groupId = "default")
	public void limitReceive1(ConsumerRecord<String, String> record) {
		MessageEntity<UserDTO> userDTOMessageEntity = objectMapper.readValue(record.value(),
				new TypeReference<MessageEntity<UserDTO>>() {
				});
		log.info("sleepasync1 {} {}", record.partition(), userDTOMessageEntity.getData().getClass());
		Thread.sleep(10000);
	}

	/**
	 * limitReceive1和limitReceive2是同组下的不同消费者，当消息积压时，可以使用多个消费者模式
	 * @param record
	 */
	@SneakyThrows
	@KafkaListener(id = "sleep2", topics = "sleep-test", groupId = "default")
	public void limitReceive2(ConsumerRecord<String, String> record) {
		MessageEntity<UserDTO> userDTOMessageEntity = objectMapper.readValue(record.value(),
				new TypeReference<MessageEntity<UserDTO>>() {
				});
		log.info("sleepasync2 {} {}", record.partition(), userDTOMessageEntity.getData().getClass());
		Thread.sleep(10000);
	}

	/**
	 * 消费者3，多个消费者，消费不同partition时，不会阻塞
	 * @param record
	 */
	@SneakyThrows
	@KafkaListener(id = "sleep3", topics = "sleep-test", groupId = "default")
	public void limitReceive3(ConsumerRecord<String, String> record) {
		MessageEntity<UserDTO> userDTOMessageEntity = objectMapper.readValue(record.value(),
				new TypeReference<MessageEntity<UserDTO>>() {
				});
		log.info("sleepasync3 {} {}", record.partition(), userDTOMessageEntity.getData().getClass());
		Thread.sleep(10000);
	}

	/**
	 * 消费者的手动提交，失败后可以进行重试. 配置：spring.kafka.consumer.enable-auto-commit:false
	 * @param consumer
	 * @param ack
	 */
	@KafkaListener(topics = "sleep3", containerFactory = "manualKafkaListenerContainerFactory1")
	public void ConsumerGroupId2(ConsumerRecord<String, String> consumer, Acknowledgment ack) {
		Optional<ConsumerRecord<String, String>> message = Optional.ofNullable(consumer);
		if (message.isPresent()) {
			int retry = 0;
			int errorRetry = 3;
			while (retry++ < errorRetry) {
				try {
					// 业务逻辑
					System.out.println("消费者B topic名称:" + consumer.topic() + ", key:" + consumer.key() + ", value:"
							+ consumer.value() + ", 分区位置:" + consumer.partition() + ", 下标:" + consumer.offset()
							+ ", timestamp:" + consumer.timestamp());
					Thread.sleep(3000);
					ack.acknowledge();
					return;
				}
				catch (Exception ex) {
					// 否定ack应答，服务重启时会再次消费未应答的offset消息
					ack.nack(100);
					// 加了消费失败日志中
				}

			}
			System.out.println("退出重试");
			ack.acknowledge();

		}
	}

	@SneakyThrows
	@KafkaListener(topics = "test-ttl", groupId = "default")
	public void ttl(ConsumerRecord<String, String> record) {
		record.headers().forEach(o -> {
			System.out.println("header:" + ByteUtils.readVarlong(byte2Byffer(o.value())));
		});

	}

}
