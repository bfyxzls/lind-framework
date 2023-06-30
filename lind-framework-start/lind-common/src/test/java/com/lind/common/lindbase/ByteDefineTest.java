package com.lind.common.lindbase;

import com.lind.common.minibase.Bytes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

/**
 * @author lind
 * @date 2023/6/20 11:24
 * @since 1.0.0
 */
@Slf4j
public class ByteDefineTest {

	static final int HEADER_LEN = 4;

	String headerString;

	/**
	 * 一个int32的数字使用byte数组来表示，数组大小为4.
	 */
	@Test
	public void fromIntToBytes() {
		// 256 = 0 0 1 0
		// 255 = 0 0 0 -1
		// 128 = 0 0 0 -128
		// 127 = 0 0 0 127
		// byte有符号，-128~127
		log.info("{}", Bytes.toBytes(256));// [0, 0, 1, 0]
		log.info("{}", Bytes.toBytes(255));// [0, 0, 0, -1]
		log.info("{}", Bytes.toBytes(128));// [0, 0, 0, -128]
		log.info("{}", Bytes.toBytes(129));// [0, 0, 0, -127]
		log.info("{}", Bytes.toBytes(-1));// [-1, -1, -1, -1]
		log.info("{}", Bytes.toBytes(127));// [0, 0, 0, 127]
	}

	@Test
	public void objectToByte() throws IOException {

		// 从对象序列化成byte[]
		headerString = "zhang张";
		Integer len = Bytes.toBytes(headerString).length;
		byte[] result = new byte[HEADER_LEN + len];

		int pos = 0;
		// Encode last kv
		byte[] offsetBytes = Bytes.toBytes(len);
		System.arraycopy(offsetBytes, 0, result, pos, HEADER_LEN);
		pos += HEADER_LEN;

		byte[] kvBytes = Bytes.toBytes(headerString);
		System.arraycopy(kvBytes, 0, result, pos, kvBytes.length);
		pos += kvBytes.length;

		log.info("result={},{}", result, pos);

		// 从byte[]中解析成对象
		pos = 0;
		// Decode block blockOffset
		Integer nameLen = Bytes.toInt(Bytes.slice(result, pos, HEADER_LEN));
		pos += HEADER_LEN;
		String nameStr = new String(Bytes.slice(result, pos, nameLen));
		pos += nameLen;
		EventData eventData = new EventData(nameLen, nameStr);
		log.info("event data:{}", eventData);
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	class EventData {

		int len;

		String value;

	}

}
