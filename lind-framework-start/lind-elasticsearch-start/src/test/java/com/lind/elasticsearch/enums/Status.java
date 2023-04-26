package com.lind.elasticsearch.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status implements BaseEnum {

	INIT(0, "初始化"), NORMAL(1, "正常");

	private final Integer value;

	private final String description;

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
