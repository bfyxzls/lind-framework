package com.lind.common.pattern.chain.bll;

import com.lind.common.pattern.chain.ChainHandler;
import com.lind.common.pattern.chain.WorkFlow;
import com.lind.common.pattern.chain.chain.CreateService;
import com.lind.common.pattern.chain.chain.EditService;
import com.lind.common.pattern.chain.chain.ReadService;
import com.lind.common.pattern.chain.chain.RemoveService;

/**
 * 第一个责任链条.
 */
public class WorkFlow1 extends WorkFlow {

	@Override
	protected ChainHandler getChainHandler() {
		ChainHandler chainHandler = new CreateService();
		chainHandler.setNext(new EditService()).setNext(new RemoveService()).setNext(new ReadService());
		return chainHandler;
	}

}
