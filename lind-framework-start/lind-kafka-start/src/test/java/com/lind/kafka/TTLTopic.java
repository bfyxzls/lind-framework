package com.lind.kafka;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.nio.ByteBuffer;

import static com.lind.kafka.util.ConvertUtils.toBytes;

/**
 * 延时消息，死信队列 kafka通过向header中写入超时时间，在生产环境拦截器中添加判断，有ttl的，就使用时间轮，延时将它发到死信队列里。
 *
 * @author lind
 * @date 2023/3/20 16:22
 * @since 1.0.0
 */
@SpringBootTest
@ActiveProfiles("dev")
public class TTLTopic {

	// 分配缓冲区,单位为字节，long类型占8字节，所以设置为8
	private static final ByteBuffer buffer = ByteBuffer.allocate(8);

	@Autowired
	KafkaTemplate kafkaTemplate;

	// long类型转byte[]
	public static byte[] longToBytes(long x) {
		buffer.putLong(0, x);
		return buffer.array();
	}

	/**
	 * 发送延时消息
	 * @param message 消息体
	 * @param delaySecondTime 多个秒后过期
	 * @param delayTopic 过期后发送到的话题
	 */
	public void publish(String topic, String message, long delaySecondTime, String delayTopic) {
		ProducerRecord producerRecord = new ProducerRecord<>(topic, 0, System.currentTimeMillis(), null, message,
				new RecordHeaders().add(new RecordHeader("ttl", toBytes(delaySecondTime)))
						.add(new RecordHeader("dead_topic", delayTopic.getBytes())));
		kafkaTemplate.send(producerRecord);
	}

	@SneakyThrows
	@Test
	public void send() {
		publish("test-delay", "hello", 30, "test-delay-dead");
		Thread.sleep(60 * 1000);
	}

}
