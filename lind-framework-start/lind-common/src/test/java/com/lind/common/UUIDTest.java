package com.lind.common;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UUIDTest {

	@Test
	public void bulkUuidStringCreate() {
		for (int i = 0; i < 10; i++)
			System.out.println(UUID.randomUUID().toString());
	}

	@Test
	public void bulkUuidCreate() {
		List<UUID> uuidList = new ArrayList<>();
		for (int i = 0; i < 10; i++)
			uuidList.add(UUID.randomUUID());
		for (UUID uuid : uuidList) {
			System.out.println(uuid.toString());
		}
	}

	@Test
	public void fromString() {
		// randomUUID方法
		String firstUuid = UUID.randomUUID().toString();
		System.out.println("firstUuid：" + firstUuid);
		// fromString方法
		String secondUuid = "0c312388-5d09-4f44-b670-5461605f0b1e";

		// 字符串转化为UUID类
		UUID uuid1 = UUID.fromString(firstUuid);
		// 字符串转化为UUID类
		UUID uuid2 = UUID.fromString(secondUuid);

		int isEqules = uuid1.compareTo(uuid2);

		System.out.println("uuid1 和 uuid是否相等:" + isEqules);
	}

}
