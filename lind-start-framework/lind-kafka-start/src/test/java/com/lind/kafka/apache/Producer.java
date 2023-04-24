package com.lind.kafka.apache;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {

	private final static String BOOTSTRAP_SERVER = "192.168.10.132:9091,192.168.10.133:9092,192.168.10.134:9097";

	public static void publishEvent(String topic, String value) {
		publishEvent(topic, null, value);
	}

	public static void publishEvent(String topic, String key, String value) {
		// reset thread context
		resetThreadContext();
		// create the producer

		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(getProperties());
		// create a producer record
		ProducerRecord<String, String> eventRecord = new ProducerRecord<String, String>(topic, key, value);

		// send data - asynchronous
		producer.send(eventRecord);

		// flush data
		producer.flush();
		// flush and close producer
		producer.close();
	}

	private static void resetThreadContext() {
		Thread.currentThread().setContextClassLoader(null);
	}

	public static Properties getProperties() {
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}

}
