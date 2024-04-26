package com.lind.common.pattern.builder;

// 具体建造者实现类
public class ConcreteBuilder implements Builder {

	private Product product = new Product();

	@Override
	public void buildPart1(String part1) {
		product.setPart1(part1);
	}

	@Override
	public void buildPart2(String part2) {
		product.setPart2(part2);
	}

	@Override
	public Product getResult() {
		return product;
	}

}
