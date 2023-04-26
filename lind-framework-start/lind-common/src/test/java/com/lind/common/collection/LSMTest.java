package com.lind.common.collection;

import org.junit.Test;

import java.util.Random;

/**
 * LSM树算法测试
 */
public class LSMTest {

	/**
	 * 计算一个随机的高度值，p是一个（0，1）之间的常数，一般取1/4或者1/2
	 * @param p
	 * @return
	 */
	public int randomHeight(double p) {
		int height = 0;
		while (new Random().nextDouble() < p)
			height++;
		return height + 1;
	}

	@Test
	public void getHeight() {
		int i = 100;
		while (i-- > 0) {
			System.out.println(randomHeight(0.5));
		}

	}

}
