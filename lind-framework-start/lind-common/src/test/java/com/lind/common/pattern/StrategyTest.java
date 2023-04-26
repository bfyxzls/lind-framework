package com.lind.common.pattern;

import com.lind.common.pattern.strategy.Animal;
import com.lind.common.pattern.strategy.Bird;
import com.lind.common.pattern.strategy.Dog;
import com.lind.common.pattern.strategy.Eagle;
import com.lind.common.pattern.strategy.StrategyContext;
import org.junit.Test;

public class StrategyTest {

	@Test
	public void test() {

		Animal dog = new Dog();
		StrategyContext strategyContext = new StrategyContext(dog);
		strategyContext.fly();

		Animal bird = new Bird();
		strategyContext = new StrategyContext(bird);
		strategyContext.fly();

		Animal eagle = new Eagle();
		strategyContext = new StrategyContext(eagle);
		strategyContext.fly();
	}

}
