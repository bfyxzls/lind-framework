package com.lind.common;

import org.junit.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * 位运算.
 */
public class BitOperationTest {

	private static ByteBuffer buffer = ByteBuffer.allocate(8);

	// long类型转byte[]
	public static byte[] longToBytes(long x) {
		buffer.putLong(0, x);
		return buffer.array();
	}

	// byte[]转long类型
	public static long bytesToLong(byte[] bytes) {

		buffer.put(bytes, 0, bytes.length);

		// flip方法将Buffer从写模式切换到读模式，调用flip()方法会将position设回0，从头读起

		buffer.flip();

		return buffer.getLong();

	}

	@Test
	public void or() {
		boolean a = true;
		boolean b = false;
		a |= b;
		System.out.println("a=" + a);

		boolean c = false;
		boolean d = true;
		c |= d;
		System.out.println("c=" + c);

	}

	@Test
	public void binPower() {
		int a = 1 | 2 | 4 | 8;
		System.out.println(a);

	}

	/**
	 * 分解2的N次方的和由哪些数组成.
	 */
	@Test
	public void split2Power() {
		System.out.println(Math.pow(2, 3));
		String numStr = Integer.toBinaryString(15);

		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < numStr.length(); i++) {
			if (numStr.charAt(i) != '0') {
				bf.append(numStr.length() - 1 - i);
			}
		}
		int arr[] = new int[bf.length()];
		for (int i = 0; i < bf.length(); i++) {
			arr[i] = bf.charAt(i) - '0';
			System.out.println(Math.pow(2, arr[i]));
		}
	}

	@Test
	public void flag() {
		int a = 1;
		int b = a << 2 << 2;
		System.out.println("b=" + b);

		// 包含
		int c = b & 4;
		System.out.println("b contaions 4:" + c);

		// 相加
		int d = 4;
		int e = b | d;
		System.out.println("b+d=" + e);

		int c2 = e & 4;
		System.out.println("e contaions 4:" + c2);

		// 相减
		int f = b & (~d);
		System.out.println("b-d=" + f);

	}

	@Test
	public void status_index_capacity() {
		int maxBig = 8;
		int statusBig = maxBig - 4; // 状态位是4位
		int CAPACITY = (1 << statusBig) - 1; // 每一位（槽）存15个数

		int RUNNING = 0 << statusBig; // 0*2^4=0
		int STOP = 1 << statusBig; // 1*2^4=16
		int TIDYING = 2 << statusBig;// 2*2^4=32
		int TERMINATED = 3 << statusBig;// 3*2^4=48
		System.out
				.println(String.format("run=%s,stop=%s,tidying=%s,terminated=%s", RUNNING, STOP, TIDYING, TERMINATED));

		int ctl = 15 | TIDYING;// TIDYING的第3个增量
		System.out.println("ctl=" + ctl);
		int status = ctl & ~CAPACITY;
		int index = ctl & CAPACITY;
		System.out.println("status=" + status + ",index=" + index);// ctl=47,status=32,index=15

	}

	/**
	 * bigInteger is 64 bit(8 byte).
	 */
	@Test
	public void long_16radix_string() {
		Long id = 4294967296L;
		String str = new BigInteger(longToBytes(id)).toString(16);
		System.out.println("1 's 16 radix string=" + str);
	}

}
