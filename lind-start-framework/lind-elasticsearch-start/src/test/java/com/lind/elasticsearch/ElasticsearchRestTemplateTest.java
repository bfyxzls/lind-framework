package com.lind.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest()
@RunWith(SpringRunner.class) // 让测试在Spring容器环境下执行
@Slf4j
public class ElasticsearchRestTemplateTest {

	@Autowired
	ElasticsearchRestTemplate elasticsearchTemplate;

	@Test
	public void simpleQuery() {
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder.must(QueryBuilders.termsQuery("id", Arrays.asList("554401577478131712", "554401577478131716")));
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder)
				.withSourceFilter(new FetchSourceFilter(new String[] { "id" }, null)).build();

		List<Map> content = elasticsearchTemplate.queryForList(searchQuery, Map.class, IndexCoordinates.of("esdto"));
		for (Map esDto : content) {
			System.out.println(esDto);
		}
	}

}
