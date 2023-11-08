package com.lind.common.othertest;

import com.lind.common.core.util.BinHexSwitchUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * 位运算.
 */
@Slf4j
public class BitOperationTest {

	private static final ByteBuffer buffer = ByteBuffer.allocate(8);

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
		int[] arr = new int[bf.length()];
		for (int i = 0; i < bf.length(); i++) {
			arr[i] = bf.charAt(i) - '0';
			System.out.println(Math.pow(2, arr[i]));
		}
	}

	@Test
	public void flag() {
		int a = 1;
		int b = a << 2 << 2; // 左移1位乘以2的1次方，左移2位乘以2的2次方，左移4位乘以2的4次方
		System.out.println("b=" + b);

		// 包含
		int c = b & 4;
		System.out.println("b contaions 4:" + c);

		// 相加
		int d = 4;
		int e = b | d;
		System.out.println("b+d=" + e);

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
		System.out.printf("run=%s,stop=%s,tidying=%s,terminated=%s%n", RUNNING, STOP, TIDYING, TERMINATED);

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

	@Test
	public void writeShort() throws IOException {
		/**
		 * 对一个数字n与0xff做与运算，相当于将n的二进制表示的低8位保留下来，并将高24位清零。
		 *
		 * 0xff（十进制为255）的二进制表示为11111111，它是一个8位二进制数，与任何一个整数做与运算都只能得到这个整数的低8位（即最右边的8位），其余位都会被清零。
		 *
		 * 例如，数字128的二进制表示为10000000，如果对它执行128 &
		 * 0xff，结果为0x80，与128的二进制表示10000000保留的是最右边的8位，而其余位置0，因此返回的值为0x80。
		 * 再比如，数字256的二进制表示为100000000，如果对它执行256 &
		 * 0xff，结果为0，与256的二进制表示100000000保留的是最右边的8位，而其余位置0，因此返回的值为0。
		 * 这个操作在进行位运算和字节数组处理时经常使用，尤其是在处理有符号和无符号整数的补码表示时，有助于保留整数的正确值。
		 */
		int v = 9;// 32768
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write((v >>> 8) & 0xFF);// 0xff=255,>>>无符号右移
		outputStream.write((v >>> 0) & 0xFF);// 右移相当于除,右移1位相当于除以2,右移8位相当于2的8次方256
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
		int result = dis.readShort();
		System.out.println(result);
		String hex16 = BinHexSwitchUtils.bytesToHexString(outputStream.toByteArray());
		System.out.println(hex16);
		BinHexSwitchUtils.hexStringToBytes(hex16);

	}
	/*
	 * 这段代码的作用是将字节数组this.buffer中从offset位置开始的4个字节解析为一个大端序的无符号整数，并将其对一个非负整数this.
	 * modDiviso取模后返回。
	 *
	 * 具体的解析过程如下：
	 *
	 * (this.buffer[offset] & 0x7f) 取字节数组 this.buffer 中从 offset 开始的第 1 个字节的后 7 位（即去除符号位）。
	 *
	 * 将这个 7 位的整数左移 24 位（即由高到低分别乘以2^24、2^16、2^8、2^0）。
	 *
	 * (this.buffer[offset + 1] & 0xff) 取字节数组 this.buffer 中从 offset+1 开始的第 2 个字节的所有 8 位。
	 *
	 * 将这个 8 位的整数左移 16 位。
	 *
	 * (this.buffer[offset + 2] & 0xff) 取字节数组 this.buffer 中从 offset+2 开始的第 3 个字节的所有 8 位。
	 *
	 * 将这个 8 位的整数左移 8 位
	 *
	 * icon
	 *
	 * "大端序的无符号整数" 是一种对于字节序列表示整数的方式，其中整数的最高有效位存储在字节序列的最前面（低地址）。
	 *
	 * 换而言之，在一个大端序的无符号整数中，字节的排列顺序是从左到右，最左边的字节包含了整数的最高有效位，最右边的字节包含了整数的最低有效位。
	 *
	 * 这里需要解释两个概念：
	 *
	 * "大端序" (Big-Endian)：表示将数值的高位字节存放在内存的低地址处，而将数值的低位字节存放在内存的高地址处。 "无符号整数" (Unsigned
	 * integer)：整数是没有符号（+/-）的数值，也就是说它只能表示正数和零。
	 *
	 * 举个例子，以下是十进制数1890278377和对应的16进制表示70FC0109，在大端序的无符号整数表示中它们是一致的：
	 *
	 * 70（十六进制）对应二进制 01110000 FC（十六进制）对应二进制 11111100 01（十六进制）对应二进制 00000001 09（十六进制）对应二进制
	 * 00001001
	 *
	 * 将它们按照大端序排列，就是70 FC 01 09。
	 *
	 * 因此，大端序的无符号整数采取的是从左到右的排列方式，最左边的字节包含了整数的最高有效位，最右边的字节包含了整数的最低有效位。
	 *
	 */

	@Test
	public void daduanxu() {
		int offset = 0;
		int modDiviso = 4;
		byte[] buffer = new byte[] { 1, 2, 3, 4 };
		int result = ((buffer[offset] & 0x7f) << 24 | (buffer[offset + 1] & 0xff) << 16
				| (buffer[offset + 2] & 0xff) << 8 | (buffer[offset + 3] & 0xff)) % modDiviso;
		System.out.println("result=" + result);

	}

	// 一个数对2^n取模，等价于这个数与2^n-1进行按位与运算
	@Test
	public void mod() {
		int result, result2;

		int bin = (int) Math.pow(2, 4) - 1;
		int dec = bin + 1;

		for (int i = 1; i < 100; i++) {
			result = i & bin;
			result2 = i % dec;
			Assert.assertEquals(result, result2);
		}
	}

	@Test
	public void max() {
		Integer max = 0xffffffff;
		Integer max2 = Integer.MAX_VALUE;
		System.out.println(max);// 溢出
		System.out.println(max2);

	}

}
