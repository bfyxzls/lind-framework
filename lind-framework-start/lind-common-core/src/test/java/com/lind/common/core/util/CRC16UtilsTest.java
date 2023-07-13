package com.lind.common.core.util;

import org.junit.Test;

import static com.lind.common.core.util.CRC16Utils.CRC_XModem;
import static com.lind.common.core.util.CRC16Utils.crc16;
import static com.lind.common.core.util.CRC16Utils.keyHashSlot;

/**
 * @author lind
 * @date 2023/7/13 14:13
 * @since 1.0.0
 */
public class CRC16UtilsTest {

	@Test
	public void main() {
		System.out.println(crc16("test3") % 16383); // 输出13026(redis key 0~16383)
		System.out.println(crc16("hello") % 16383); // 输出866
		System.out.println("group01".hashCode()); // 输出293427392
		System.out.println("group02".hashCode()); // 输出293427393
		System.out.println("group03".hashCode()); // 输出293427394
		System.out.println("group04".hashCode()); // 输出293427395
		System.out.println("crc16('group01') & 16383=" + crc16("group01") % 16383); // 输出7544
		System.out.println(crc16("group01") & 16383); // 输出11547
		System.out.println(crc16("group03") & 16383); // 输出15674
		System.out.println(crc16("group04")); // 输出19933
		System.out.println(keyHashSlot("group01")); // 输出7544
		System.out.println(keyHashSlot("group02")); // 输出11547
		System.out.println(keyHashSlot("group03")); // 输出15674
		System.out.println(keyHashSlot("group04")); // 输出3549
		System.out.println(keyHashSlot("{group04}00")); // 输出3549
		System.out.println(keyHashSlot("{group04}10")); // 输出3549
		System.out.println(CRC_XModem("group01".getBytes())); // hash输出7544，和模拟redis的crc16算出结果一致
		System.out.println(CRC_XModem("group02".getBytes())); // hash输出11547，和模拟redis的crc16算出结果一致
		System.out.println(CRC_XModem("group03".getBytes())); // hash输出15674，和模拟redis的crc16算出结果一致
		System.out.println(CRC_XModem("group04".getBytes())); // hash输出19933，和模拟redis的crc16算出结果一致
	}

}
