package com.lind.common.core.util;

import lombok.Builder;
import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;

/**
 * @author lind
 * @date 2023/7/13 16:43
 * @since 1.0.0
 */
public class ObjectByteUtilsTest {

	@Test
	public void convert() {
		byte[] buffer = ObjectByteUtils.toByteArray(Info.builder().title("lind").build());
		Info info = (Info) ObjectByteUtils.toObject(buffer);
		System.out.println(info.title);
	}

	@Test
	public void serializationUtils() {
		byte[] buffer = SerializationUtils.serialize(Info.builder().title("lind").build());
		Info info = (Info) SerializationUtils.deserialize(buffer);
		System.out.println(info.title);
	}

	@Builder
	static class Info implements Serializable {

		private String title;

	}

}
