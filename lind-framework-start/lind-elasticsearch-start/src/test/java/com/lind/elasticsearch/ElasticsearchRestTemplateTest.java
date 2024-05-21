package com.lind.elasticsearch;

import com.lind.elasticsearch.entity.EsDto;
import com.lind.elasticsearch.repository.EsRepo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@SpringBootTest
@Slf4j
public class ElasticsearchRestTemplateTest {

	@Autowired
	ElasticsearchRestTemplate elasticsearchRestTemplate;

	@Autowired
	EsRepo esRepo;

	// 通过实体构建索引,包含建立和更新mapping
	@Test
	public void createIndex() {
		IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(EsDto.class);
		indexOperations.putMapping(indexOperations.createMapping(EsDto.class));
	}

	@Test
	public void simpleQuery() {
		Query query = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).build();

		SearchHits<EsDto> searchHits = elasticsearchRestTemplate.search(query, EsDto.class);

		searchHits.getSearchHits().forEach(searchHit -> {
			System.out.println(searchHit.getContent().toString());
		});
	}

	@Test
	public void searchByName() {
		Query query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.termQuery("application", "lind")).build();

		SearchHits<EsDto> searchHits = elasticsearchRestTemplate.search(query, EsDto.class);

		searchHits.getSearchHits().forEach(searchHit -> {
			System.out.println(searchHit.getContent().toString());
		});
	}

}
