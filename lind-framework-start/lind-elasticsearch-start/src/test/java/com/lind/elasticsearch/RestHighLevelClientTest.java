package com.lind.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.lind.elasticsearch.entity.DataRecord;
import com.lind.elasticsearch.entity.EsDto;
import com.lind.elasticsearch.util.EsDataUtils;
import com.lind.elasticsearch.util.ObjectToMapUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author lind
 * @date 2024/5/7 16:40
 * @since 1.0.0
 */
@SpringBootTest
public class RestHighLevelClientTest {

	static final String INDEX = "edit-backend-prod";

	@Autowired
	private RestHighLevelClient client;

	// 这种保存数据时，如果没有索引，它建立的索引的mapping是不符合预期的，不要自动生成；或者使用
	// ElasticsearchRestTemplate提供的putMapping()方法，根据实体的注解来准确生成mapping
	@Test
	public void save() throws IOException {
		EsDto esDto = new EsDto();
		esDto.setName("lind");
		esDto.setId(UUID.randomUUID().toString());
		IndexRequest request = new IndexRequest("edit-backend-prod").id(String.valueOf(esDto.getId()))
				.source(ObjectToMapUtils.beanToMap(esDto));
		IndexResponse response = client.index(request, RequestOptions.DEFAULT);
		System.out.println(JSONObject.toJSON(response));
	}

	@Test
	public void update() throws IOException {
		EsDto esDto = new EsDto();
		esDto.setName("test2");
		esDto.setId(UUID.randomUUID().toString());
		UpdateRequest request = new UpdateRequest(INDEX, "d2106425-17f1-431d-9908-e1e03af3c8d8")
				.doc(ObjectToMapUtils.beanToMap(esDto));
		client.update(request, RequestOptions.DEFAULT);
	}

	@Test
	public void dataRecordUpdate() throws IOException {
		EsDto logDto = new EsDto();
		logDto.setName("test2-1-2-3");
		logDto.setId("79c7fc29-7292-46b4-b4fd-ee33caa948a3");// 映射到es的_id才有效
		EsDataUtils.saveOrUpdate(client, "edit-backend-prod", new DataRecord(ObjectToMapUtils.beanToMap(logDto)));
	}

	@Test
	public void deleteById() throws IOException {
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, "d2106425-17f1-431d-9908-e1e03af3c8d8");
		client.delete(deleteRequest, RequestOptions.DEFAULT);
	}

	@Test
	public void deleteByIds() throws IOException {
		String[] ids = new String[] { "7--mVY8BkR5rSzx0wiR3", "7u-jVY8BkR5rSzx0UCQl", "8O-oVY8BkR5rSzx0_SQs" };
		DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(INDEX);
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		boolQueryBuilder.must(QueryBuilders.idsQuery().addIds(ids));
		deleteByQueryRequest.setQuery(boolQueryBuilder);
		client.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
	}

	@Test
	public void getEs() throws IOException {
		String id = "d2106425-17f1-431d-9908-e1e03af3c8d8";
		GetRequest getRequest = new GetRequest(INDEX, id);
		GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
		System.out.println(response);
	}

	@Test
	public void getTypeGroupBy() throws IOException {
		Map<String, Long> map = new LinkedHashMap<>();
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("record_traffic*");
		// 指定分组字段,terms指定别名,field指定字段名
		TermsAggregationBuilder aggregation = AggregationBuilders.terms("username")
				// 聚合字段名
				.field("username.keyword").size(100)
				// 降序
				.order(BucketOrder.count(false));
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.aggregation(aggregation);
		// 执行查询
		searchRequest.source(searchSourceBuilder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		Terms byAgeAggregation = response.getAggregations().get("content");
		List<? extends Terms.Bucket> buckets = byAgeAggregation.getBuckets();
		for (Terms.Bucket buck : buckets) {
			map.put(buck.getKeyAsString(), buck.getDocCount());
		}
		System.out.println("map=" + map);
	}

}
