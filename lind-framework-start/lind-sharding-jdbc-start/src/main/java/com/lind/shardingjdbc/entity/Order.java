package com.lind.shardingjdbc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author lind
 * @date 2023/5/24 9:15
 * @since 1.0.0
 */
@Data
@TableName("t_order")
public class Order {

	@TableId(type = IdType.ASSIGN_ID)
	Long orderId;

	double amount;

	Integer userId;

}
