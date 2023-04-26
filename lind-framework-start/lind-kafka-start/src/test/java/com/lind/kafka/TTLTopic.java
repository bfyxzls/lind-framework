package com.lind.kafka;

import com.lind.kafka.anno.EnableMqKafka;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.ByteBuffer;

/**
 * 延时消息，死信队列 kafka通过向header中写入超时时间，在消费者端对这个头进行判断，来达到TTL的功能
 *
 * @author lind
 * @date 2023/3/20 16:22
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableMqKafka
@ActiveProfiles("dev")
public class TTLTopic {

	// 分配缓冲区,单位为字节，long类型占8字节，所以设置为8
	private static ByteBuffer buffer = ByteBuffer.allocate(8);

	// long类型转byte[]
	public static byte[] longToBytes(long x) {
		buffer.putLong(0, x);
		return buffer.array();
	}

	@SneakyThrows
	@Test
	public void send() {
		// Properties properties = new Properties();
		// properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.60.146:9092");
		// properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
		// StringSerializer.class.getName());
		// properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
		// StringSerializer.class.getName());
		// // 记录键值对的默认序列化和反序列化库
		// KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
		//
		// ProducerRecord record = new ProducerRecord<>("test-ttl", 0,
		// System.currentTimeMillis(), null, "msg",
		// new RecordHeaders().add(new RecordHeader("ttl", longToBytes(20))));
		// producer.send(record);
		//
		// // flush data
		// producer.flush();
		// // flush and close producer
		// producer.close();
		Thread.sleep(60 * 1000);
	}

}
