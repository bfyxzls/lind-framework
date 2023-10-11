package com.lind.common.othertest;

import org.junit.Test;

/**
 * full gc test
 */
public class FullGcTest {

	@Test
	public void test() {
		/**
		 * -verbose:gc 会输出详细的垃圾回收信息 -Xms20M 初始值20m 堆空间 -Xmx20M 最大值20m -Xmn10M
		 * 堆空间中，新生代的大小是10m -XX:+PrintGC -XX:+PrintHeapAtGC -XX:+PrintGCTimeStamps
		 * -XX:+PrintGCDetails 打印出垃圾回收的详细信息。 -XX:SurvivorRatio=8 eden:survivor 是8:1
		 * 但是survivor是两个，每个都是8：1，即10M新生代对接eden为8M,s1为1M，s2也为1M。
		 *
		 * 因此栈内空间分布为：eden区8M，survivor 两个区分别是1M，老年代10M。
		 */
		int size = 1024 * 1024;
		byte[] myalloc1 = new byte[2 * size];
		System.out.println("test full gc1");

		byte[] myalloc2 = new byte[2 * size];
		System.out.println("test full gc2");

		byte[] myalloc3 = new byte[2 * size];
		System.out.println("test full gc3");

		byte[] myalloc4 = new byte[2 * size];
		System.out.println("test full gc4");
	}

}
