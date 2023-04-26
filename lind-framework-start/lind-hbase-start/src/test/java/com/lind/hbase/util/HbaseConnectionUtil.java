package com.lind.hbase.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

/**
 * @author:-J-
 * @date:2020/11/7
 * @description
 */
public class HbaseConnectionUtil {

	public static Connection getConnection373839() {
		Configuration configuration = HBaseConfiguration.create();
		configuration.set("hbase.zookeeper.property.clientPort", "2181");
		configuration.set("hbase.zookeeper.quorum", "qingyun37,qingyun38,qingyun39");
		configuration.set("hbase.master", "qingyun37:60000");
		Connection conn = null;
		try {
			conn = ConnectionFactory.createConnection(configuration);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

}
