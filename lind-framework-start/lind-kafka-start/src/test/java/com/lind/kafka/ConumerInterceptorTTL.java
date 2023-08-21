package com.lind.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消费者拦截器，注意我们的配置使用的是com.lind.kafka.config.KafkaProperties类，需要在这里添加interceptorClasses配置项
 * 事实上，我们应该用生产者拦截器，在生产消息时，如果消费包含TTL，就将这个消息在TTL时间之后，再发到死信队列中。
 *
 * @author lind
 * @date 2023/3/21 13:45
 * @since 1.0.0
 */
@Slf4j
public class ConumerInterceptorTTL implements ConsumerInterceptor<String, String> {

	// long类型转byte[]
	public static long bytesToLong(byte[] b) {
		long l = ((long) b[0] << 56) & 0xFF00000000000000L;
		// 如果不强制转换为long，那么默认会当作int，导致最高32位丢失
		l |= ((long) b[1] << 48) & 0xFF000000000000L;
		l |= ((long) b[2] << 40) & 0xFF0000000000L;
		l |= ((long) b[3] << 32) & 0xFF00000000L;
		l |= ((long) b[4] << 24) & 0xFF000000L;
		l |= ((long) b[5] << 16) & 0xFF0000L;
		l |= ((long) b[6] << 8) & 0xFF00L;
		l |= (long) b[7] & 0xFFL;
		return l;
	}

	@Override
	public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {
		long now = System.currentTimeMillis();
		Map<TopicPartition, List<ConsumerRecord<String, String>>> newRecords = new HashMap<>();
		for (TopicPartition tp : records.partitions()) {
			List<ConsumerRecord<String, String>> tpRecords = records.records(tp);
			List<ConsumerRecord<String, String>> newTpRecords = new ArrayList<>();
			for (ConsumerRecord<String, String> record : tpRecords) {
				Headers headers = record.headers();
				long ttl = -1;
				for (Header header : headers) {
					if (header.key().equals("ttl")) {
						ttl = bytesToLong(header.value());
					}
				}
				// 消息超时判定[now()-record.timestamp()/1000/60/60]小时
				if (ttl > 0 && now - record.timestamp() < ttl * 1000) {
					// 可以放在死信队列中
					log.info("放到死信:{}", record);
				}
				else { // 没有设置TTL,不需要超时判定
					newTpRecords.add(record);
				}

			}
			if (!newRecords.isEmpty()) {
				newRecords.put(tp, newTpRecords);
			}
		}
		return new ConsumerRecords<>(newRecords);
	}

	@Override
	public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {
		offsets.forEach((tp, offset) -> System.out.println(tp + ":" + offset.offset()));
	}

	@Override
	public void close() {
	}

	@Override
	public void configure(Map<String, ?> configs) {

	}

}
