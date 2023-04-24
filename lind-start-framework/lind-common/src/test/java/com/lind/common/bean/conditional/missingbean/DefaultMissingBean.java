package com.lind.common.bean.conditional.missingbean;

public class DefaultMissingBean implements MissingBean {

	@Override
	public void hello() {
		System.out.println("default MissingBean");
	}

}
