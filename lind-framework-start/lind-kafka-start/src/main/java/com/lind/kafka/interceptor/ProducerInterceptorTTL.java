package com.lind.kafka.interceptor;

import com.lind.kafka.util.LindTimeWheel;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.lind.kafka.util.ConvertUtils.toLong;

/**
 * 生产环境的拦截器.
 *
 * @author lind
 * @date 2023/8/20 19:44
 * @since 1.0.0
 */
@Component
public class ProducerInterceptorTTL implements ProducerInterceptor<Integer, String>, ApplicationContextAware {

	// 消息延时,单位秒
	public static String TTL = "ttl";

	// 死信队列,延时后发送到的队列，我们称为死信队列
	public static String DEAD_TOPIC = "dead_topic";

	// 静态化的上下文，用于获取bean，因为ConsumerInterceptor是通过反射创建的，不是spring ioc管理的，所以无法通过注入的方式获取bean
	private static ApplicationContext applicationContext;

	// 时间轮，用于延时发送消息
	private static final LindTimeWheel timeWheel = new LindTimeWheel(1000, 8);

	@Override
	public ProducerRecord onSend(ProducerRecord<Integer, String> record) {
		final String topic = record.topic();
		final Integer partition = record.partition();
		final Integer key = record.key();
		final String value = record.value();
		final Long timestamp = record.timestamp();
		final Headers headers = record.headers();
		long ttl = -1;
		String deadTopic = null;
		for (Header header : headers) {
			if (header.key().equals(TTL)) {
				ttl = toLong(header.value());
			}
			if (header.key().equals(DEAD_TOPIC)) {
				deadTopic = new String(header.value());
			}
		}
		// 消息超时判定
		if (deadTopic != null && ttl > 0) {
			// 可以放在死信队列中
			String finalDeadTopic = deadTopic;
			long finalTtl = ttl * 1000;
			timeWheel.addTask(() -> {
				System.out.println("消息超时了," + finalTtl + "需要发到topic:" + record.key());
				KafkaTemplate kafkaTemplate = applicationContext.getBean(KafkaTemplate.class);
				kafkaTemplate.send(finalDeadTopic, record.value());

			}, finalTtl);
		}
		// 拦截器拦下来之后改变原来的消息内容
		ProducerRecord<Integer, String> newRecord = new ProducerRecord<Integer, String>(topic, partition, timestamp,
				key, value, headers);
		// 传递新的消息
		return newRecord;

	}

	@Override
	public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> map) {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ProducerInterceptorTTL.applicationContext = applicationContext;
	}

}
