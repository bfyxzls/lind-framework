package com.lind.hbase.config;

import com.lind.hbase.api.HBaseOperations;
import com.lind.hbase.service.HBaseService;
import com.lind.hbase.service.HBaseServiceImpl;
import com.lind.hbase.template.HBaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties({ HBaseProperties.class })
@ConditionalOnProperty(value = "hbase.enabled", matchIfMissing = true)
public class HBaseAutoConfiguration {

	private final HBaseProperties properties;

	public HBaseAutoConfiguration(HBaseProperties properties) {
		this.properties = properties;
	}

	private boolean isEmptyOrWhiteSpace(String str) {
		return str == null || str.length() == 0 || str.trim().length() == 0;
	}

	/**
	 * HBase 连接
	 * @return
	 * @throws IOException
	 */
	@Bean
	public Connection connection() throws IOException {
		try {
			Configuration conf = HBaseConfiguration.create();
			if (!isEmptyOrWhiteSpace(properties.getZookeeper().getQuorum())) {
				// zookeeper集群地址,多个以逗号分隔
				conf.set("hbase.zookeeper.quorum", properties.getZookeeper().getQuorum());
			}

			if (Objects.nonNull(properties.getClient().getPause())) {
				// 失败重试时的等待时间，随着重试次数越多,重试等待时间越长，单位ms，默认100ms
				conf.set("hbase.client.pause", String.valueOf(properties.getClient().getPause()));
			}
			if (Objects.nonNull(properties.getClient().getRetries().getNumbers())) {
				// 失败重试次数,默认3次
				conf.set("hbase.client.retries.number",
						String.valueOf(properties.getClient().getRetries().getNumbers()));
			}
			if (Objects.nonNull(properties.getRpc().getTimeout())) {
				// 表示一次RPC请求的超时时间。如果某次RPC时间超过该值，客户端就会主动关闭socket，单位ms
				conf.set("hbase.rpc.timeout", String.valueOf(properties.getRpc().getTimeout()));
			}
			if (Objects.nonNull(properties.getClient().getOperation().getTimeout())) {
				// 该参数表示HBase客户端发起一次数据操作直至得到响应之间总的超时时间，单位ms
				// 数据操作类型包括get、append、increment、delete、put等
				conf.set("hbase.client.operation.timeout",
						String.valueOf(properties.getClient().getOperation().getTimeout()));
			}
			if (Objects.nonNull(properties.getClient().getScanner().getTimeout())) {
				// 该参数是表示HBase客户端发起一次scan操作的rpc调用至得到响应之间总的超时时间
				conf.set("hbase.client.scanner.timeout.period",
						String.valueOf(properties.getClient().getScanner().getTimeout()));
			}

			if (!isEmptyOrWhiteSpace(properties.getUsername())) {
				System.setProperty("HADOOP_USER_NAME", properties.getUsername());
			}

			return ConnectionFactory.createConnection(conf);
		}
		catch (IOException e) {
			log.error("创建HBase连接失败：{}", e.getMessage(), e);
			throw e;
		}
	}

	@Bean
	@ConditionalOnMissingBean(HBaseOperations.class)
	public HBaseTemplate hbaseTemplate(Connection connection) throws IOException {
		HBaseTemplate hbaseTemplate = new HBaseTemplate(connection);
		log.info("HBase 连接成功。");
		return hbaseTemplate;
	}

	@Bean
	@ConditionalOnMissingBean(HBaseService.class)
	public HBaseService hbaseService(HBaseTemplate template) {
		return new HBaseServiceImpl(template, properties.getNameSpace());
	}

}
