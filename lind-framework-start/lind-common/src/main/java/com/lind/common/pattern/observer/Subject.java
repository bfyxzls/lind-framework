package com.lind.common.pattern.observer;

/**
 * 定义被观察者接口
 *
 * @author lind
 * @date 2023/6/27 15:06
 * @since 1.0.0
 */
public interface Subject {

	void registerObserver(Observer observer);

	void removeObserver(Observer observer);

	void notifyObservers(String message);

}
