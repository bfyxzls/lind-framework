package com.lind.elasticsearch;

import com.lind.elasticsearch.entity.EsDto;
import com.lind.elasticsearch.entity.LogDto;
import com.lind.elasticsearch.repository.EsRepo;
import com.lind.elasticsearch.repository.LogDtoRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;

import java.util.UUID;

@SpringBootTest
@Slf4j
public class ElasticsearchRestTemplateTest {

	@Autowired
	ElasticsearchRestTemplate elasticsearchTemplate;

	@Autowired
	LogDtoRepo logDtoRepo;

	@Autowired
	EsRepo esRepo;

	@Test
	public void createIndex() {
		IndexOperations indexOperations = elasticsearchTemplate.indexOps(EsDto.class);
		indexOperations.putMapping(indexOperations.createMapping(EsDto.class));
		indexOperations = elasticsearchTemplate.indexOps(LogDto.class);
		indexOperations.putMapping(indexOperations.createMapping(LogDto.class));
	}

	// 保存失败
	@Test
	public void add() {
		LogDto logDto = new LogDto();
		logDto.setApplication("test");
		logDto.setId(UUID.randomUUID().toString());
		logDtoRepo.save(logDto);
	}

	@Test
	public void simpleQuery() {

		LogDto logDto = logDtoRepo.findById("opCRTY8BUYQH9SVfsGmA").orElse(null);
		log.info("logDto: {}", logDto);
		// BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		// queryBuilder.must(QueryBuilders.termsQuery("id",
		// Arrays.asList("554401577478131712", "554401577478131716")));
		// NativeSearchQuery searchQuery = new
		// NativeSearchQueryBuilder().withQuery(queryBuilder)
		// .withSourceFilter(new FetchSourceFilter(new String[] { "id" }, null)).build();
		//
		// List<Map> content =
		// elasticsearchTemplate.matchAllQuery().queryForList(searchQuery, Map.class,
		// IndexCoordinates.of("esdto"));
		// for (Map esDto : content) {
		// System.out.println(esDto);
		// }
	}

}
