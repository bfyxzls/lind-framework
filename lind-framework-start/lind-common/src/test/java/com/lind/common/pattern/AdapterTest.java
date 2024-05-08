package com.lind.common.pattern;

import com.lind.common.pattern.adapter.ConcreteTarget;
import com.lind.common.pattern.adapter.NewProjectAdapter;
import com.lind.common.pattern.adapter.Service;
import com.lind.common.pattern.adapter.Target;
import org.junit.jupiter.api.Test;

public class AdapterTest {

	@Test
	public void test() {
		Target adapter = new NewProjectAdapter();
		new Service().doSomethings(adapter);

		Target target = new ConcreteTarget();
		new Service().doSomethings(target);
	}

}
