package com.lind.common.bean.conditional.onbean;

import org.springframework.stereotype.Component;

/**
 * 掉鱼？当你去掉鱼后，你才能吃，所有FishFood应该依赖于Fishing.
 */
@Component
public class Fishing {

	public void hello() {
		System.out.println("掉鱼");
	}

}
