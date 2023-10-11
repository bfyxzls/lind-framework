package com.lind.common.othertest;

import com.lind.common.bean.family.Son;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2022/8/18 11:41
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class AnnoTest {

	@Test
	public void classAnno() {
		AutoConfigureAfter component = AnnotationUtils.findAnnotation(Son.class, AutoConfigureAfter.class);
	}

	@Test
	public void methodAnno() {

	}

}
