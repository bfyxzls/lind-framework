package com.lind.kafka;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author lind
 * @date 2024/1/5 13:06
 * @since 1.0.0
 */
public class KafkaStreamQuickStart {

	public static void main(String[] args) {

		// kafka的配置信息
		Properties prop = new Properties();
		prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.10.132:9091,192.168.10.133:9092,192.168.10.134:9097");
		prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		prop.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-quickstart-test");
		// stream 构建器
		StreamsBuilder streamsBuilder = new StreamsBuilder();
		streamProcessor(streamsBuilder);
		// 创建kafkaStream对象
		KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), prop);
		// 开启流式计算
		kafkaStreams.start();
	}

	/**
	 * 流式计算 消息的内容：hello kafka hello world
	 * @param streamsBuilder
	 */
	private static void streamProcessor(StreamsBuilder streamsBuilder) {
		// 创建kstream对象，同时指定从那个topic中接收消息
		KStream<String, String> stream = streamsBuilder.stream("KC_LOGIN");
		KStream<String, String> serializedStream = stream.mapValues(jsonString -> {
			if (JSONUtil.parseObj(jsonString).containsKey("userId")) {
				String userId = JSONUtil.parseObj(jsonString).getStr("userId");
				return userId;
			}
			else {
				return "";
			}
		});

		KStream<String, String> loginTypeStream = stream.mapValues(jsonString -> {
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

		KStream<String, String> clientIdStream = stream.mapValues(jsonString -> {
			if (JSONUtil.parseObj(jsonString).containsKey("clientId")) {
				return JSONUtil.parseObj(jsonString).getStr("clientId");
			}
			else {
				return "";
			}
		});

		/**
		 * 处理消息的value
		 */
		clientIdStream.flatMapValues(new ValueMapper<String, Iterable<String>>() {
			@Override
			public Iterable<String> apply(String value) {
				return Arrays.asList(value.split(" "));
			}
		}).filter((key, value) -> !value.equals(""))
				// 按照value进行聚合处理
				.groupBy((key, value) -> value)// 这进而的value是kafka的消息内容
				// 时间窗口
				.windowedBy(TimeWindows.of(Duration.ofSeconds(60)))// 统计1分钟之内的窗口数据,感觉是30秒打印一次（相同的时间窗口向下游发了两个消息，数据内容是不同的，后发的数据更大，因为会与之前的累加）
				// 统计单词的个数
				.count()
				// 转换为kStream
				.toStream().map((key, value) -> {
					// key是分组的key,value是分组count的结果
					return new KeyValue<>(key.toString(), value.toString());
				})
				// 发送消息
				// .to("topic-out");//每30秒向topic-out发送一次数据
				.foreach((key, value) -> {
					System.out.println("key:" + key + " value:" + value);
				});
		/**
		 * 消息中会有根据时间窗口合并的数据，所以需要先分组 select login_type,window_start,window_end,max(count)
		 * FROM report_login_type where login_type='password' and create_at>='2024-01-10
		 * 14:00:00' group by login_type,window_start,window_end
		 */
	}

}
