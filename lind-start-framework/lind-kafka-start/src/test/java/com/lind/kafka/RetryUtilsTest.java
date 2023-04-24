package com.lind.kafka;

import com.lind.kafka.util.RetryUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class RetryUtilsTest {

	/***
	 * 手动提交消息，并带有消息重试能力.
	 * @param consumer
	 * @param ack
	 */
	@KafkaListener(topics = "sleep3", containerFactory = "manualKafkaListenerContainerFactory1")
	public void ConsumerGroupId2(ConsumerRecord<String, String> consumer, Acknowledgment ack) {
		RetryUtils.reDo(5, ack, () -> {
			int a = 0;
			int b = 100 / a;
		});
	}

}
