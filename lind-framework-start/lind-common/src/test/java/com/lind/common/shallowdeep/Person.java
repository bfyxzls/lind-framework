package com.lind.common.shallowdeep;

import java.io.*;

/**
 * @author lind
 * @date 2023/9/11 14:17
 * @since 1.0.0
 */

public class Person implements Cloneable, Serializable {

	private String name;

	private Address address;

	public Person(String name, Address address) {
		this.name = name;
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	// 序列化实现深拷贝
	public Person deepCopy() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(this);

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		return (Person) objectInputStream.readObject();
	}

}
