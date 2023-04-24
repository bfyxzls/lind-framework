package com.lind.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 文档更新
 */
public class IndexRequestTest {

	@Autowired
	ElasticsearchRestTemplate elasticsearchRestTemplate;

	@Autowired
	RestHighLevelClient client;

	@SneakyThrows
	@Test
	public void indexUpdate() {
		ObjectMapper objectMapper = new ObjectMapper();
		IndexRequest indexRequest = null;
		String id = "554401577478131712";
		if (StringUtils.isNotBlank(id)) {
			indexRequest = new IndexRequest("esdto", "esdto", id);
		}
		else {
			indexRequest = new IndexRequest("esdto", "esdto");
		}

		// 填充数据
		Map<String, Object> hash = new HashMap<>();
		hash.put("name", "lind");
		hash.put("id", id);
		indexRequest.source(objectMapper.writeValueAsString(hash), XContentType.JSON);
		RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
				RestClient.builder(new HttpHost("192.168.10.41", 9200)));
		String documentId = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT).getId();

		System.out.println("documentId=" + documentId);
	}

}
