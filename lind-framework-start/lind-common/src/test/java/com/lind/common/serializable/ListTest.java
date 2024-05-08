package com.lind.common.serializable;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lind
 * @date 2023/11/22 10:03
 * @since 1.0.0
 */
public class ListTest {

	@SneakyThrows
	@Test
	public void test() {
		List<String> list = new ArrayList<>();
		list.add("apple");
		list.add("banana");
		list.add("orange");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);

		oos.writeObject(list);
		byte[] serializedData = bos.toByteArray();
		System.out.println(serializedData);

		ByteArrayInputStream bis = new ByteArrayInputStream(serializedData);
		ObjectInputStream ois = new ObjectInputStream(bis);

		List<String> deserializedList = (List<String>) ois.readObject();
		System.out.println(deserializedList);

	}

}
