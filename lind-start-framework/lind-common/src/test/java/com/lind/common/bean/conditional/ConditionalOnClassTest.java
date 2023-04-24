package com.lind.common.bean.conditional;

import com.lind.common.bean.conditional.missingbean.MissingBean;
import com.lind.common.bean.conditional.onbean.FishFood;
import com.lind.common.test.Eat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class ConditionalOnClassTest {

	@Autowired
	MissingBean missingBean;

	@Autowired
	FishFood fishFood;

	@Autowired
	Eat eat;

	/**
	 * @ConditionalOnMissingBean.
	 */
	@Test
	public void onMissingBean() {
		missingBean.hello();
	}

	/**
	 * @ConditionalOnBean.
	 */
	@Test
	public void dependOnClass() {
		fishFood.hello();
	}

	@Test
	public void eatTest() {
		eat.drink();
	}

}
