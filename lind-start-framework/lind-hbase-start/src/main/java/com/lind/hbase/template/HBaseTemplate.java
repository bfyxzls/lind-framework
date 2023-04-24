package com.lind.hbase.template;

import com.lind.hbase.api.HBaseOperations;
import com.lind.hbase.api.MutatorCallback;
import com.lind.hbase.api.RowMapper;
import com.lind.hbase.api.TableCallback;
import com.lind.hbase.exception.HbaseSystemException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.List;

/**
 * Central class for accessing the HBase API. Simplifies the use of HBase and helps to avoid common errors.
 * It executes core HBase workflow, leaving application code to invoke actions and extract results.
 *
 * @author Costin Leau
 * @author Shaun Elliott
 */

/**
 * JThink@JThink
 *
 * @author JThink
 * @version 0.0.1 desc： copy from spring data hadoop hbase, modified by JThink, use the
 * 1.0.0 api date： 2016-11-15 15:42:46
 */
public class HBaseTemplate implements HBaseOperations {

	private static final Logger LOGGER = LoggerFactory.getLogger(HBaseTemplate.class);

	private Connection connection;

	public HBaseTemplate(Connection connection) {
		this.connection = connection;
		Assert.notNull(connection, " a valid connection is required");
	}

	@Override
	public <T> T execute(TableName tableName, TableCallback<T> action) {
		Assert.notNull(action, "Callback object must not be null");
		Assert.notNull(tableName, "No table specified");

		StopWatch sw = new StopWatch();
		sw.start();
		Table table = null;
		try {
			table = this.getConnection().getTable(tableName);
			return action.doInTable(table);
		}
		catch (Throwable throwable) {
			throw new HbaseSystemException(throwable);
		}
		finally {
			if (null != table) {
				try {
					table.close();
					sw.stop();
				}
				catch (IOException e) {
					LOGGER.error("hbase资源释放失败");
				}
			}
		}
	}

	@Override
	public boolean exist(TableName tableName, String familyName, String rowKey) throws IOException {
		Assert.notNull(familyName, "列族不能为空");
		Assert.notNull(rowKey, "rowKey不能为空");
		Table table = getConnection().getTable(tableName);
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addFamily(Bytes.toBytes(familyName));
		return table.exists(get);
	}

	@Override
	public <T> T get(TableName tableName, String rowKey, final RowMapper<T> mapper) {
		return this.get(tableName, rowKey, null, null, mapper);
	}

	@Override
	public <T> T get(TableName tableName, String rowKey, String familyName, final RowMapper<T> mapper) {
		return this.get(tableName, rowKey, familyName, null, mapper);
	}

	@Override
	public <T> T get(TableName tableName, String rowKey, String familyName, RowMapper<T> mapper, String... columnName) {
		return this.execute(tableName, new TableCallback<T>() {
			@Override
			public T doInTable(Table table) throws Throwable {
				Get get = new Get(Bytes.toBytes(rowKey));
				if (StringUtils.isNotBlank(familyName)) {
					byte[] family = Bytes.toBytes(familyName);
					if (ObjectUtils.isNotEmpty(columnName)) {
						for (String s : columnName) {
							get.addColumn(family, Bytes.toBytes(s));
						}
					}
					else {
						get.addFamily(family);
					}
				}
				Result result = table.get(get);
				return mapper.mapRow(result, 0);
			}
		});
	}

	@Override
	public <T> T get(TableName tableName, final String rowKey, final String familyName, final String qualifier,
			final RowMapper<T> mapper) {
		return this.execute(tableName, new TableCallback<T>() {
			@Override
			public T doInTable(Table table) throws Throwable {
				Get get = new Get(Bytes.toBytes(rowKey));
				if (StringUtils.isNotBlank(familyName)) {
					byte[] family = Bytes.toBytes(familyName);
					if (StringUtils.isNotBlank(qualifier)) {
						get.addColumn(family, Bytes.toBytes(qualifier));
					}
					else {
						get.addFamily(family);
					}
				}
				Result result = table.get(get);
				return mapper.mapRow(result, 0);
			}
		});
	}

	@Override
	public void execute(TableName tableName, MutatorCallback action) {
		this.execute(tableName, action, 3 * 1024 * 1024l);
	}

	@Override
	public void execute(TableName tableName, MutatorCallback action, Long writeBufferSize) {
		Assert.notNull(action, "Callback object must not be null");
		Assert.notNull(tableName, "No table specified");
		Assert.notNull(writeBufferSize, "WriteBufferSize must not be null");

		StopWatch sw = new StopWatch();
		sw.start();
		BufferedMutator mutator = null;
		try {
			BufferedMutatorParams mutatorParams = new BufferedMutatorParams(tableName);
			mutator = this.getConnection().getBufferedMutator(mutatorParams.writeBufferSize(writeBufferSize));
			action.doInMutator(mutator);
		}
		catch (Throwable throwable) {
			sw.stop();
			throw new HbaseSystemException(throwable);
		}
		finally {
			if (null != mutator) {
				try {
					mutator.flush();
					mutator.close();
					sw.stop();
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("cost time:{}", sw.prettyPrint());
					}
				}
				catch (IOException e) {
					LOGGER.error("hbase mutator资源释放失败");
				}
			}
		}
	}

	@Override
	public void saveOrUpdate(TableName tableName, final Mutation mutation) {
		this.execute(tableName, new MutatorCallback() {
			@Override
			public void doInMutator(BufferedMutator mutator) throws Throwable {
				mutator.mutate(mutation);
			}
		});
	}

	@Override
	public void saveOrUpdates(TableName tableName, List<Mutation> mutations) {
		this.saveOrUpdates(tableName, mutations, null);
	}

	@Override
	public void saveOrUpdates(TableName tableName, final List<Mutation> mutations, Long writeBufferSize) {
		if (writeBufferSize != null) {
			this.execute(tableName, new MutatorCallback() {
				@Override
				public void doInMutator(BufferedMutator mutator) throws Throwable {
					mutator.mutate(mutations);
				}
			}, writeBufferSize);
		}
		else {
			this.execute(tableName, new MutatorCallback() {
				@Override
				public void doInMutator(BufferedMutator mutator) throws Throwable {
					mutator.mutate(mutations);
				}
			});
		}
	}

	public Connection getConnection() {

		return this.connection;
	}

}
