package com.lind.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;

import java.util.Properties;
import java.util.UUID;

/**
 * 事务消息.
 *
 * @author lind
 * @date 2023/3/20 16:00
 * @since 1.0.0
 */
public class TransactionMq {

	@Test
	public void send() {
		String topic = "test-trans";
		String transactionId = UUID.randomUUID().toString();
		Properties properties = new Properties();
		properties.put(org.apache.kafka.clients.producer.ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionId);
		// 用于建立与Kafka集群的初始连接的主机/端口对的列表
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.60.146:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		// 记录键值对的默认序列化和反序列化库
		KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

		// 初始化事务
		producer.initTransactions();
		// 开启事务
		producer.beginTransaction();

		try {
			// 处理业务逻辑
			ProducerRecord<String, String> record1 = new ProducerRecord<String, String>(topic, "msg1");
			producer.send(record1);
			ProducerRecord<String, String> record2 = new ProducerRecord<String, String>(topic, "msg2");
			producer.send(record2);
			ProducerRecord<String, String> record3 = new ProducerRecord<String, String>(topic, "msg3");
			producer.send(record3);
			// 处理其他业务逻辑
			// 提交事务
			producer.commitTransaction();
		}
		catch (ProducerFencedException e) {
			// 中止事务，类似于事务回滚
			producer.abortTransaction();
		}
		producer.close();
	}

}
