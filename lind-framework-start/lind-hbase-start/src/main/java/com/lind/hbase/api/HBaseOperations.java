package com.lind.hbase.api;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Mutation;

import java.io.IOException;
import java.util.List;

/**
 * Interface that specifies a basic set of Hbase operations, implemented by {@link HbaseTemplate}. Not often used,
 * but a useful option to enhance testability, as it can easily be mocked or stubbed.
 *
 * @author Costin Leau
 * @author Shaun Elliott
 */

/**
 * JThink@JThink
 *
 * @author JThink
 * @version 0.0.1 desc: copy from spring data hadoop hbase, modified by JThink, remove the
 * unuse interface date: 2016-11-15 14:49:52
 */
public interface HBaseOperations {

	/**
	 * Executes the given action against the specified table handling resource management.
	 * <p>
	 * Application exceptions thrown by the action object get propagated to the caller
	 * (can only be unchecked). Allows for returning a result object (typically a domain
	 * object or collection of domain objects).
	 * @param tableName the target table
	 * @param <T> action type
	 * @return the result object of the callback action, or null
	 */
	<T> T execute(TableName tableName, TableCallback<T> mapper);

	/**
	 * 是否存在某条数据
	 * @param tableName
	 * @param familyName 列族
	 * @param rowKey
	 * @return
	 */
	boolean exist(TableName tableName, String familyName, String rowKey) throws IOException;

	/**
	 * Gets an individual row from the given table. The content is mapped by the given
	 * action.
	 * @param tableName target table
	 * @param rowName row name
	 * @param mapper row mapper
	 * @param <T> mapper type
	 * @return object mapping the target row
	 */
	<T> T get(TableName tableName, String rowName, final RowMapper<T> mapper);

	/**
	 * Gets an individual row from the given table. The content is mapped by the given
	 * action.
	 * @param tableName target table
	 * @param rowName row name
	 * @param familyName column family
	 * @param mapper row mapper
	 * @param <T> mapper type
	 * @return object mapping the target row
	 */
	<T> T get(TableName tableName, String rowName, String familyName, final RowMapper<T> mapper);

	/**
	 * Gets an individual row from the given table. The content is mapped by the given
	 * action.
	 * @param tableName target table
	 * @param rowName row name
	 * @param familyName family
	 * @param qualifier column qualifier
	 * @param mapper row mapper
	 * @param <T> mapper type
	 * @return object mapping the target row
	 */
	<T> T get(TableName tableName, final String rowName, final String familyName, final String qualifier,
			final RowMapper<T> mapper);

	/**
	 * 获取指定列
	 * @param tableName
	 * @param rowKey
	 * @param familyName
	 * @param mapper
	 * @param columnName
	 * @param <T>
	 * @return
	 */
	<T> T get(TableName tableName, final String rowKey, final String familyName, final RowMapper<T> mapper,
			final String... columnName);

	/**
	 * 执行put update or delete
	 * @param tableName
	 * @param action
	 */
	void execute(TableName tableName, MutatorCallback action);

	/**
	 * @param tableName
	 * @param action
	 * @param writeBufferSize 批量操作时使用
	 * @author belizer 2021/02/01 执行put update or delete
	 */
	void execute(TableName tableName, MutatorCallback action, Long writeBufferSize);

	/**
	 * @param tableName
	 * @param mutation
	 */
	void saveOrUpdate(TableName tableName, Mutation mutation);

	/**
	 * @param tableName
	 * @param mutations
	 */
	void saveOrUpdates(TableName tableName, List<Mutation> mutations);

	/**
	 * @param tableName
	 * @param mutations
	 * @param writeBufferSize 批量操作时使用
	 * @author belizer 2021/02/01
	 */
	void saveOrUpdates(TableName tableName, List<Mutation> mutations, Long writeBufferSize);

}
