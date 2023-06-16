package com.lind.common.algorithms;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author lind
 * @date 2023/5/30 15:17
 * @since 1.0.0
 */
public class TwoSum {

	public static void main(String[] args) {
		int[] nums = { 2, 7, 11, 15 };
		int target = 18;
		System.out.println(Arrays.toString(twoSum(nums, target)));
	}

	static int[] twoSum(int[] nums, int target) {
		int[] result = new int[2];
		HashMap<Integer, Integer> hashdict = new HashMap<Integer, Integer>();
		for (int i = 0; i < nums.length; i++) {
			if (hashdict.containsKey(target - nums[i])) {
				result[0] = hashdict.get(target - nums[i]);
				result[1] = i;
				break;
			}
			hashdict.put(nums[i], i);
		}
		return result;
	}

}
