package com.lind.kafka.interceptor;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.MDC;

import java.util.Map;

/**
 * @author lind
 * @date 2023/4/2 15:04
 * @since 1.0.0
 */
public class TraceConsumerInterceptor implements ConsumerInterceptor<String, String> {

	@Override
	public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> consumerRecords) {
		consumerRecords.forEach(o -> {
			o.headers().forEach(header -> {
				if (header.key().equals("traceId")) {
					System.out.println(header.key() + ":" + header.value());
					MDC.put("traceId", new String(header.value()));
				}
			});
		});
		return consumerRecords;
	}

	@Override
	public void onCommit(Map<TopicPartition, OffsetAndMetadata> map) {

	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> map) {

	}

}
