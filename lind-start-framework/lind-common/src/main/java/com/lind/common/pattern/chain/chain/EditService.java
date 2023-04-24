package com.lind.common.pattern.chain.chain;

import com.lind.common.pattern.chain.ChainHandler;
import com.lind.common.pattern.chain.HandlerParameters;

public class EditService extends ChainHandler {

	@Override
	public void execute(HandlerParameters parameters) {
		if (parameters.getCommandType().contains("edit"))
			System.out.println("编辑");
	}

}
