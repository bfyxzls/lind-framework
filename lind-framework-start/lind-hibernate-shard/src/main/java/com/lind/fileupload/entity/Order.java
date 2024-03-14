package com.lind.fileupload.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author lind
 * @date 2023/5/24 9:15
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "t_order")
@NamedQueries({ @NamedQuery(name = "findById", query = "FROM Order t WHERE t.orderId = :orderId"),
		@NamedQuery(name = "findByUserId", query = "FROM Order t WHERE t.userId = :userId") })
public class Order {

	@Id
	@Column(name = "order_id")
	Long orderId;

	@Column(name = "amount")
	double amount;

	@Column(name = "user_id")
	Integer userId;

}
