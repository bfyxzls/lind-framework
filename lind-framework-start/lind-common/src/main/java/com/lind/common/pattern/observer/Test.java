package com.lind.common.pattern.observer;

/**
 * @author lind
 * @date 2023/6/27 15:07
 * @since 1.0.0
 */
public class Test {

	public static void main(String[] args) {
		ConcreteSubject subject = new ConcreteSubject();
		Observer observer1 = new ConcreteObserver("Observer 1");
		Observer observer2 = new ConcreteObserver("Observer 2");

		subject.registerObserver(observer1);
		subject.registerObserver(observer2);

		subject.notifyObservers("Hello, observers!");

		subject.removeObserver(observer2);

		subject.notifyObservers("Goodbye, observer 2!");
	}

}
