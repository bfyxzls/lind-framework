package com.lind.common.core.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author lind
 * @date 2023/2/27 16:27
 * @since 1.0.0
 */
@Slf4j
public class ClassUtilsTest {

	@Test
	public void getFiledDescByType() {
		assertEquals("zzl", ClassUtils.getFieldValueByName("name", new DateRangeDTO("zzl")));
	}

	@Data
	@AllArgsConstructor
	class DateRangeDTO {

		private String name;

	}

}
