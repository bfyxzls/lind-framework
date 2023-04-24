package com.lind.common.util;

import com.lind.common.dto.DateRangeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author lind
 * @date 2023/2/27 16:27
 * @since 1.0.0
 */
@Slf4j
public class ClassUtilsTest {

	@Test
	public void getFiledDescByType() {
		ClassUtils.getFiledDescByType(DateRangeDTO.class).forEach(o -> {
			log.info(o);
		});
	}

	@Test
	public void getFiledNameByType() {
		ClassUtils.getFiledNameByType(DateRangeDTO.class).forEach(o -> {
			log.info(o);
			log.info("desc:{}", ClassUtils.getFieldDescByName(o, DateRangeDTO.class));
		});
	}

}
