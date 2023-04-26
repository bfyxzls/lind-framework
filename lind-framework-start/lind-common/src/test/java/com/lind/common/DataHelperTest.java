package com.lind.common;

import com.lind.common.util.DataHelper;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

public class DataHelperTest {

	/**
	 * 8秒处理400个任务，每个任务执行时间为1S，并行的威力
	 */
	@Test
	public void test() {
		List<Integer> sumList = new ArrayList<>();
		for (int i = 0; i < 400; i++) {
			sumList.add(i);
		}
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		DataHelper.fillDataByPage(sumList, 100, (o) -> {
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		stopWatch.stop();
		System.out.println("time:" + stopWatch.getTotalTimeMillis());
	}

}
