package com.lind.common;

import com.lind.common.core.id.LindIdGenerator;
import org.junit.Test;

/**
 * byte[]到字符的手工的转换，根据转的参数，toString()方法生成一个格式化的字符串.
 * 将一个有特殊含义的字符串进行拆分，形成一个byte[],类型class文件的组成
 *
 * @author lind
 * @date 2023/1/4 13:12
 * @since 1.0.0
 */
public class LindIDTest {

	@Test
	public void StrToByte() {
		// 类型char（2字节）-单词长度int(2字节）-具体单词-增量int(4字节)
		String UID = "AB-7-battery-10";
		LindIdGenerator uid = new LindIdGenerator("AB", "english", 1);
		byte[] arr = uid.toByte();
		System.out.printf("arr=" + uid);
	}

	@Test
	public void validate() {
		LindIdGenerator.validate("1-1-2");
	}

}
