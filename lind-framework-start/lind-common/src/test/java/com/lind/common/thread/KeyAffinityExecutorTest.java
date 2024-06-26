package com.lind.common.thread;

import com.alibaba.fastjson.JSON;
import com.github.phantomthief.pool.KeyAffinityExecutor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lind
 * @date 2023/5/16 13:37
 * @since 1.0.0
 */
public class KeyAffinityExecutorTest {

	static KeyAffinityExecutor executor = KeyAffinityExecutor.newSerializingExecutor(8, 200, "MY-POOL");

	public static void executeByAffinitydPool(List<ThreadPoolUtilsTest.Person> personList) {
		personList.stream().forEach(p -> executor.executeEx(p.getId(), () -> {
			System.out.println(
					Thread.currentThread().getName() + Thread.currentThread().getId() + ":" + JSON.toJSONString(p));
		}));
	}

	/**
	 * Simple pool保存了有序性
	 * @throws InterruptedException
	 */
	@Test
	public void phantomthiefSimplePool() throws InterruptedException {
		// 1.创建列表
		List<ThreadPoolUtilsTest.Person> personList = new ArrayList<>();
		personList.add(ThreadPoolUtilsTest.Person.builder().id(1).data("1s").build());
		personList.add(ThreadPoolUtilsTest.Person.builder().id(2).data("2s").build());
		personList.add(ThreadPoolUtilsTest.Person.builder().id(1).data("11s").build());
		personList.add(ThreadPoolUtilsTest.Person.builder().id(3).data("3s").build());
		personList.add(ThreadPoolUtilsTest.Person.builder().id(1).data("111s").build());
		personList.add(ThreadPoolUtilsTest.Person.builder().id(2).data("22s").build());
		personList.add(ThreadPoolUtilsTest.Person.builder().id(3).data("33s").build());
		personList.add(ThreadPoolUtilsTest.Person.builder().id(1).data("1111s").build());

		executeByAffinitydPool(personList);

		Thread.sleep(1000);
		/**
		 * id相同的使用同一个线程池 MY-POOL11:{"data":"1s","id":1} MY-POOL12:{"data":"2s","id":2}
		 * MY-POOL13:{"data":"3s","id":3} MY-POOL11:{"data":"11s","id":1}
		 * MY-POOL11:{"data":"111s","id":1} MY-POOL11:{"data":"1111s","id":1}
		 * MY-POOL13:{"data":"33s","id":3} MY-POOL12:{"data":"22s","id":2}
		 */
	}

}
