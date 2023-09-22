package com.lind.common.bean.conditional.onbean;

import lombok.RequiredArgsConstructor;

/**
 * @author lind
 * @date 2023/9/20 21:43
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class Cat {

	private final Fishing fishing;

	public void run() {
		fishing.hello();
		System.out.println("Cat run");
	}

}
