package com.lind.uaa.keycloak.listener;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义的kc的kafka连接串.
 */
@ConfigurationProperties("kc-kafka")
public class KafkaProperties {

	/**
	 * 是否开启kafka功能.
	 */
	private Boolean enabled = true;

	/**
	 * kafka生产者ack机制
	 */
	private String acks = "1";

	/**
	 * kafka并发数量
	 */
	private String concurrency = "12";

	private String bootstrapServers;

	private String groupID;

	private int retries = 3;

	private int bufferMemory = 1024 * 1024 * 32;

	private int lingerMS = 5;

	private boolean enableAutoCommit = true;

	private int batchSize = 1024 * 16;

	/**
	 * Key序列化
	 */
	private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";

	/**
	 * Value序列化
	 */
	private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";

	/**
	 * Key反序列化
	 */
	private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

	/**
	 * Value反序列化
	 */
	private String valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";

	/**
	 * 消费者配置
	 */
	private Consumer consumer = new Consumer();

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getAcks() {
		return acks;
	}

	public void setAcks(String acks) {
		this.acks = acks;
	}

	public String getConcurrency() {
		return concurrency;
	}

	public void setConcurrency(String concurrency) {
		this.concurrency = concurrency;
	}

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public Consumer getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public int getBufferMemory() {
		return bufferMemory;
	}

	public void setBufferMemory(int bufferMemory) {
		this.bufferMemory = bufferMemory;
	}

	public int getLingerMS() {
		return lingerMS;
	}

	public void setLingerMS(int lingerMS) {
		this.lingerMS = lingerMS;
	}

	public boolean getEnableAutoCommit() {
		return enableAutoCommit;
	}

	public void setEnableAutoCommit(boolean enableAutoCommit) {
		this.enableAutoCommit = enableAutoCommit;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public String getKeySerializer() {
		return keySerializer;
	}

	public void setKeySerializer(String keySerializer) {
		this.keySerializer = keySerializer;
	}

	public String getValueSerializer() {
		return valueSerializer;
	}

	public void setValueSerializer(String valueSerializer) {
		this.valueSerializer = valueSerializer;
	}

	public String getKeyDeserializer() {
		return keyDeserializer;
	}

	public void setKeyDeserializer(String keyDeserializer) {
		this.keyDeserializer = keyDeserializer;
	}

	public String getValueDeserializer() {
		return valueDeserializer;
	}

	public void setValueDeserializer(String valueDeserializer) {
		this.valueDeserializer = valueDeserializer;
	}

	@Data
	public class Consumer {

		/**
		 * 消费者的组id，默认是default
		 */
		private String groupId = "default";

		/**
		 * 发生消费错误时重试次数，超过这个次数后会提交offset，需自行处理
		 */
		private int errorRetry = 3;

		private String autoOffsetReset = "latest";

	}

}
