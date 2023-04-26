package com.lind.common.pattern.factorymethod;

public class SqlFactory implements Factory {

	@Override
	public UserRepository createUserRepository() {
		return new SqlUserRepository();
	}

}
