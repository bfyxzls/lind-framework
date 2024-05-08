package com.lind.common.core.id;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author lind
 * @date 2023/11/27 8:39
 * @since 1.0.0
 */
public class RangeGeneratorTest {

	@Test
	public void testF0() {
		RangeGenerator rangeGenerator = new RangeGenerator(0xf0L);
		System.out.println(rangeGenerator.getRangeId(5L));
		assertEquals(0xf5L, rangeGenerator.getRangeId(5L).longValue());
	}

	@Test
	public void test70000000() {
		RangeGenerator rangeGenerator = new RangeGenerator(0x70000000L);
		System.out.println(rangeGenerator.getRangeId(5L));
	}

	@Test
	public void test7f00() {
		RangeGenerator rangeGenerator = new RangeGenerator(0x7f00L);// 0x7fff-0x7f00=255,0x7fff-0x7000=4095,0xffff-0xff00=255,f=16,ff=f*f=256,fff=ff*f=4096,fff=f*f*f=4096
		System.out.println(rangeGenerator.getRangeId(5L));
	}

}
