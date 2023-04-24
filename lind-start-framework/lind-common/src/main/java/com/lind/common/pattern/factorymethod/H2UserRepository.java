package com.lind.common.pattern.factorymethod;

public class H2UserRepository implements UserRepository {

	@Override
	public void insert(Userinfo userinfo) {
		System.out.println("H2UserRepository.insert");

	}

	@Override
	public void del(Userinfo userinfo) {
		System.out.println("H2UserRepository.del");

	}

}
