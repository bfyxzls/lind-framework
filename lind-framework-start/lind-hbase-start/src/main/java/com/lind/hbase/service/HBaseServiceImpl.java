package com.lind.hbase.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ReUtil;
import com.lind.hbase.api.RowMapper;
import com.lind.hbase.api.TableCallback;
import com.lind.hbase.entity.DataRecord;
import com.lind.hbase.template.HBaseTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HBaseService封装，列族空间为info.
 */
@Slf4j
@RequiredArgsConstructor
public class HBaseServiceImpl implements HBaseService {

	private static final String FAMILY_NAME = "info";

	private static final byte[] FAMILY_NAME_BYTE = Bytes.toBytes(FAMILY_NAME);

	private static final String regex = ".*?[\\/\\+].*?";

	private final HBaseTemplate hBaseTemplate;

	/**
	 * HBase的命名空间，需要提前定义
	 */
	private final String nameSpace;

	/**
	 * 将带有 / +的rowKey合法化
	 * @param sourceRowKey 原rowKey
	 * @return
	 */
	public static String legalize(String sourceRowKey) {

		if (StringUtils.isEmpty(sourceRowKey)) {
			throw new IllegalArgumentException("rowKey must not be empty.");
		}
		if (ReUtil.isMatch(regex, sourceRowKey)) {
			return Base64.encodeUrlSafe(sourceRowKey);
		}
		return sourceRowKey;
	}

	@Override
	public boolean createTable(String tableName) throws IOException {
		HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(nameSpace, tableName));
		hTableDescriptor.addFamily(new HColumnDescriptor(FAMILY_NAME));
		hBaseTemplate.getConnection().getAdmin().createTable(hTableDescriptor);
		return true;
	}

	@Override
	public boolean tableExist(String tableName) throws IOException {
		Admin admin = hBaseTemplate.getConnection().getAdmin();
		return admin.tableExists(retrieveTableName(tableName));
	}

	@Override
	public String save(String tableName, DataRecord dataRecord) {
		String execute = hBaseTemplate.execute(retrieveTableName(tableName), new TableCallback<String>() {
			@Override
			public String doInTable(Table table) throws Throwable {

				String id = dataRecord.getId();

				String idStr = StringUtils.isBlank(id) ? UUID.fastUUID().toString() : id;

				// 合法化
				String legalize = legalize(idStr);

				Put put = new Put(Bytes.toBytes(legalize));

				dataRecord.forEach((key, value) -> {
					// 将数据填充到dataRecord中
					put.addColumn(FAMILY_NAME_BYTE, Bytes.toBytes(key), SerializationUtils.serialize(value));
				});
				table.put(put);

				return new String(put.getRow());
			}
		});
		return execute;
	}

	@Override
	public String update(String tableName, DataRecord dataRecord) {
		Assert.notNull(dataRecord, "更新数据不能为空");
		Assert.hasText(dataRecord.getId(), "更新id不能为空");
		if (getByKey(tableName, dataRecord.getId()) == null) {
			throw new IllegalArgumentException("要更新的数据不存在:" + dataRecord.getId());
		}
		return save(tableName, dataRecord);
	}

	@Override
	public Object getValue(String tableName, String rowKey, String fieldName) {
		Object o = hBaseTemplate.get(retrieveTableName(tableName), rowKey, new RowMapper<Object>() {
			@Override
			public Object mapRow(Result result, int rowNum) throws Exception {
				if (result.isEmpty()) {
					return null;
				}
				byte[] value = result.getValue(FAMILY_NAME_BYTE, Bytes.toBytes(fieldName));
				Object deserialize = SerializationUtils.deserialize(value);
				return deserialize;
			}
		});
		return o;
	}

	@Override
	public DataRecord getByKey(String tableName, String rowKey, String... fieldNames) {
		// rowKey合法化
		String legalizeRowKey = legalize(rowKey);
		DataRecord dataRecord = hBaseTemplate.get(retrieveTableName(tableName), legalizeRowKey, FAMILY_NAME,
				new RowMapper<DataRecord>() {
					@Override
					public DataRecord mapRow(Result result, int rowNum) throws Exception {
						if (result.isEmpty()) {
							return null;
						}
						DataRecord dataRecord = new DataRecord();
						for (String fieldName : fieldNames) {
							// 获取值
							byte[] value = result.getValue(FAMILY_NAME_BYTE, Bytes.toBytes(fieldName));
							if (ObjectUtils.isEmpty(value)) {
								dataRecord.append(fieldName, null);
							}
							else {
								dataRecord.append(fieldName, SerializationUtils.deserialize(value));
							}
						}
						dataRecord.setId(rowKey);
						return dataRecord;
					}
				});
		return dataRecord;
	}

	@Override
	public DataRecord getByKey(String tableName, String rowKey) {
		return hBaseTemplate.get(retrieveTableName(tableName), rowKey, FAMILY_NAME, new RowMapper<DataRecord>() {
			@Override
			public DataRecord mapRow(Result result, int rowNum) throws Exception {
				if (result.isEmpty()) {
					return null;
				}
				DataRecord dataRecord = new DataRecord();

				CellScanner cellScanner = result.cellScanner();
				while (cellScanner.advance()) {
					Cell current = cellScanner.current();

					byte[] qualifierArray = CellUtil.cloneQualifier(current);
					byte[] valueArray = CellUtil.cloneValue(current);
					if (ObjectUtils.isNotEmpty(valueArray)) {
						Object obj = SerializationUtils.deserialize(valueArray);
						dataRecord.append(Bytes.toString(qualifierArray), obj);
					}
				}
				dataRecord.setId(rowKey);
				return dataRecord;
			}
		});
	}

	@Override
	public void deleteTable(String tableName) throws IOException {
		Assert.hasText(tableName, "表名不能为空");
		Admin admin = hBaseTemplate.getConnection().getAdmin();
		TableName tableName1 = retrieveTableName(tableName);
		admin.disableTable(tableName1);
		admin.deleteTable(tableName1);
	}

	@Override
	public List<DataRecord> getByKeyList(String tableName, List<String> rowkeyList, String... fieldNames) {
		Assert.hasText(tableName, "表名不能为空");
		Assert.notNull(rowkeyList, "rowkey集合不能为空");

		List<DataRecord> list = hBaseTemplate.execute(retrieveTableName(tableName),
				new TableCallback<List<DataRecord>>() {
					@Override
					public List<DataRecord> doInTable(Table table) throws Throwable {
						List<Get> getList = rowkeyList.stream().filter(k -> ObjectUtils.isNotEmpty(k))
								.map(rowKey -> (new Get(Bytes.toBytes(rowKey)))).collect(Collectors.toList());
						Result[] results = table.get(getList);// 重点在这，直接查getList

						if (ObjectUtils.isEmpty(results)) {
							throw new IllegalArgumentException("获取数据失败，请查验");
						}

						List<DataRecord> list = new ArrayList();

						for (int i = 0; i < results.length; i++) {
							Result result = results[i];
							DataRecord dataRecord = new DataRecord();
							// 没有这个id的时候就将结果置空
							if (result.isEmpty()) {
								dataRecord.setId(getList.get(i).getId());
							}
							else {
								for (String fieldName : fieldNames) {
									// 获取值
									byte[] value = result.getValue(FAMILY_NAME_BYTE, Bytes.toBytes(fieldName));
									if (ObjectUtils.isEmpty(value)) {
										dataRecord.append(fieldName, null);
									}
									else {
										dataRecord.append(fieldName, SerializationUtils.deserialize(value));
									}
								}
								dataRecord.setId(new String(result.getRow()));
							}
							list.add(dataRecord);
						}

						return list;
					}
				});

		return list;
	}

	@Override
	public List<DataRecord> getByKeyList(String tableName, List<String> rowkeyList) {
		Assert.hasText(tableName, "表名不能为空");
		Assert.notNull(rowkeyList, "rowkey集合不能为空");

		List<DataRecord> list = hBaseTemplate.execute(retrieveTableName(tableName),
				new TableCallback<List<DataRecord>>() {
					@Override
					public List<DataRecord> doInTable(Table table) throws Throwable {
						List<Get> getList = rowkeyList.stream().map(rowKey -> (new Get(Bytes.toBytes(rowKey))))
								.collect(Collectors.toList());
						Result[] results = table.get(getList);// 重点在这，直接查getList

						List<DataRecord> list = new ArrayList();

						for (Result result : results) {
							DataRecord dataRecord = new DataRecord();

							CellScanner cellScanner = result.cellScanner();
							while (true) {
								if (!cellScanner.advance())
									break;

								Cell current = cellScanner.current();

								byte[] qualifierArray = CellUtil.cloneQualifier(current);
								byte[] valueArray = CellUtil.cloneValue(current);
								if (ObjectUtils.isNotEmpty(valueArray)) {
									dataRecord.append(Bytes.toString(qualifierArray),
											SerializationUtils.deserialize(valueArray));
								}
							}

							dataRecord.setId(new String(result.getRow()));
							list.add(dataRecord);
						}
						;

						return list;
					}
				});

		return list;
	}

	@Override
	public void saveOrUpdates(String tableName, List<DataRecord> dataRecordList) {
		this.saveOrUpdates(tableName, dataRecordList, null);
	}

	@Override
	public void saveOrUpdates(String tableName, List<DataRecord> dataRecordList, Long writeBufferSize) {
		List<Mutation> mutationList = new ArrayList<>();
		dataRecordList.forEach(dataRecord -> {
			Put put = new Put(Bytes.toBytes(dataRecord.getId()));

			dataRecord.forEach((key, value) -> {
				// 将数据填充到dataRecord中
				put.addColumn(FAMILY_NAME_BYTE, Bytes.toBytes(key), SerializationUtils.serialize(value));
			});

			mutationList.add(put);
		});

		hBaseTemplate.saveOrUpdates(retrieveTableName(tableName), mutationList, writeBufferSize);
	}

	@SneakyThrows
	@Override
	public boolean existDataRecord(String tableName, String rowkey) {
		return hBaseTemplate.exist(retrieveTableName(tableName), FAMILY_NAME, rowkey);
	}

	@Override
	public DataRecord getByKey(String namespace, String tableName, String rowKey) {

		Assert.hasText(namespace, "命名空间不能为空");
		Assert.hasText(tableName, "表名不能为空");
		Assert.hasText(rowKey, "rowKey不能为空");

		return hBaseTemplate.get(TableName.valueOf(namespace + ":" + tableName), rowKey, FAMILY_NAME,
				new RowMapper<DataRecord>() {
					@Override
					public DataRecord mapRow(Result result, int rowNum) throws Exception {
						if (result.isEmpty()) {
							return null;
						}
						DataRecord dataRecord = new DataRecord();

						CellScanner cellScanner = result.cellScanner();
						while (cellScanner.advance()) {
							Cell current = cellScanner.current();

							byte[] qualifierArray = CellUtil.cloneQualifier(current);
							byte[] valueArray = CellUtil.cloneValue(current);
							if (ObjectUtils.isNotEmpty(valueArray)) {
								String key = null;
								try {
									key = Bytes.toString(qualifierArray);
									dataRecord.append(key, SerializationUtils.deserialize(valueArray));
								}
								catch (Exception e) {
									log.error("反序列化【{}】的字段【{}】时出现异常", rowKey, key, e);
									throw e;
								}

							}
						}
						dataRecord.setId(rowKey);
						return dataRecord;
					}
				});
	}

	private TableName retrieveTableName(String tableName) {
		return TableName.valueOf(nameSpace + ":" + tableName);
	}

}
