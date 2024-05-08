package com.lind.common.bean.property_switch_bean;

import com.lind.common.bean.property_switch_bean.service.Lind;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lind
 * @date 2022/10/17 10:12
 * @since 1.0.0
 */
@SpringBootTest()
public class LindTest {

	@Autowired
	Lind lind;

	@Test
	public void productInit() {
		lind.print();
	}

}
