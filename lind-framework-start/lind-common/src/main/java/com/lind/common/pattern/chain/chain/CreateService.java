package com.lind.common.pattern.chain.chain;

import com.lind.common.pattern.chain.ChainHandler;
import com.lind.common.pattern.chain.HandlerParameters;

public class CreateService extends ChainHandler {

	@Override
	public void execute(HandlerParameters parameters) {
		if (parameters.getCommandType().contains("create"))
			System.out.println("建立");
	}

}
