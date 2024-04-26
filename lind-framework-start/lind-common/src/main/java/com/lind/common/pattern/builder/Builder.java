package com.lind.common.pattern.builder;

// 建造者接口
public interface Builder {

	void buildPart1(String part1);

	void buildPart2(String part2);

	Product getResult();

}
