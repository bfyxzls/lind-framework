package com.lind.common.bean.definition;

/**
 * @author lind
 * @date 2023/5/26 9:10
 * @since 1.0.0
 */
public class Car implements PrintInfo{

	@Override
	public void print() {
		System.out.println("car");
	}

}
