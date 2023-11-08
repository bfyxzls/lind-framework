package com.lind.common.othertest;

import com.lind.common.minibase.Bytes;
import org.junit.Assert;
import org.junit.Test;

/**
 * byte[]与int和long的转换
 */
public class ByteToIntAndLong {

	// 将int数值转换为占四个字节的byte数组，(低位在前，高位在后的顺序)
	@Test
	public void byteAndInt() {
		int a = 257; // [0,0,1,1]
		// 每个byte有符号字符由8bit组成，取值为-128~127
		byte[] aByte = Bytes.toBytes(a);
		int b = Bytes.toInt(aByte);
		Assert.assertEquals(a, b);
	}

	@Test
	public void byteAndLong() {
		long a = 257; // [0,0,0,0,0,0,1,1]
		// 每个byte有符号字符由8bit组成，取值为-128~127
		byte[] aByte = Bytes.toBytes(a);
		long b = Bytes.toLong(aByte);
		Assert.assertEquals(a, b);
	}

}
