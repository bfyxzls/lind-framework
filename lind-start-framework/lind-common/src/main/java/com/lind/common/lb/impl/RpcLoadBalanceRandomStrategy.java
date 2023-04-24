package com.lind.common.lb.impl;

import com.lind.common.lb.RpcLoadBalance;

import java.util.Random;
import java.util.TreeSet;

/**
 * 随机
 *
 */
public class RpcLoadBalanceRandomStrategy extends RpcLoadBalance {

	private Random random = new Random();

	@Override
	public String route(String serviceKey, TreeSet<String> addressSet) {
		// arr
		String[] addressArr = addressSet.toArray(new String[addressSet.size()]);

		// random
		String finalAddress = addressArr[random.nextInt(addressSet.size())];
		return finalAddress;
	}

}
