package com.lind.common.core.util;

import com.lind.common.core.dto.A;
import com.lind.common.core.dto.B;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CopyUtilsTest {

	@Test
	public void aToB() {
		A a = new A();
		a.setAge(10);
		B b = new B();
		CopyUtils.copyProperties(a, b);
		Assert.assertNotEquals("10", b.getAge());
	}

	@Test
	public void aListToBList() {
		A a = new A();
		a.setName("zzl");
		List<A> aList = new ArrayList<>();
		aList.add(a);
		List<B> bList = CopyUtils.copyListProperties(aList, B.class);
		bList.forEach(o -> {
			Assert.assertEquals("zzl", o.getName());
		});

	}

}
