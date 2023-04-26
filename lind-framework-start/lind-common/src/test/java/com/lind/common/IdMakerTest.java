package com.lind.common;

import com.lind.common.util.IdMakerUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class IdMakerTest {

	/**
	 * 时间戳转换成日期格式字符串
	 * @param seconds 精确到秒的字符串
	 * @return
	 */
	public static String timeStamp2Date(Integer seconds, String format) {
		if (seconds == null) {
			return "";
		}
		if (format == null || format.isEmpty()) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	@Test
	public void mongodbId() throws InterruptedException {

		for (int i = 0; i < 10; i++) {
			log.info("id={}", IdMakerUtils.generateId(0));
		}

	}

	@Test
	public void hexDecConvert() {
		String hex = "ff";
		Integer dec = Integer.parseInt(hex, 16);
		log.info("dec={}", dec);

		String val = "5f46260501000001fc";

		Integer timer = Integer.parseInt(val.substring(0, 8), 16);
		String date = timeStamp2Date(timer, "yyyy-MM-dd HH:mm:ss");
		log.info("time:{}", date);

		Integer service = Integer.parseInt(val.substring(8, 10), 16);
		log.info("serviceId:{}", service);

		Integer inc = Integer.parseInt(val.substring(10), 16);
		log.info("inc:{}", inc);

	}

	@Test
	public void gid() {
		String gid = "10";
		for (int i = 0; i < 10; i++) {
			Long newId = i | Long.parseLong(gid, 16);
			log.info("newId:{}", newId);
		}

	}

	@Test
	public void bitAndOperator() {
		log.info("10 & 7={}", 10 & 7);
		log.info("3 & 7={}", 3 & 7);
		log.info("10 & 255={}", 10 & 255);
		log.info("300 & 255={}", 300 & 255); // 255的16进制为ff,后面数字为2^n-1
	}

	@Test
	public void bitOrOperator() {
		Long begin = Long.parseLong("0007000000000000", 16);
		log.info("or={}", 1 | begin);
		log.info(String.format("%16x", 1 | begin));

		begin = Long.parseLong("1000", 16);
		for (int i = 0; i < 10; i++) {
			log.info("{} | {} = {}", i, begin, i | begin);// 超过begin之后，数字会有重复
		}
	}

	@Test
	public void hashCodeString() {
		log.info("hex data1={}", String.format("%08x", "hello data1".hashCode()));
		log.info("hex data1={}", String.format("%016x", "hell9o data2".hashCode()));
		log.info("dec val={}", Long.parseLong("0007000000000000", 16));
		log.info("dec val={}", Long.parseLong("004B000000000000", 16)); // 21110623253299200
		log.info("dec max={}", Long.MAX_VALUE); // 9223372036854775807

		log.info("hex data1={}", String.format("%02x", 10));

	}

}
