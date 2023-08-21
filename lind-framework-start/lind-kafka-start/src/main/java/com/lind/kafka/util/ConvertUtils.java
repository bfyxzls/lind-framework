package com.lind.kafka.util;

/**
 * @author lind
 * @date 2023/8/21 11:37
 * @since 1.0.0
 */
public class ConvertUtils {

	/**
	 * 将一个8位字节数组转换为长整数。<br>
	 * 注意，函数中不会对字节数组长度进行判断，请自行保证传入参数的正确性。
	 * @param b 字节数组
	 * @return 长整数
	 */
	public static long toLong(byte[] b) {
		long l = ((long) b[0] << 56) & 0xFF00000000000000L;
		// 如果不强制转换为long，那么默认会当作int，导致最高32位丢失
		l |= ((long) b[1] << 48) & 0xFF000000000000L;
		l |= ((long) b[2] << 40) & 0xFF0000000000L;
		l |= ((long) b[3] << 32) & 0xFF00000000L;
		l |= ((long) b[4] << 24) & 0xFF000000L;
		l |= ((long) b[5] << 16) & 0xFF0000L;
		l |= ((long) b[6] << 8) & 0xFF00L;
		l |= (long) b[7] & 0xFFL;
		return l;
	}

	/**
	 * 将一个长整数转换位字节数组(8个字节)，b[0]存储高位字符，大端
	 * @param l 长整数
	 * @return 代表长整数的字节数组
	 */
	public static byte[] toBytes(long l) {
		byte[] b = new byte[8];
		b[0] = (byte) (l >>> 56);
		b[1] = (byte) (l >>> 48);
		b[2] = (byte) (l >>> 40);
		b[3] = (byte) (l >>> 32);
		b[4] = (byte) (l >>> 24);
		b[5] = (byte) (l >>> 16);
		b[6] = (byte) (l >>> 8);
		b[7] = (byte) (l);
		return b;
	}

}
