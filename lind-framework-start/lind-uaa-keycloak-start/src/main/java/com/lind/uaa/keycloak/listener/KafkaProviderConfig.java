package com.lind.uaa.keycloak.listener;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 自动装配管理，依赖于kc-kafka.enable的值.
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(value = "keycloak.uaa.kafka.enabled", matchIfMissing = true)
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaProviderConfig {

	@Autowired
	KafkaProperties kafkaProperties;

	@Bean
	@ConditionalOnBean(DefaultKafkaProducerFactory.class)
	public KafkaTemplate<String, String> kafkaTemplate(
			DefaultKafkaProducerFactory<String, String> kafkaProducerFactory) {
		return new KafkaTemplate<>(kafkaProducerFactory, false);
	}

	@Bean
	public DefaultKafkaConsumerFactory<String, String> kafkaConsumerFactory() {
		Map<String, Object> paraMap = new HashMap<>();
		paraMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
		paraMap.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
		paraMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getKeyDeserializer());
		paraMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getValueDeserializer());
		paraMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getEnableAutoCommit());
		paraMap.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "60000");
		paraMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "20000");
		paraMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "3");
		paraMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "50000");
		paraMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());
		paraMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		return new DefaultKafkaConsumerFactory<>(paraMap);
	}

	/**
	 * 消费者自定义配置.
	 * @return
	 */
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> manulKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(kafkaConsumerFactory());
		if (StringUtils.isEmpty(kafkaProperties.getConcurrency())) {
			factory.setConcurrency(30);
		}
		else {
			factory.setConcurrency(Integer.parseInt(kafkaProperties.getConcurrency()));
		}
		factory.getContainerProperties().setPollTimeout(1000);
		// 手动提交的时候需要设置AckMode为 MANUAL或MANUAL_IMMEDIATE
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

		return factory;
	}

}
