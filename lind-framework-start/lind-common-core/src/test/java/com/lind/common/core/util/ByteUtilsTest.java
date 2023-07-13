package com.lind.common.core.util;

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * byteUtils方法测试.
 */
public class ByteUtilsTest {

	@Test
	public void fromByteRead() throws IOException {
		byte[] source = ByteUtil.toBytes("hello world");
		byte[] result = new byte[8];
		System.arraycopy(source, 0, result, 0, 5);
		System.arraycopy(ByteUtil.toBytes(" ok"), 0, result, 5, 3);
		System.out.println(ByteUtil.toHex(result));
		System.out.println(ByteUtil.toHex(ByteUtil.toBytes(" ok")));
	}

	// 将int数值转换为占四个字节的byte数组，(低位在前，高位在后的顺序)
	@Test
	public void byteAndInt() {
		int a = 257; // [0,0,1,1]
		// 每个byte有符号字符由8bit组成，取值为-128~127
		byte[] aByte = ByteUtil.toBytes(a);
		int b = ByteUtil.toInt(aByte);
		int c = ByteUtil.toInt(aByte);
		Assert.assertTrue(a == b && a == c);
	}

	@Test
	public void byteAndLong() {
		long a = 257; // [0,0,0,0,0,0,1,1]
		// 每个byte有符号字符由8bit组成，取值为-128~127
		byte[] aByte = ByteUtil.toBytes(a);
		long b = ByteUtil.toLong(aByte);
		Assert.assertTrue(a == b);
	}

	@SneakyThrows
	@Test
	public void slice() {
		byte[] aByte = ByteUtil.toBytes("abcde");
		byte[] expect = ByteUtil.toBytes("ab");
		byte[] dynamic = ByteUtil.slice(aByte, 0, 2);

		assert ByteUtil.compare(dynamic, expect) == 0;
	}

	@SneakyThrows
	@Test
	public void arraycopy() {
		byte[] result = new byte[10];
		byte[] name = ByteUtil.toBytes("0001");
		byte[] type = ByteUtil.toBytes("ZHFW");
		int pos = 0;
		System.arraycopy(type, 0, result, pos, type.length);
		pos += type.length;
		System.arraycopy(name, 0, result, pos, name.length);
		System.out.println(new String(result));
	}

}
