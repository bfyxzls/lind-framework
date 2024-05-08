package com.lind.common.core.util;

import com.lind.common.core.dto.A;
import com.lind.common.core.dto.B;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CopyUtilsTest {

	@Test
	public void aToB() {
		A a = new A();
		a.setAge(10);
		B b = new B();
		CopyUtils.copyProperties(a, b);
		assertNotEquals("10", b.getAge());
	}

	@Test
	public void aListToBList() {
		A a = new A();
		a.setName("zzl");
		List<A> aList = new ArrayList<>();
		aList.add(a);
		List<B> bList = CopyUtils.copyListProperties(aList, B.class);
		bList.forEach(o -> {
			assertEquals("zzl", o.getName());
		});

	}

}
