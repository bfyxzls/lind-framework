package com.lind.hbase.util;

import cn.hutool.json.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Properties;

/**
 * @author:-J-
 * @date:2020/8/14
 * @description
 */
public class HbaseUtil {

	private static Configuration hconf_373839 = HBaseConfiguration.create();

	private static Properties hbaseProperties = new Properties();

	private static Connection conn = null;

	private static String FAMILY_NAME = "info";

	static {
		InputStream in = HbaseUtil.class.getClassLoader().getResourceAsStream("hbase.properties");
		try {
			hbaseProperties.load(in);
			Iterator keys = hbaseProperties.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = (String) hbaseProperties.get(key);
				hconf_373839.set(key, value);
			}
			conn = ConnectionFactory.createConnection(hconf_373839);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 单条查找
	 * @param rowKey
	 * @param tableName
	 * @return
	 * @throws IOException
	 */
	public static JSONObject queryByRowkey(String rowKey, String tableName) throws IOException {
		Table table = conn.getTable(TableName.valueOf(tableName));
		Get get = new Get(Bytes.toBytes(rowKey));
		Result result = table.get(get);
		NavigableMap<byte[], byte[]> map = result.getFamilyMap(Bytes.toBytes(FAMILY_NAME));
		Iterator<byte[]> keys = map.keySet().iterator();
		JSONObject obj = new JSONObject();
		while (keys.hasNext()) {
			byte[] key = keys.next();
			byte[] value = map.get(key);
			obj.put(Bytes.toString(key), Bytes.toString(value));
		}
		return obj;
	}

	/**
	 * 单条查找
	 * @param rowKey
	 * @param tableName
	 * @return
	 * @throws IOException
	 */
	public static JSONObject queryByRowkeySerialize(String rowKey, String tableName) throws IOException {
		Table table = conn.getTable(TableName.valueOf(tableName));
		Get get = new Get(Bytes.toBytes(rowKey));
		Result result = table.get(get);
		NavigableMap<byte[], byte[]> map = result.getFamilyMap(Bytes.toBytes(FAMILY_NAME));
		Iterator<byte[]> keys = map.keySet().iterator();
		JSONObject obj = new JSONObject();
		while (keys.hasNext()) {
			byte[] key = keys.next();
			byte[] value = map.get(key);
			obj.put(Bytes.toString(key), SerializationUtils.deserialize(value));
		}
		return obj;
	}

	/**
	 * 是否存在
	 * @param rowKey
	 * @param tableName
	 * @return
	 * @throws IOException
	 */
	public static boolean checkByRowKey(String rowKey, String tableName) throws IOException {
		Table table = conn.getTable(TableName.valueOf(tableName));
		Get get = new Get(Bytes.toBytes(rowKey));
		return table.exists(get);
	}

	public static ResultScanner queryBySingleColumnFilter(String columnName, String value, String tableName)
			throws IOException {
		Table table = conn.getTable(TableName.valueOf(tableName));
		Scan scan = new Scan();
		SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes(FAMILY_NAME),
				Bytes.toBytes(columnName), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(value));
		FilterList filterList = new FilterList();
		filterList.addFilter(filter);
		scan.setFilter(filterList);
		ResultScanner scanner = table.getScanner(scan);
		return scanner;
	}

}
