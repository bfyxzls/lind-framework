package com.lind.elasticsearch;

import com.lind.elasticsearch.entity.EsDto;
import com.lind.elasticsearch.repository.EsRepo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

/**
 * ElasticsearchRestTemplate提供建立索引和复杂的查询功能.
 */
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

	// matchQuery
	@Test
	public void simpleQuery() {
		Query query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("desc", "中国")).build();
		SearchHits<EsDto> searchHits = elasticsearchRestTemplate.search(query, EsDto.class);
		searchHits.getSearchHits().forEach(searchHit -> {
			System.out.println(searchHit.getContent());
		});
	}

	// termQuery
	@Test
	public void searchByName() {
		Query query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.termQuery("application", "lind")).build();
		SearchHits<EsDto> searchHits = elasticsearchRestTemplate.search(query, EsDto.class);
		searchHits.getSearchHits().forEach(searchHit -> {
			System.out.println(searchHit.getContent());
		});
	}

	// 分组聚合
	@Test
	public void groupBySex() {
		// 构建分组聚合查询条件
		Query searchQuery = new NativeSearchQueryBuilder()
				.addAggregation(AggregationBuilders.terms("group_by_sex").field("sex")).build();

		// 执行查询
		SearchHits<EsDto> searchHits = elasticsearchRestTemplate.search(searchQuery, EsDto.class);
		AggregationsContainer<?> aggregationsContainer = searchHits.getAggregations();
		if (aggregationsContainer != null) {
			Aggregations aggregations = (Aggregations) aggregationsContainer.aggregations();
			for (Aggregation aggregation : aggregations) {
				if (aggregation instanceof Terms && "group_by_sex".equals(aggregation.getName())) {
					for (Terms.Bucket entry : ((Terms) aggregation).getBuckets()) {
						String key = entry.getKeyAsString();
						long docCount = entry.getDocCount();
						System.out.println("Key: " + key + ", Doc count: " + docCount);
					}
				}
			}

		}

	}

}
