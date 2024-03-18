package com.lind.fileupload;

import com.lind.fileupload.entity.Order;
import com.lind.fileupload.repository.OrderRepository;
import com.lind.fileupload.util.DbUtil;
import com.lind.fileupload.util.StandardShardingTableAlgorithm;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {

	public static void main(String[] args) throws SQLException {
		System.out.println("sharding jdbc!");
		entityManagerAdd();
		// HihernateInsert();
		// selectByUser();
		// selectById();
	}

	// 自定义entityManager来实现基于entityManager操作时的分表操作
	static void entityManagerAdd() {
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

		DataSource db = null;
		try {
			db = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("lindDb",
				Collections.singletonMap("javax.persistence.nonJtaDataSource", db));
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Order entity = new Order();
		entity.setOrderId(179L);
		entity.setAmount(1);
		entity.setUserId(1000);
		entityManager.getTransaction().begin();
		entityManager.persist(entity);
		entityManager.getTransaction().commit();
	}

	// Repository插入
	static void HihernateInsert() throws SQLException {
		OrderRepository orderRepository = new OrderRepository();
		Order order = new Order();
		order.setAmount(101d);
		order.setOrderId(17L);
		order.setUserId(16);
		orderRepository.save(order);
	}

	// 原生查询
	static void select() throws SQLException {
		// 创建数据源
		DataSource dataSource = DbUtil.createDataSource();

		// 执行SQL
		try (Connection conn = dataSource.getConnection()) {
			String sql = "SELECT * FROM t_order WHERE order_id = ?";
			try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
				preparedStatement.setLong(1, 11L);
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						System.out.println(resultSet.getString("order_id"));
					}
				}
			}
		}
	}

	static void selectById() throws SQLException {
		OrderRepository orderRepository = new OrderRepository();
		orderRepository.findById(11L).forEach(o -> System.out.println(o));
	}

	static void selectByUser() throws SQLException {
		OrderRepository orderRepository = new OrderRepository();
		orderRepository.findByUserId(6).forEach(o -> System.out.println(o));
	}

	// 原生插入
	static void insert() throws SQLException {
		DataSource dataSource = DbUtil.createDataSource();
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement preparedStatement = conn
					.prepareStatement("insert into t_order(order_id,user_id,amount) values(?,?,?)");
			preparedStatement.setLong(1, 7L);
			preparedStatement.setLong(2, 40L);
			preparedStatement.setDouble(3, 4.0);
			preparedStatement.execute();
		}
	}

}
