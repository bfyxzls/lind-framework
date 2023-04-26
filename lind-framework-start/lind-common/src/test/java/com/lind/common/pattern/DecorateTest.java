package com.lind.common.pattern;

import com.lind.common.pattern.decorate.Coffee;
import com.lind.common.pattern.decorate.AbstractDrinkDecorate;
import com.lind.common.pattern.decorate.Drink;
import com.lind.common.pattern.decorate.MilkAbstractDrinkDecorate;
import com.lind.common.pattern.decorate.SugarAbstractDrinkDecorate;
import com.lind.common.pattern.decorate.Tea;
import com.lind.common.pattern.decorate2.FlyDecorator;
import com.lind.common.pattern.decorate2.Human;
import com.lind.common.pattern.decorate2.SuperMan;
import com.lind.common.pattern.decorate2.SuperManFlyDecorator;
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
		flyDecorator.run();
	}

}
