package com.lind.elasticsearch.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lind.elasticsearch.entity.DataRecord;
import com.lind.elasticsearch.entity.DateDeserialize;
import com.lind.elasticsearch.entity.DateSerializer;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * ES数据保存操作，主要用于操作DateRecord动态数据，使用json接口.
 */
public class EsDataUtils {

	private static final String DEFAULT_TYPE = "info";

	private static final ObjectMapper objectMapper = createObjectMapper();

	/**
	 * 初始化objectMapper.
	 * @return
	 */
	private static ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		// 常用配置
		objectMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
		objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 大小写脱敏 默认为false 需要改为true
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		SimpleModule simpleModule = new SimpleModule();
		// 时间序列化
		simpleModule.addSerializer(Date.class, new DateSerializer());
		// 时间反序列化
		simpleModule.addDeserializer(Date.class, new DateDeserialize());
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(simpleModule);
		return objectMapper;
	}

	/**
	 * 插入数据
	 * @param client
	 * @param indexName
	 * @param dataRecord
	 * @return
	 */
	public static String saveOrUpdate(RestHighLevelClient client, String indexName, DataRecord dataRecord)
			throws IOException {
		Objects.requireNonNull(indexName, "索引名不能为空");
		Objects.requireNonNull(dataRecord, "数据不能为null");

		IndexRequest indexRequest;
		String id = dataRecord.getId();

		if (StringUtils.isNotBlank(id)) {
			indexRequest = new IndexRequest(indexName, DEFAULT_TYPE, id);
		}
		else {

			indexRequest = new IndexRequest(indexName, DEFAULT_TYPE);
		}

		// 填充数据
		indexRequest.source(objectMapper.writeValueAsString(dataRecord), XContentType.JSON);

		String documentId = client.index(indexRequest, RequestOptions.DEFAULT).getId();

		return documentId;
	}

}
