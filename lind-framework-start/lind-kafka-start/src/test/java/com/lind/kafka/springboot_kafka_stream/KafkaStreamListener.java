package com.lind.kafka.springboot_kafka_stream;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

/**
 * @author lind
 * @date 2024/1/8 11:11
 * @since 1.0.0
 */
@Configuration
@Slf4j
public class KafkaStreamListener {

	@KafkaListener(topics = "topic-out")
	public void listen(ConsumerRecord<String, String> record) {
		// 将时间戳转换为本地日期时间
		LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(record.timestamp()),
				ZoneId.systemDefault());
		log.info("key:{},value:{},time:{}", record.key(), record.value(), dateTime);
	}

	@Bean
	public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
		KStream<String, String> stream = streamsBuilder.stream("KC_LOGIN");
		KStream<String, String> serializedStream = stream.mapValues(jsonString -> {
			// 分组依据
			if (JSONUtil.parseObj(jsonString).containsKey("details")) {
				JSONObject details = JSONUtil.parseObj(jsonString).getJSONObject("details");
				if (details.containsKey("loginType")) {
					String loginType = details.getStr("loginType");
					return loginType;
				}
				return "";
			}
			else {
				return "";
			}
		});
		/**
		 * 处理消息的value 感觉kafka-stream 是每30秒输出一次数据，而时间窗口只是统计数据的一个范围。
		 */
		serializedStream.flatMapValues(new ValueMapper<String, Iterable<String>>() {
			@Override
			public Iterable<String> apply(String value) {
				return Arrays.asList(value.split(" "));
			}
		}).filter((key, value) -> !value.equals(""))
				// 按照value进行聚合处理
				.groupBy((key, value) -> value)// 这进而的value是kafka的消息内容
				// 时间窗口
				.windowedBy(TimeWindows.of(Duration.ofSeconds(60)))
				// 统计单词的个数
				.count()
				// 转换为kStream
				.toStream().map((key, value) -> {
					// key是分组的key,value是分组count的结果
					return new KeyValue<>(key.key().toString(), value.toString());
				})
				// 发送消息
				.to("topic-out");// 每30秒向topic-out发送一次数据
		return stream;
	}

}
