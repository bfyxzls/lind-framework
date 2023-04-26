package com.lind.common.util;

import com.lind.common.encrypt.HashUtils;
import com.lind.common.zip.LZW;
import com.lind.common.zip.LZWUtils;
import com.lind.common.zip.RLEUtils;
import com.lind.common.zip.ZLibUtils;
import com.lind.common.zip.ZipUtils;
import lombok.SneakyThrows;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZipTest {

	@Test
	public void zipUtils() throws Exception {
		// 压缩文件
		ZipUtils.compress("d:\\20210513会议纪要.docx");
		// 压缩目录
		ZipUtils.compress("d:\\zip");
	}

	@SneakyThrows
	@Test
	public void rle() {
		String code = "DBDCDDDEDFDGDHDIDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDADAFPFGDG";
		System.out.println("rle=" + RLEUtils.encode(code));
	}

	@SneakyThrows
	@Test
	public void lzw() {
		String code = "abcaaabbbb";
		System.out.println("rle=" + LZW.compress(code));
		System.out.println("rle=" + LZW.decompress(LZW.compress(code)));
	}

	@SneakyThrows
	@Test
	public void lzwUtils() {
		String code = "abcd123WQAab123";
		String encode = LZWUtils.compress(code);
		System.out.println("rle=" + encode);
		System.out.println("rle=" + LZWUtils.decompress(encode));
	}

	@Test
	public void stringGet() {
		String str = Character.toString((char) Character.codePointAt("hello", 0));
		System.out.println("string=" + str);

		char[] c1 = { '明', '日', '科', '技' };
		String cha = Character.toString((char) Character.codePointAt(c1, 2));
		System.out.println(cha);

		System.out.println(Character.toString((char) 97));

		System.out.println(Character.toString((char) 256));
	}

	@Test
	public final void testBytes() {
		System.err.println("字节压缩／解压缩测试");
		String inputStr = "12345678_f_20200612_D_100_V6";
		System.err.println("输入字符串:\t" + inputStr);
		byte[] input = inputStr.getBytes();
		System.err.println("输入字节长度:\t" + input.length);

		byte[] data = ZLibUtils.compress(input);
		System.err.println("压缩后字节长度:\t" + data.length);
		System.err.println("压缩后字符：\t" + HashUtils.encryptBASE64(data) + "\t" + HashUtils.encryptBASE64(data).length());

		byte[] output = ZLibUtils.decompress(data);
		System.err.println("解压缩后字节长度:\t" + output.length);
		String outputStr = new String(output);
		System.err.println("输出字符串:\t" + outputStr);

		assertEquals(inputStr, outputStr);
	}

}