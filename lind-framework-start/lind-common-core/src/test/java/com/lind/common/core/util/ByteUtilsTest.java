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
		byte[] source = ByteUtils.toBytes("hello world");
		byte[] result = new byte[8];
		System.arraycopy(source, 0, result, 0, 5);
		System.arraycopy(ByteUtils.toBytes(" ok"), 0, result, 5, 3);
		System.out.println(ByteUtils.toHex(result));
		System.out.println(ByteUtils.toHex(ByteUtils.toBytes(" ok")));
	}

	// 将int数值转换为占四个字节的byte数组，(低位在前，高位在后的顺序)
	@Test
	public void byteAndInt() {
		int a = 257; // [0,0,1,1]
		// 每个byte有符号字符由8bit组成，取值为-128~127
		byte[] aByte = ByteUtils.toBytes(a);
		int b = ByteUtils.toInt(aByte);
		int c = ByteUtils.toInt(aByte);
		Assert.assertTrue(a == b && a == c);
	}

	@Test
	public void byteAndLong() {
		long a = 257; // [0,0,0,0,0,0,1,1]
		// 每个byte有符号字符由8bit组成，取值为-128~127
		byte[] aByte = ByteUtils.toBytes(a);
		long b = ByteUtils.toLong(aByte);
		Assert.assertTrue(a == b);
	}

	@SneakyThrows
	@Test
	public void slice() {
		byte[] aByte = ByteUtils.toBytes("abcde");
		byte[] expect = ByteUtils.toBytes("ab");
		byte[] dynamic = ByteUtils.slice(aByte, 0, 2);

		assert ByteUtils.compare(dynamic, expect) == 0;
	}

	@SneakyThrows
	@Test
	public void arraycopy() {
		byte[] result = new byte[10];
		byte[] name = ByteUtils.toBytes("0001");
		byte[] type = ByteUtils.toBytes("ZHFW");
		int pos = 0;
		System.arraycopy(type, 0, result, pos, type.length);
		pos += type.length;
		System.arraycopy(name, 0, result, pos, name.length);
		System.out.println(new String(result));
	}

}
