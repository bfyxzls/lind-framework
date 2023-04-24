package com.lind.elasticsearch;

import com.lind.elasticsearch.entity.EsDto;
import com.lind.elasticsearch.entity.Reply;
import com.lind.elasticsearch.repository.EsRepo;
import com.lind.elasticsearch.util.EsPageUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.TopHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest()
@RunWith(SpringRunner.class) // 让测试在Spring容器环境下执行
@Slf4j
public class TestApp {

	public static final IndexCoordinates ESDTO = IndexCoordinates.of("esdto");

	@Autowired
	EsRepo testDao;

	@Autowired
	ElasticsearchRestTemplate elasticsearchTemplate;

	/**
	 * 索引初始化. 对于开启了@RunWith(SpringRunner.class)注入之后，这个索引初始化的代码就不需要了.
	 */
	@Test
	public void initIndexMapping() {
		System.out.println("init...");
		elasticsearchTemplate.deleteIndex(EsDto.class);
		if (!elasticsearchTemplate.indexExists(EsDto.class)) {
			// 建立空索引
			if (elasticsearchTemplate.createIndex(EsDto.class)) {
				// 建立mapping
				elasticsearchTemplate.putMapping(EsDto.class);
			}
		}
	}

	@Test
	public void insert() {
		List<EsDto> list = new ArrayList<>();
		list.add(new EsDto("张三", 41, 1, "北京丰台东高地", "中国生产吴虹飞", new EsDto.Address("北京", "北京", "丰台")));
		list.add(new EsDto("李四", 31, 1, "北京房山张谢", "中国生产吴虹飞", new EsDto.Address("北京", "北京", "房山")));
		list.add(new EsDto("王洁", 21, 0, "河北唐山周庄", "中国生产吴虹飞", new EsDto.Address("河北", "唐山", "滦县")));
		list.add(new EsDto("赵新", 30, 0, "河北唐山周庄", "中国生产吴虹飞", new EsDto.Address("河北", "唐山", "滦县")));
		list.add(new EsDto("赵一迪", 12, 1, "河北唐山周庄", "中国生产吴虹飞", new EsDto.Address("河北", "唐山", "滦县")));
		list.add(new EsDto("王使", 32, 1, "北京房山张谢", "中国生产吴虹飞", new EsDto.Address("北京", "北京", "房山")));

		testDao.saveAll(list);
	}

	/**
	 * 分词查询
	 */
	@Test
	public void matchQuery() {
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.matchQuery("memo", "中国"));
		Pageable pageable = EsPageUtils.getPageable(0, 10);
		NativeSearchQuery searchQuery = queryBuilder.withQuery(boolQueryBuilder).withSourceFilter(null) // 返回所有字段
				.withPageable(pageable).build();
		Page<EsDto> page = testDao.search(searchQuery);
		List<EsDto> content = page.getContent();
		log.info("content.len:{}", content.size());
		for (EsDto dto : content) {
			log.info(dto.toString());
		}
	}

	/**
	 * 精确查找
	 */
	@Test
	public void termQuery() {
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// matchQuery先分词再检索,termQuery精确检索，
		// matchQuery将搜索词分词，再与目标查询字段进行匹配，若分词中的任意一个词与目标字段匹配上，则可查询到。
		// 分词：把一个长的句子，分成若干个词，进行检索
		// termQuery本会对查询条件进行分词，但如果你的查询内容字段是分词的，它是对进行内容内容查找的
		// termQuery如果希望对内容也进行精确检索，需要加keyword关键字
		// must表示与，should表示或
		boolQueryBuilder.must(QueryBuilders.termQuery("memo.keyword", "中国"));
		Pageable pageable = EsPageUtils.getPageable(0, 10);
		String[] fieldNames = new String[] { "name", "age", "memo" };
		NativeSearchQuery searchQuery = queryBuilder.withQuery(boolQueryBuilder)
				.withSourceFilter(new FetchSourceFilter(fieldNames, null)).withSourceFilter(null) // 返回所有字段
				.withPageable(pageable).build();
		Page<EsDto> page = testDao.search(searchQuery);
		List<EsDto> content = page.getContent();
		log.info("content.len:{}", content.size());
		if (content.size() > 0) {
			for (EsDto dto : content) {
				log.info(dto.toString());
			}
		}
	}

	@Test
	public void termInQuery() {

		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder.must(QueryBuilders.termsQuery("id", Arrays.asList("554401577478131712", "554401577478131716")));
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder)
				.withSourceFilter(new FetchSourceFilter(new String[] { "id" }, null)).build();

		List<EsDto> content = elasticsearchTemplate.queryForList(searchQuery, EsDto.class, ESDTO);
		for (EsDto esDto : content) {
			System.out.println(esDto);
		}
	}

	/**
	 * 聚合.
	 */
	@Test
	public void aggregate() {
		// 创建一个查询条件对象
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		// 拼接查询条件
		queryBuilder.should(QueryBuilders.termQuery("creator", "1"));
		// 创建聚合查询条件
		TermsAggregationBuilder sexAgg = AggregationBuilders.terms("sex") // 别名名
				.field("sex");// 字段
		SumAggregationBuilder sexSumAgg = AggregationBuilders.sum("ageSum") // 别名
				.field("age"); // 字段
		sexAgg.subAggregation(sexSumAgg);
		// 创建查询对象
		NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(queryBuilder) // 添加查询条件
				.addAggregation(sexAgg) // 添加聚合条件
				.withPageable(PageRequest.of(0, 1)) // 符合查询条件的文档分页，如果文档比较大，可以把这个分页改小（不是聚合的分页）
				.build();
		// 执行查询
		AggregatedPage<EsDto> testEntities = elasticsearchTemplate.queryForPage(build, EsDto.class, ESDTO);
		// 取出聚合结果
		Aggregations entitiesAggregations = testEntities.getAggregations();
		Terms terms = (Terms) entitiesAggregations.asMap().get("sex");

		// 遍历取出聚合字段列的值，与对应的数量
		for (Terms.Bucket bucket : terms.getBuckets()) {
			ParsedSum parsedSum = bucket.getAggregations().get("ageSum");// 注意从bucket而不是searchResponse
			System.out.println(
					bucket.getKeyAsString() + "\t" + bucket.getDocCount() + "\t" + parsedSum.getValueAsString());

		}
	}

	/**
	 * 嵌套聚合.
	 */
	@Test
	public void aggregateNest() {
		// 创建一个查询条件对象
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		// 拼接查询条件
		queryBuilder.should(QueryBuilders.termQuery("creator", "1"));

		// 创建聚合查询条件
		TermsAggregationBuilder sexAgg = AggregationBuilders.terms("sex").field("sex");// keyword表示不使用分词进行聚合,全字匹配
		TermsAggregationBuilder descAgg = AggregationBuilders.terms("address").field("address.province.keyword");// keyword表示不使用分词进行聚合,全字匹配
		SumAggregationBuilder ageSumAgg = AggregationBuilders.sum("ageSum").field("age");
		// 嵌套
		descAgg.subAggregation(ageSumAgg);
		sexAgg.subAggregation(descAgg);

		// 创建查询对象
		NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(queryBuilder) // 添加查询条件
				.addAggregation(sexAgg) // 添加聚合条件
				.withPageable(PageRequest.of(0, 1)) // 符合查询条件的文档分页，如果文档比较大，可以把这个分页改小（不是聚合的分页）
				.build();
		// 执行查询
		AggregatedPage<EsDto> testEntities = elasticsearchTemplate.queryForPage(build, EsDto.class, ESDTO);

		// 取出聚合结果
		Aggregations entitiesAggregations = testEntities.getAggregations();
		Terms terms = (Terms) entitiesAggregations.asMap().get("sex");

		// 遍历取出聚合字段列的值，与对应的数量
		for (Terms.Bucket bucket : terms.getBuckets()) {
			Terms descTerms = (Terms) bucket.getAggregations().asMap().get("address");
			for (Terms.Bucket descTermsBucket : descTerms.getBuckets()) {
				ParsedSum parsedSum = descTermsBucket.getAggregations().get("ageSum");// 注意从bucket而不是searchResponse
				System.out.println(
						bucket.getKeyAsString() + "\t" + bucket.getDocCount() + "\t" + descTermsBucket.getKeyAsString()
								+ "\t" + descTermsBucket.getDocCount() + "\t" + parsedSum.getValueAsString());
			}
		}
	}

	/**
	 * 高亮检索.
	 */
	@Test
	public void highSearch() {
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.matchQuery("desc", "hello"));// matchQuery先分词再检索,termQuery精确检索
		Pageable pageable = EsPageUtils.getPageable(0, 10);

		// 高亮检索
		String preTag = "<font color='#dd4b39'>";
		String postTag = "</font>";

		NativeSearchQuery searchQuery = queryBuilder.withQuery(boolQueryBuilder).withSourceFilter(null) // 返回所有字段
				.withHighlightFields(new HighlightBuilder.Field("desc").preTags(preTag).postTags(postTag)
						.numOfFragments(5).fragmentSize(100).noMatchSize(5)) // 高亮显示
				.withPageable(pageable).build();

	}

	/**
	 * 更新.
	 */
	@Test
	public void update() {
		String id = "303280141742641152";
		Map<String, Object> sourceMap = new HashMap<>();
		sourceMap.put("name", "占岭");
		IndexRequest indexRequest = new IndexRequest();
		indexRequest.source(sourceMap);
		UpdateQuery updateQuery = UpdateQuery.builder(id).withDocument(Document.from(sourceMap)).build();
		elasticsearchTemplate.update(updateQuery, ESDTO);
	}

	/**
	 * 获取.
	 */
	@Test
	public void detail() {
		String id = "303280141742641152";
		Optional<EsDto> optional = testDao.findById(id);
		Assert.notNull(optional.orElse(null));
		log.info(optional.get().toString());
	}

	@Test
	public void aggregateTopHitsTotal() {
		// 创建一个查询条件对象
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		// 拼接查询条件
		queryBuilder.should(QueryBuilders.termQuery("createUser", "1"));
		// 创建聚合查询条件
		TermsAggregationBuilder operateTypeAggBuilder = AggregationBuilders.terms("commentId").field("commentId")
				.size(10000);
		operateTypeAggBuilder
				.subAggregation(AggregationBuilders.topHits("top").size(2).fetchSource("content", "delFlag"));
		// 创建查询对象
		NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(queryBuilder) // 添加查询条件
				.addAggregation(operateTypeAggBuilder) // 添加聚合条件
				.withPageable(PageRequest.of(0, 1)) // 符合查询条件的文档分页，如果文档比较大，可以把这个分页改小（不是聚合的分页）
				.build();

		AggregatedPage<Reply> testEntities = elasticsearchTemplate.queryForPage(build, Reply.class, ESDTO);
		Aggregations entitiesAggregations = testEntities.getAggregations();
		Terms terms = (Terms) entitiesAggregations.asMap().get("commentId");
		// 遍历取出聚合字段列的值，与对应的数量
		for (Terms.Bucket bucket : terms.getBuckets()) {
			// We ask for top_hits for each bucket
			log.info("commentId:{}", bucket.getKeyAsString());
			TopHits topHits = bucket.getAggregations().get("top");
			for (SearchHit hit : topHits.getHits().getHits()) {
				log.info(" -> id [{}], _source [{}]", hit.getId(), hit.getSourceAsString());
			}

		}

	}

}
