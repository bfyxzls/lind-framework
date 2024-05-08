package com.lind.common.pattern;

import com.lind.common.pattern.state.demo.Context;
import com.lind.common.pattern.state.demo.StartState;
import org.junit.jupiter.api.Test;

public class StateTest {

	@Test
	public void statePatternDemo() {
		// 状态模式，定义的状态的变更，类似于工作流.
		Context context = new Context(new StartState());
		context.printStatus();

		context.next();
		context.printStatus();

		context.next();
		context.printStatus();

		context.next();
	}

}
