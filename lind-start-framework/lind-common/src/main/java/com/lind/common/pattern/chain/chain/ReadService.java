package com.lind.common.pattern.chain.chain;

import com.lind.common.pattern.chain.ChainHandler;
import com.lind.common.pattern.chain.HandlerParameters;

public class ReadService extends ChainHandler {

	@Override
	public void execute(HandlerParameters parameters) {
		if (parameters.getCommandType().contains("read"))
			System.out.println("读取");
	}

}
