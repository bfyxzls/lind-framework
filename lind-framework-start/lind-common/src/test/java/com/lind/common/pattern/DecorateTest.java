package com.lind.common.pattern;

import com.lind.common.pattern.decorate.*;
import com.lind.common.pattern.decorate2.*;
import org.junit.Test;

public class DecorateTest {

	@Test
	public void drink() {
		Drink coffee = new Coffee();
		AbstractDrinkDecorate milk = new MilkAbstractDrinkDecorate(coffee);
		AbstractDrinkDecorate sugar = new SugarAbstractDrinkDecorate(milk);
		System.out.println(sugar.printer());
		System.out.println("总计：" + sugar.cost());
		System.out.println("----- ------------------");
		Drink tea = new Tea();
		sugar = new SugarAbstractDrinkDecorate(tea);
		System.out.println(sugar.printer());
		System.out.println("总计：" + sugar.cost());
	}

	@Test
	public void human() {
		Human human = new SuperMan("牛肉", "工人");
		FlyDecorator flyDecorator = new SuperManFlyDecorator(human);
		SwimHumanDecorator swimHumanDecorator = new SwimHumanDecorator(flyDecorator);
		flyDecorator.run(); // 人类跑起来 超人会飞
		swimHumanDecorator.run();// 人类跑起来 超人会飞 会游泳
	}

}
