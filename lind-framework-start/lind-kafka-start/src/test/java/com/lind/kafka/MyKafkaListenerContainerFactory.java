package com.lind.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 消费者offset和ack处理工厂类
 * @Author qizhentao
 * @Date 2020/7/16 10:55
 * @Version 1.0
 */
@Component
public class MyKafkaListenerContainerFactory {

	@Value("${spring.kafka.bootstrap-servers}")
	String host;

	public ConsumerFactory<String, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}

	public Map<String, Object> consumerConfigs() {
		Map<String, Object> propsMap = new HashMap<>();
		propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
		propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "60000");
		propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, "test3");
		propsMap.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "500");
		propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "4000");// poll拉消息时间间隔，如果到了这个时间还没有提交，才会拉新消息，如果已经提交了，这个时间就没用了
		propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "4");// poll每次拉消息的数量
		propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "20");
		return propsMap;
	}

	@Bean("manualKafkaListenerContainerFactory1")
	KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory1() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	/**
	 * 每个poll()，都需要ack应答，如果没有@Bean启名字，使用方法名（首字母小写）
	 */
	@Bean
	KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory1(
			ConsumerFactory<String, String> consumerFactory) {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

		factory.setConsumerFactory(consumerFactory);
		factory.setConcurrency(3); // 有多少个消费线程
		factory.getContainerProperties().setPollTimeout(50); // 当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后，距离上次提交时间大于TIME时提交
		factory.getContainerProperties().setGroupId("test2");
		// 当使用手动提交时必须设置ackMode为MANUAL,否则会报错No Acknowledgment available as an argument, the
		// listener container must have a MANUAL AckMode to populate the Acknowledgment.
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
		factory.getContainerProperties().setAckCount(10); // 当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后，被处理record数量大于等于COUNT时提交
		factory.getContainerProperties().setAckTime(5000);
		return factory;
	}

	/**
	 * MANUAL 当每一批poll()的数据被消费者监听器（ListenerConsumer）处理之后,
	 * 手动调用Acknowledgment.acknowledge()后提交
	 * @param consumerFactory
	 * @return
	 */
	@Bean("manualListenerContainerFactory")
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> manualListenerContainerFactory(
			ConsumerFactory<String, String> consumerFactory) {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.getContainerProperties().setPollTimeout(1500);
		factory.getContainerProperties().setGroupId("test3");
		factory.setBatchListener(true);
		// 配置手动提交offset
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
		factory.getContainerProperties().setAckTime(1000);
		return factory;
	}

}