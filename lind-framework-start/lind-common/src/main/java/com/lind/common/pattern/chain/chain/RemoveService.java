package com.lind.common.pattern.chain.chain;

import com.lind.common.pattern.chain.ChainHandler;
import com.lind.common.pattern.chain.HandlerParameters;

public class RemoveService extends ChainHandler {

	@Override
	public void execute(HandlerParameters parameters) {
		if (parameters.getCommandType().contains("del"))
			System.out.println("删除");
	}

}
