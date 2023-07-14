package com.lind.common.core.id;

import com.lind.common.core.util.ByteUtils;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * 设计一个主键生成器,支持前缀，中缀，最后是增量，自定义byte[]和ToString().
 * byte[]到字符的手工的转换，根据转的参数，toString()方法生成一个格式化的字符串.
 * 将一个有特殊含义的字符串进行拆分，形成一个byte[],类型class文件的组成
 *
 * @author lind
 * @date 2023/1/4 16:08
 * @since 1.0.0
 */
public class LindIdGenerator implements Serializable {

	final static short TYPE_LENGTH = 2;

	final static short SUB_LENGTH = 4;

	final static short OFFSET_LENGTH = 4;

	byte[] bytes;

	String type;

	int subjectLength;

	String subject;

	int offset;

	public LindIdGenerator(String type, String subject, int offset) {
		this.type = type;
		this.subjectLength = subject.length();
		this.subject = subject;
		this.offset = offset;
		bytes = new byte[TYPE_LENGTH + SUB_LENGTH + subjectLength + OFFSET_LENGTH];

	}

	/**
	 * 验证ID合法性.
	 * @param id
	 */
	public static void validate(String id) {
		Pattern DOT = Pattern.compile("\\-");
		if (DOT.split(id).length != 4) {
			throw new IllegalArgumentException("LindID is not match.");
		}
	}

	/**
	 * 转成byte数组,控制每一位.
	 * @return
	 */
	public byte[] toByte() {
		int index = 0;
		System.arraycopy(type.getBytes(), 0, bytes, index, TYPE_LENGTH);
		index += TYPE_LENGTH;
		System.arraycopy(ByteUtils.toBytes(subjectLength), 0, bytes, index, SUB_LENGTH);
		index += SUB_LENGTH;
		System.arraycopy(subject.getBytes(), 0, bytes, index, subjectLength);
		index += subjectLength;
		System.arraycopy(ByteUtils.toBytes(offset), 0, bytes, index, OFFSET_LENGTH);
		return bytes;
	}

	/**
	 * 从byte数组每一位读出来字符串.
	 * @return
	 */
	@Override
	public String toString() {
		int index = 0;
		byte[] type = new byte[TYPE_LENGTH];
		System.arraycopy(bytes, 0, type, 0, TYPE_LENGTH);
		index += TYPE_LENGTH;

		byte[] subLength = new byte[SUB_LENGTH];
		System.arraycopy(bytes, index, subLength, 0, SUB_LENGTH);
		index += SUB_LENGTH;

		byte[] subject = new byte[subjectLength];
		System.arraycopy(bytes, index, subject, 0, subjectLength);
		index += subjectLength;

		byte[] offset = new byte[OFFSET_LENGTH];
		System.arraycopy(bytes, index, offset, 0, OFFSET_LENGTH);

		return String.join("-", new String(type), String.valueOf(ByteUtils.toInt(subLength)), new String(subject),
				String.valueOf(ByteUtils.toInt(offset)));

	}

}
