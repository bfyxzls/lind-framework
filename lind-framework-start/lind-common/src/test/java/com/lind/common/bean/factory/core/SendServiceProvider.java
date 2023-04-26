package com.lind.common.bean.factory.core;

public interface SendServiceProvider<T> {

	void insert(T t);

	void delete(T t);

}
