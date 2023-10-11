package com.lind.common.othertest;

import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 依赖注入测试.
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class IocTest {

	@Autowired
	ARepository aRepository;

	// 属性注入
	@Autowired
	People people;

	@Test
	public void setTest() {
		System.out.println(keyCloak.context);
		aRepository.printa();
	}

}

@Component
class People {

	/**
	 * 注入后，把值赋值其它属性或者方法，不用在外面定义一个ApplicationContext了.
	 * @param context
	 */
	@Autowired
	public void setApplicationContext(ApplicationContext context) {
		keyCloak.setLindContext(context);
	}

	public void print() {
		System.out.println("print People");
	}

	public void init() {

	}

}

@Component
@RequiredArgsConstructor
class ARepository {

	// 通过@RequiredArgsConstructor完成构造方法注入
	private final People people;

	public void printa() {
		people.print();
	}

}

class keyCloak {

	public static ApplicationContext context;

	public static void setLindContext(ApplicationContext context) {
		keyCloak.context = context;
	}

}
