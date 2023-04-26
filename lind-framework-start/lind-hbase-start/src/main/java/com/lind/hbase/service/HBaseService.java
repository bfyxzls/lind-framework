package com.lind.hbase.service;

import com.lind.hbase.entity.DataRecord;

import java.io.IOException;
import java.util.List;

/***
 * HBase业务操作规范.
 */
public interface HBaseService {

	/**
	 * 创建表
	 * @param tableName
	 * @return
	 * @throws IOException
	 */
	boolean createTable(String tableName) throws IOException;

	/**
	 * 表格是否已经存在
	 * @param tableName
	 * @return
	 * @throws IOException
	 */
	boolean tableExist(String tableName) throws IOException;

	/**
	 * 保存数据
	 * @param tableName
	 * @param dataRecord
	 * @return 保存数据的rowKey
	 */
	String save(String tableName, DataRecord dataRecord);

	/**
	 * 更新数据
	 * @param tableName
	 * @param dataRecord
	 * @return
	 */
	String update(String tableName, DataRecord dataRecord);

	/**
	 * 获取数据的指定列
	 * @param tableName
	 * @param rowKey
	 * @param fieldName
	 * @return
	 */
	Object getValue(String tableName, String rowKey, String fieldName);

	/**
	 * 根据rowKey获取数据
	 * @param tableName
	 * @param rowKey
	 * @param fieldNames
	 * @return
	 */
	DataRecord getByKey(String tableName, String rowKey, String... fieldNames);

	/**
	 * 根据rowKey获取全字段
	 * @param tableName
	 * @param rowKey
	 * @return
	 */
	DataRecord getByKey(String tableName, String rowKey);

	/**
	 * 根据rowkey获取全字段
	 * @param namespace
	 * @param tableName
	 * @param rowKey
	 * @return
	 */
	DataRecord getByKey(String namespace, String tableName, String rowKey);

	/**
	 * 删除表
	 * @param tableName
	 */
	void deleteTable(String tableName) throws IOException;

	/**
	 * @param tableName
	 * @param rowkeyList
	 * @param fieldNames
	 * @return
	 * @author belizer 20210121 通过多个rowkey,指定字段获取多行数据
	 */
	List<DataRecord> getByKeyList(String tableName, List<String> rowkeyList, String... fieldNames);

	/**
	 * @param tableName
	 * @param rowkeyList
	 * @return
	 * @author belizer 20210121 通过多个rowkey,获取多行数据（全字段）
	 */
	List<DataRecord> getByKeyList(String tableName, List<String> rowkeyList);

	/**
	 * @param tableName
	 * @param dataRecordList
	 * @author belizer 20210201 一次保存/更新多行数据,一批数据大小为3MB
	 */
	void saveOrUpdates(String tableName, List<DataRecord> dataRecordList);

	/**
	 * @param tableName
	 * @param dataRecordList
	 * @author belizer 20210201 一次保存/更新多行数据，一批数据大小自定义
	 */
	void saveOrUpdates(String tableName, List<DataRecord> dataRecordList, Long writeBufferSize);

	/**
	 * 记录是否存在.
	 * @param tableName
	 * @param rowkey
	 * @return
	 */
	boolean existDataRecord(String tableName, String rowkey);

}
