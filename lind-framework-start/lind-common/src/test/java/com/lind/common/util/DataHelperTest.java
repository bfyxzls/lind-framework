package com.lind.common.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lind
 * @date 2022/7/29 13:42
 * @since 1.0.0
 */
public class DataHelperTest {

	@Test
	public void bulk() {
		List<Integer> integerList = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			integerList.add(i);
		}
		DataHelper.fillDataByPage(integerList, 100, o -> {
			System.out.println(o);
			try {
				Thread.sleep(1);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
	}

}
