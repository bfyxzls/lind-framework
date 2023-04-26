package com.lind.kafka.entity;

/**
 * 当前登陆人接口.
 */
public interface CurrentUserAware {

	String getCurrentUserName();

	String getCurrentUserId();

}
