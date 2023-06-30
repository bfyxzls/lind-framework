package com.lind.common.pattern.observer;

/**
 * 实现具体的被观察者类
 * @author lind
 * @date 2023/6/27 15:06
 * @since 1.0.0
 */

import java.util.List;
import java.util.Vector;

public class ConcreteSubject implements Subject {

	private List<Observer> observers = new Vector<>();

	@Override
	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObservers(String message) {
		for (Observer observer : observers) {
			observer.update(message);
		}
	}

}
