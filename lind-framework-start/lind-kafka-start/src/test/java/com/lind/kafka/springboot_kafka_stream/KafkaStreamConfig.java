package com.lind.kafka.springboot_kafka_stream;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * 通过重新注册KafkaStreamsConfiguration对象，设置自定配置参数
 *
 * @author lind
 * @date 2024/1/8 11:05
 * @since 1.0.0
 */
@Configuration
@EnableKafkaStreams
public class KafkaStreamConfig {

	private static final int MAX_MESSAGE_SIZE = 16 * 1024 * 1024;

	@Value("${spring.kafka.bootstrap-servers}")
	private String hosts;

	@Value("${spring.kafka.consumer.group-id}")
	private String group;

	@Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
	public KafkaStreamsConfiguration defaultKafkaStreamsConfig() {
		Map<String, Object> props = new HashMap<>();
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, hosts);
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, group + "_stream_aid");
		props.put(StreamsConfig.CLIENT_ID_CONFIG, group + "_stream_cid");
		props.put(StreamsConfig.RETRIES_CONFIG, 3);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");// 从最近的消息开始消费
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		return new KafkaStreamsConfiguration(props);
	}

}
