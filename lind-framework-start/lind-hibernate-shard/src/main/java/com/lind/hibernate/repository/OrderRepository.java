package com.lind.hibernate.repository;

import com.lind.hibernate.entity.Order;
import com.lind.hibernate.util.DbUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lind
 * @date 2024/2/26 9:04
 * @since 1.0.0
 */
public class OrderRepository {

	// 创建EntityManagerFactory
	EntityManagerFactory entityManagerFactory;

	// 创建EntityManager
	EntityManager entityManager;

	public OrderRepository() throws SQLException {
		this.entityManagerFactory = Persistence.createEntityManagerFactory("lindDb",
				getProperties(DbUtil.createDataSource()));
		this.entityManager = entityManagerFactory.createEntityManager();
	}

	private static Map<String, Object> getProperties(DataSource dataSource) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("javax.persistence.nonJtaDataSource", dataSource);
		return properties;
	}

	public List<Order> findById(Long id) {
		return entityManager.createNamedQuery("findById", Order.class).setParameter("orderId", id).getResultList();
	}

	public List<Order> findByUserId(Integer userId) {
		return entityManager.createNamedQuery("findByUserId", Order.class).setParameter("userId", userId)
				.getResultList();
	}

	public void save(Order order) {
		entityManager.getTransaction().begin();
		entityManager.persist(order);
		entityManager.getTransaction().commit();
		entityManager.close();
		entityManagerFactory.close();
	}

}
