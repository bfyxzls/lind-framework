package com.lind.kafka.apache;

import com.lind.kafka.ConumerInterceptorTTL;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Test {

	private final static String TOPIC = "test-ttl";

	private final static String GROUP = "group1";

	@org.junit.Test
	public void publish() {
		// 没有配置key，消息都分发到多个partition
		Producer.publishEvent("lind_demo", "hello1");
		Producer.publishEvent("lind_demo", "hello2");
		Producer.publishEvent("lind_demo", "hello3");
	}

	@org.junit.Test
	public void publishKey() {
		// 配置key之后，消息发到同一个partition，从而保证了消息的有序性
		Producer.publishEvent("topic001", "2", "hello4");
		Producer.publishEvent("topic001", "2", "hello5");
		Producer.publishEvent("topic001", "2", "hello6");

	}

	@org.junit.Test
	public void consumer() {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.60.146:9092");
		// 消费分组名
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP);
		// 是否自动提交offset，默认就是true
		// properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		// 自动提交offset的间隔时间, 每1000毫秒提交一次
		// properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		// 是否自动提交offset
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		// 当消费主题的是一个新的消费组，或者指定offset的消费方式，offset不存在，那么应该如何消费
		// latest(默认) ：只消费自己启动之后才发送到主题的消息
		// earliest：第一次从头开始消费，以后按照消费offset记录继续消费，这个需要区别于consumer.seekToBeginning(每次都从头开始消费)
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		// consumer给broker发送心跳的间隔时间，broker接收到心跳如果此时有rebalance发生会通过心跳响应将rebalance方案下发给consumer，这个时间可以稍微短一点
		properties.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 1000);
		// broker接收不到一个consumer的心跳, 持续该时间,
		// 就认为故障了，会将其踢出消费组，对应的Partition也会被重新分配给其他consumer，默认是10秒
		properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10 * 1000);
		// 一次poll最大拉取消息的条数，如果消费者处理速度很快，可以设置大点，如果处理速度一般，可以设置小点
		properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
		// 如果两次poll操作间隔超过了这个时间，broker就会认为这个consumer处理能力太弱，会将其踢出消费组，将分区分配给别的consumer消费
		properties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 30 * 1000);
		// 把消息的key从字节数组反序列化为字符串
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		// 把消息的value从字节数组反序列化为字符串
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, ConumerInterceptorTTL.class.getName());

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

		// 订阅主题，如果新加入的消费者需要消费之前的消息，那需要建立新的分组
		consumer.subscribe(Collections.singletonList(TOPIC));
		while (true) {
			// poll(duration): 长轮询, 即duration时段内没拿到消息就一直重复尝试拿, 知道时间到或者拿到消息才返回结果
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
			for (ConsumerRecord<String, String> record : records) {
				System.out.printf("收到消息：partition = %d,offset = %d, key = %s, value = %s%n", record.partition(),
						record.offset(), record.key(), record.value());
			}

			// if (records.count() > 0) {
			// // 手动同步提交offset，当前线程会阻塞直到offset提交成功, 一般使用同步提交，因为提交之后一般也没有什么逻辑代码了
			// // consumer.commitSync();
			// // 手动异步提交offset，当前线程提交offset不会阻塞，可以继续处理后面的程序逻辑
			// consumer.commitAsync(new OffsetCommitCallback() {
			// @Override
			// public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets,
			// Exception exception) {
			// if (exception != null) {
			// System.err.println("Commit failed for " + offsets);
			// System.err.println("Commit failed exception: " +
			// Arrays.toString(exception.getStackTrace()));
			// }
			// }
			// });
			// }
		}
	}

}
