package com.lind.fileupload.util;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库工具.
 *
 * @author lind
 * @date 2024/2/26 9:06
 * @since 1.0.0
 */
public class DbUtil {

	public static DataSource createDataSource() throws SQLException {
		// 配置真实数据源
		Map<String, DataSource> dataSourceMap = new HashMap<>();
		// 配置第一个 MySQL 数据源
		HikariDataSource ds0 = new HikariDataSource();
		ds0.setDriverClassName("com.mysql.jdbc.Driver");
		ds0.setJdbcUrl("jdbc:mysql://192.168.4.26:3306/sharding0");
		ds0.setUsername("root");
		ds0.setPassword("123456");
		dataSourceMap.put("ds0", ds0);

		// 配置分片规则
		TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration("t_order", "ds0.t_order_${0..1}");
		ShardingStrategyConfiguration shardingStrategyConfiguration = new StandardShardingStrategyConfiguration(
				"order_id", new StandardShardingTableAlgorithm());
		tableRuleConfig.setTableShardingStrategyConfig(shardingStrategyConfiguration);
		tableRuleConfig.setKeyGeneratorConfig(new KeyGeneratorConfiguration("UUID", "order_id"));
		ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
		shardingRuleConfig.getTableRuleConfigs().add(tableRuleConfig);

		// 创建 ShardingSphere 数据源
		return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());

	}

}
