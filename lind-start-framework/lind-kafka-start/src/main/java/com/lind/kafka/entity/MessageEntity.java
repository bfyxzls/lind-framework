package com.lind.kafka.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息实体.
 *
 * @param <T>
 */
@Data
public class MessageEntity<T> implements Serializable, MessageEntityAware {

	private static final long serialVersionUID = 3377490669064560046L;

	/**
	 * 消费者需要做的操作
	 */
	private String operator;

	/**
	 * 消息体
	 */
	private T data;

	/**
	 * 发送消息的用户名
	 */
	private String sendUser;

	/**
	 * 发送消息的用户名
	 */
	private String sendUserId;

	/**
	 * 发送时间
	 */
	private Date sendTime;

}
