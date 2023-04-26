package com.lind.elasticsearch.util;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Slf4j
public class EsIndexUtils implements ConstUtils {

	private static final String SHARDS_SETTING = "index.number_of_shards";

	private static final String REPLICAS_SETTING = "index.number_of_replicas";

	private static final String READ_ONLY_ALLOW_DELETE = "index.blocks.read_only_allow_delete";

	/**
	 * 索引的type
	 */
	private static final String DEFAULT_INDEX_TYPE = "info";

	private static <T extends Object> T getValueOrDefault(T value, T defaultValue) {
		if (Objects.nonNull(value)) {
			return value;
		}
		return defaultValue;
	}

	/**
	 * 创建索引
	 * @param client
	 * @param indexDTO
	 * @return
	 * @throws IOException
	 */
	public static boolean createIndex(RestHighLevelClient client, IndexDTO indexDTO) throws IOException {

		Assert.notNull(indexDTO, "index信息不能为null");
		Assert.hasText(indexDTO.getIndexName(), "indexName不能为空");

		CreateIndexRequest request = new CreateIndexRequest(indexDTO.getIndexName());
		request.settings(Settings.builder().put(SHARDS_SETTING, getValueOrDefault(indexDTO.getNumberOfShards(), 3))
				.put(REPLICAS_SETTING, getValueOrDefault(indexDTO.getNumberOfReplicas(), 2))
				.put(READ_ONLY_ALLOW_DELETE, "false"));

		CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);

		boolean acknowledged = createIndexResponse.isAcknowledged();
		return acknowledged;
	}

	/**
	 * 重建索引
	 * @param client
	 * @param reindexDTO
	 * @return
	 */
	public static boolean reindex(RestHighLevelClient client, ReindexDTO reindexDTO) throws IOException {
		Assert.notNull(reindexDTO);
		Assert.hasText(reindexDTO.getSourceIndexName());
		Assert.hasText(reindexDTO.getTargetIndexName());
		ReindexRequest request = new ReindexRequest();

		request.setSourceIndices(reindexDTO.getSourceIndexName());
		request.setDestIndex(reindexDTO.getTargetIndexName());
		request.setConflicts("proceed");
		// 不限时
		request.setRetryBackoffInitialTime(TimeValue.timeValueMinutes(Integer.MAX_VALUE));
		request.setMaxRetries(Integer.MAX_VALUE);
		request.setTimeout(TimeValue.timeValueMinutes(Integer.MAX_VALUE));
		// 如果有查询语句的话根据查询语句进行reindex
		if (Objects.isNull(reindexDTO.getSearchQuery())) {
			request.setSourceQuery(matchAllQuery());
		}
		else {
			request.setSourceQuery(reindexDTO.getSearchQuery());
		}
		// 每批次数量
		if (Objects.isNull(reindexDTO.getBatchSize())) {
			request.setSourceBatchSize(200);
		}
		else {
			request.setSourceBatchSize(Math.max(10, reindexDTO.getBatchSize()));
		}
		client.reindexAsync(request, RequestOptions.DEFAULT, new ActionListener<BulkByScrollResponse>() {
			@Override
			public void onResponse(BulkByScrollResponse bulkByScrollResponse) {
				long total = bulkByScrollResponse.getTotal();
				log.info("重建索引完成，source:{},target:{},total: {}", reindexDTO.getSourceIndexName(),
						reindexDTO.getTargetIndexName(), total);
			}

			@Override
			public void onFailure(Exception e) {
				log.error("重建索引失败：", e);
			}
		});

		return true;

	}

	/**
	 * 删除索引
	 * @param client
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteIndex(RestHighLevelClient client, String indexName) throws IOException {

		Assert.hasText(indexName, "索引名不能为空");

		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
		AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
		boolean acknowledged = delete.isAcknowledged();
		return acknowledged;
	}

	/**
	 * 创建mapping
	 * @param client
	 * @param indexName
	 * @param fieldDTOS
	 * @return
	 */
	public static boolean putMapping(RestHighLevelClient client, String indexName, Collection<FieldDTO> fieldDTOS)
			throws IOException {

		return putMapping(client, indexName, fieldDTOS, false);
	}

	/**
	 * @param client
	 * @param indexName
	 * @param fieldDTOS
	 * @param needDefaultFields 是否需要创建默认字段（_update_time等）
	 * @return
	 * @throws IOException
	 */
	public static boolean putMapping(RestHighLevelClient client, String indexName, Collection<FieldDTO> fieldDTOS,
			boolean needDefaultFields) throws IOException {

		Assert.hasText(indexName, "索引名不能为空");
		Assert.notNull(fieldDTOS, "properties列表不能为空");

		Assert.isTrue(!CollectionUtils.isEmpty(fieldDTOS), "请添加字段信息");

		PutMappingRequest putMappingRequest = getPutMappingRequest(indexName, fieldDTOS, needDefaultFields);

		AcknowledgedResponse acknowledgedResponse = client.indices().putMapping(putMappingRequest,
				RequestOptions.DEFAULT);
		boolean acknowledged = acknowledgedResponse.isAcknowledged();
		return acknowledged;
	}

	/**
	 * 构建mapping 请求信息
	 * @param indexName
	 * @param fieldDTOS
	 * @param needDefaultFields
	 * @return
	 * @throws IOException
	 */
	private static PutMappingRequest getPutMappingRequest(String indexName, Collection<FieldDTO> fieldDTOS,
			boolean needDefaultFields) throws IOException {
		XContentBuilder builder = XContentFactory.jsonBuilder();

		// 构建mapping
		builder.startObject();
		{
			builder.startObject("properties");
			{
				// 创建mapping
				for (FieldDTO fieldDTO : fieldDTOS) {
					buildField(builder, fieldDTO);
				}
				// 需要增加默认字段
				if (needDefaultFields) {
					String createTime = CREATE_TIME;
					builder.startObject(createTime);
					builder.field("type", FieldType.Date.name().toLowerCase());
					builder.field("format", TIME_FORMATTER);
					builder.endObject();

					String updateTime = UPDATE_TIME;
					builder.startObject(updateTime);
					builder.field("type", FieldType.Date.name().toLowerCase());
					builder.field("format", TIME_FORMATTER);
					builder.endObject();
				}
			}
			builder.endObject();
		}
		builder.endObject();
		PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);
		putMappingRequest.type(DEFAULT_INDEX_TYPE);

		putMappingRequest.source(builder);
		return putMappingRequest;
	}

	private static void buildField(XContentBuilder builder, FieldDTO fieldDTO) throws IOException {

		builder.startObject(fieldDTO.getName());

		if (ObjectUtils.isNotEmpty(fieldDTO.getChildren())) {
			builder.field("type", "nested");
		}
		else {
			builder.field("type", fieldDTO.getFieldType().name().toLowerCase(Locale.ROOT));
		}
		// text类型特殊处理
		if (FieldType.Text.equals(fieldDTO.getFieldType())) {
			if (Objects.nonNull(fieldDTO.getAnalyzer())) {
				builder.field("analyzer", fieldDTO.getAnalyzer());
			}

			// 如果需要精确检索的话，增加key字段
			if (fieldDTO.isNeedPrecise()) {
				builder.startObject("fields");
				{
					builder.startObject("keyword");
					{
						builder.field("type", "keyword");
						builder.field("ignore_above", "5000");
					}

					builder.endObject();
				}
				builder.endObject();
			}
		}
		else if (FieldType.Date.equals(fieldDTO.getFieldType())) {
			// yyyy||yyyyMM||yyyy.MM||yyyy/MM||yyyy-MM||yyyyMMdd||yyyy.MM
			// .dd||yyyy/MM/dd||yyyy-MM-dd||yyyy-MM-dd HH:mm:ss||yyyy.MM.dd
			// HH:mm:ss||yyyy/MM/dd
			// HH:mm:ss||yyyy-MM-dd HH:mm:ss.SSS||yyyy.MM.dd HH:mm:ss.SSS||yyyy/MM/dd
			// HH:mm:ss.SSS
			builder.field("format", TIME_FORMATTER);
		}
		else if (FieldType.Keyword.equals(fieldDTO.getFieldType())) {
			builder.field("ignore_above", "5000");
		}

		// 递归处理nested类型字段
		if (ObjectUtils.isNotEmpty(fieldDTO.getChildren())) {
			builder.startObject("properties");
			{
				List<FieldDTO> children = fieldDTO.getChildren();
				for (FieldDTO child : children) {
					buildField(builder, child);
				}
			}

			builder.endObject();
		}

		builder.endObject();
	}

	/**
	 * 创建索引并设置mapping
	 * @param client
	 * @param indexDTO
	 * @param fieldDTOS
	 * @return
	 */
	public static boolean createIndexAndPutMapping(RestHighLevelClient client, IndexDTO indexDTO,
			Collection<FieldDTO> fieldDTOS) {

		Assert.notNull(indexDTO);
		Assert.notNull(fieldDTOS);
		String indexName = indexDTO.getIndexName();
		boolean indexCreated;
		try {
			indexCreated = EsIndexUtils.createIndex(client, indexDTO);
		}
		catch (Exception e) {
			log.error("创建索引[{}]失败", indexName, e);
			return false;
		}
		if (indexCreated) {
			try {
				return EsIndexUtils.putMapping(client, indexName, fieldDTOS);
			}
			catch (Exception e) {
				log.error("创建[{}]mapping失败", indexName, e);
			}
			try {
				EsIndexUtils.deleteIndex(client, indexName);
			}
			catch (Exception e) {
				log.error("再创建索引回滚时删除索引[{}]失败", indexName, e);
			}
			return false;
		}
		return false;
	}

	/**
	 * 给复合字段添加字段配置
	 * @param client
	 * @param nestedDTO
	 * @return
	 * @throws IOException
	 */
	public static boolean putNestedMapping(RestHighLevelClient client, NestedDTO nestedDTO) throws IOException {

		Map<String, Object> jsonMap = new HashMap<>();

		Map<String, Object> properties = new HashMap<>();

		Map<String, Object> nestedMap = new HashMap<>();
		nestedMap.put("type", "nested");

		Map<String, Object> nestedProperties = new HashMap<>();

		List<NestedDTO.NestedField> fields = nestedDTO.getFields();
		for (NestedDTO.NestedField field : fields) {
			Map<String, Object> fieldMap = new HashMap<>();
			if (NON_INSIDE_FIELD_TYPE.contains(field.getFieldType())) {
				throw new IllegalArgumentException("非法的复合字段类型：" + field.getFieldType());
			}
			fieldMap.put("type", field.getFieldType().name().toLowerCase());
			if (FieldType.Date.equals(field.getFieldType())) {
				// yyyy||yyyyMM||yyyy.MM||yyyy/MM||yyyy-MM||yyyyMMdd||yyyy.MM
				// .dd||yyyy/MM/dd||yyyy-MM-dd||yyyy-MM-dd HH:mm:ss||yyyy.MM.dd
				// HH:mm:ss||yyyy/MM/dd
				// HH:mm:ss||yyyy-MM-dd HH:mm:ss.SSS||yyyy.MM.dd HH:mm:ss.SSS||yyyy/MM/dd
				// HH:mm:ss.SSS
				fieldMap.put("format", TIME_FORMATTER);
			}
			else if (FieldType.Keyword.equals(field.getFieldType())) {
				fieldMap.put("ignore_above", "5000");
			}
			nestedProperties.put(field.getFieldName(), fieldMap);
		}

		nestedMap.put("properties", nestedProperties);

		properties.put(nestedDTO.getParentFieldName(), nestedMap);

		jsonMap.put("properties", properties);

		PutMappingRequest putMappingRequest = new PutMappingRequest(nestedDTO.getIndexName());
		putMappingRequest.type(DEFAULT_INDEX_TYPE);

		putMappingRequest.source(jsonMap);

		AcknowledgedResponse acknowledgedResponse = client.indices().putMapping(putMappingRequest,
				RequestOptions.DEFAULT);
		boolean acknowledged = acknowledgedResponse.isAcknowledged();
		return acknowledged;

	}

	/**
	 * 创建索引并设置mapping
	 * @param client
	 * @param indexDTO
	 * @param fieldDTOS
	 * @param needDefaultFields 是否创建默认的createTime和updateTime
	 * @return
	 */
	public static boolean createIndexAndPutMapping(RestHighLevelClient client, IndexDTO indexDTO,
			Collection<FieldDTO> fieldDTOS, boolean needDefaultFields) {

		Assert.notNull(indexDTO);
		Assert.notNull(fieldDTOS);
		String indexName = indexDTO.getIndexName();
		boolean indexCreated;
		try {
			indexCreated = EsIndexUtils.createIndex(client, indexDTO);
		}
		catch (Exception e) {
			log.error("创建索引[{}]失败", indexName, e);
			return false;
		}
		if (indexCreated) {
			try {
				return EsIndexUtils.putMapping(client, indexName, fieldDTOS, needDefaultFields);
			}
			catch (Exception e) {
				log.error("创建[{}]mapping失败", indexName, e);
			}
			try {
				EsIndexUtils.deleteIndex(client, indexName);
			}
			catch (Exception e) {
				log.error("再创建索引回滚时删除索引[{}]失败", indexName, e);
			}
			return false;
		}
		return false;
	}

	/**
	 * 创建mapping
	 * @param client
	 * @param indexName
	 * @param fieldDTOS
	 * @return
	 */
	public static boolean putColumnMapping(RestHighLevelClient client, String indexName, Collection<FieldDTO> fieldDTOS)
			throws IOException {

		Assert.hasText(indexName, "索引名不能为空");
		Assert.notNull(fieldDTOS, "properties列表不能为空");
		Assert.isTrue(!CollectionUtils.isEmpty(fieldDTOS), "请添加字段信息");
		PutMappingRequest putMappingRequest = getPutMappingRequest(indexName, fieldDTOS, false);
		AcknowledgedResponse acknowledgedResponse = client.indices().putMapping(putMappingRequest,
				RequestOptions.DEFAULT);
		boolean acknowledged = acknowledgedResponse.isAcknowledged();
		return acknowledged;
	}

	/**
	 * 为索引添加新的列信息
	 * @param client
	 * @param indexDTO
	 * @param fieldDTOS
	 * @return
	 */
	public static boolean addIndexColumns(RestHighLevelClient client, IndexDTO indexDTO,
			Collection<FieldDTO> fieldDTOS) {
		Assert.notNull(indexDTO);
		Assert.notNull(fieldDTOS);
		String indexName = indexDTO.getIndexName();
		boolean mapping = false;
		try {
			mapping = EsIndexUtils.putColumnMapping(client, indexName, fieldDTOS);
		}
		catch (IOException e) {
			log.error("创建[{}]mapping失败", indexName, e);
			return false;
		}
		if (mapping) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 修改配置
	 * @param client
	 * @param indexName
	 * @param settings
	 * @return
	 * @throws IOException
	 */
	public static boolean putSettings(RestHighLevelClient client, String indexName, Map<String, String> settings)
			throws IOException {

		UpdateSettingsRequest updateSettingsRequest = new UpdateSettingsRequest(indexName);

		updateSettingsRequest.settings(settings);

		AcknowledgedResponse acknowledgedResponse = client.indices().putSettings(updateSettingsRequest,
				RequestOptions.DEFAULT);
		return acknowledgedResponse.isAcknowledged();
	}

	/**
	 * 判断指定的索引名是否存在
	 * @param client
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	public static boolean isExistsIndex(RestHighLevelClient client, String indexName) throws IOException {
		Assert.hasText(indexName, "索引名不能为空");

		GetIndexRequest getIndexRequest = new GetIndexRequest();
		getIndexRequest.indices(indexName);

		return client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);

	}

	/**
	 * 给索引添加别名
	 * @param client
	 * @param indexName
	 * @param alias
	 * @return
	 */
	public static boolean addAlias(RestHighLevelClient client, String indexName, String alias) {
		Assert.hasText(indexName, "索引名不能为空");
		Assert.hasText(alias, "别名不能为空");
		IndicesAliasesRequest.AliasActions aliasAction = IndicesAliasesRequest.AliasActions.add().alias(alias)
				.index(indexName);
		IndicesAliasesRequest request = new IndicesAliasesRequest();
		request.addAliasAction(aliasAction);

		try {
			return client.indices().updateAliases(request, RequestOptions.DEFAULT).isAcknowledged();
		}
		catch (IOException e) {
			log.error("failed to update aliases with request: " + request, e);
		}

		return false;

	}

	/**
	 * 判断指定的别名是否存在
	 * @param client
	 * @param alias
	 * @return
	 * @throws IOException
	 */
	public static boolean isExistsAlias(RestHighLevelClient client, String alias) throws IOException {
		Assert.hasText(alias, "别名不能为空");
		GetAliasesRequest getAliasesRequest = new GetAliasesRequest(alias);
		return client.indices().existsAlias(getAliasesRequest, RequestOptions.DEFAULT);
	}

	/**
	 * 获取指定索引或别名的字段配置信息
	 * @param client
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	public static LinkedHashMap<String, LinkedHashMap<String, Object>> getFieldMapping(RestHighLevelClient client,
			String indexName) throws IOException {
		Assert.hasText(indexName, "索引名不能为空");

		GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
		// 此处的indexName包含索引名和别名
		getMappingsRequest.indices(indexName);
		GetMappingsResponse response = client.indices().getMapping(getMappingsRequest, RequestOptions.DEFAULT);

		// key为索引名，value为对应的mapping信息
		ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = response.getMappings();
		if (Objects.isNull(mappings) || mappings.size() < 1) {
			return Maps.newLinkedHashMap();
		}

		Iterator<ObjectObjectCursor<String, ImmutableOpenMap<String, MappingMetaData>>> iterator = mappings.iterator();

		if (iterator.hasNext()) {
			// 只采取第一个的信息
			ObjectObjectCursor<String, ImmutableOpenMap<String, MappingMetaData>> next = iterator.next();

			// type层
			ImmutableOpenMap<String, MappingMetaData> typeMap = next.value;

			MappingMetaData info = typeMap.get(DEFAULT_INDEX_TYPE);

			//
			Map<String, Object> infoMap = info.getSourceAsMap();

			Object properties = infoMap.get("properties");
			if (properties instanceof Map) {
				return (LinkedHashMap<String, LinkedHashMap<String, Object>>) properties;
			}
		}

		return Maps.newLinkedHashMap();
	}

	@Data
	public static class FieldDTO {

		/**
		 * 字段名
		 */
		private String name;

		/**
		 * 字段类型
		 */
		private FieldType fieldType;

		/**
		 * 分词器(当字段类型为Text时生效)
		 */
		private String analyzer;

		/**
		 * 是否需要精确检索
		 */
		private boolean needPrecise;

		/**
		 * 子字段集
		 */
		private List<FieldDTO> children;

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class IndexDTO {

		/**
		 * 索引名
		 */
		private String indexName;

		/**
		 * 分片数量
		 */
		private Integer numberOfShards;

		/**
		 * 副本数量
		 */
		private Integer numberOfReplicas;

		public IndexDTO(String indexName) {
			this.indexName = indexName;
		}

	}

	@Data
	public static class NestedDTO {

		/**
		 * 索引名
		 */
		private String indexName;

		/**
		 * 父字段名
		 */
		private String parentFieldName;

		/**
		 * 内部字段列表
		 */
		private List<NestedField> fields;

		/**
		 * 复合类型的内部字段配置
		 */
		@Data
		@NoArgsConstructor
		@AllArgsConstructor
		public class NestedField {

			private String fieldName;

			/**
			 * 数据类型
			 */
			private FieldType fieldType = FieldType.Keyword;

		}

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReindexDTO {

		/**
		 * 源索引名
		 */
		private String sourceIndexName;

		/**
		 * 目标索引名
		 */
		private String targetIndexName;

		/**
		 * 每次发包的数量，默认200
		 */
		private Integer batchSize;

		/**
		 * reindex执行条件，默认查询所有
		 */
		private QueryBuilder searchQuery;

		public ReindexDTO(String sourceIndexName, String targetIndexName) {
			this.sourceIndexName = sourceIndexName;
			this.targetIndexName = targetIndexName;
		}

	}

}