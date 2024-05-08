package com.lind.common.algorithms;

import org.junit.jupiter.api.Test;

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
		int target = 22;
		System.out.println(Arrays.toString(twoSum(nums, target)));
	}

	static int[] twoSum(int[] nums, int target) {
		int[] result = new int[2];
		HashMap<Integer, Integer> hashdict = new HashMap<Integer, Integer>();
		for (int i = 0; i < nums.length; i++) {
			int t = target - nums[i];
			if (hashdict.containsKey(target - nums[i])) {
				result[0] = hashdict.get(target - nums[i]);
				result[1] = i;
				break;
			}
			hashdict.put(nums[i], i);
		}
		return result;
	}

	/**
	 * pfnl通过ID得到GID.
	 * @param id
	 * @return
	 */
	public static Long getGid(Long id) {
		return id | 0x0007000000000000L;
	}

	/**
	 * pfnl通过GID得到ID.
	 * @param gid
	 * @return
	 */
	public static Long getId(Long gid) {
		// 0x0000FFFFFFFFFFFFL为这个区间的自增数=它是最大值-初始值的差
		// 0x0000FFFFFFFFFFFFL=((0x0007000000000000-1)|0x0007000000000000)-0x0007000000000000
		return gid & 0x0000FFFFFFFFFFFFL;
	}

	@Test
	public void pretice() {
		int start = 0xA000;
		int end = ((start - 1) | start) - start; // 0x1FFF
		System.out.println("end=" + end);
	}

	@Test
	public void articleGid() {
		long a = 0x0007000000000000L;
		long fan64 = ((a - 1) | a) - a;
		System.out.println("fan64=" + fan64);
		int fan = ((0x6A000000 - 1) | 0x6A000000) - 0x6A000000;
		System.out.println("fan=" + fan);
		long gid = 0x6A000000 | 33554430;
		long id = gid & 0x1FFFFFF;
		System.out.println("gid=" + gid);
		System.out.println("id=" + id);
	}

}
