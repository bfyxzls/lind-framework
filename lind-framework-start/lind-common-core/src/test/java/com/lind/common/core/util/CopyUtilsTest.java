package com.lind.common.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CopyUtilsTest {

	@Test
	public void aToB() {
		A a = new A();
		a.setAge(10);
		B b = new B();
		CopyUtils.copyProperties(a, b);
		Assert.assertEquals("10", b.getAge());
	}

	@Test
	public void aListToBList() {
		A a = new A();
		a.setAge(10);
		List<A> aList = new ArrayList<>();
		aList.add(a);
		List<B> bList = CopyUtils.copyListProperties(aList, B.class);
		bList.forEach(o -> {
			Assert.assertEquals("10", o.getAge());
		});

	}

	@Test
	public void MapConvertType() throws JsonProcessingException {
		Map<String, Object> hashMap = new HashMap();
		hashMap.put("id", 1);
		hashMap.put("name", "lind");
		String result = new ObjectMapper().writeValueAsString(hashMap);
		System.out.println(result);
	}

	@Data
	class A {

		private int age;

	}

	@Data
	class B {

		private String age;

	}

}
