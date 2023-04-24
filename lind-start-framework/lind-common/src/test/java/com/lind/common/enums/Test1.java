package com.lind.common.enums;

enum Test1 implements NameValueEnum {

	T1(1, "String类型测试1"), T2(2, "String类型测试2");

	private Integer value;

	private String name;

	Test1(Integer value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Integer getValue() {
		return this.value;
	}

}
