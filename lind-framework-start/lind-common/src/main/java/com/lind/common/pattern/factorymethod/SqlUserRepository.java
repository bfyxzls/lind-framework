package com.lind.common.pattern.factorymethod;

public class SqlUserRepository implements UserRepository {

	@Override
	public void insert(Userinfo userinfo) {
		System.out.println("SqlUserRepository.insert");
	}

	@Override
	public void del(Userinfo userinfo) {
		System.out.println("SqlUserRepository.del");

	}

}
