package com.lind.common.pattern.chain;

/**
 * 责任链流程处理者.
 */
public abstract class WorkFlow {

	private ChainHandler command;

	public WorkFlow() {
		this.command = getChainHandler();
	}

	protected abstract ChainHandler getChainHandler();

	/**
	 * 链条的处理方法，单向链表的遍历。
	 * @param handlerParameters
	 */
	public void ProcessRequest(HandlerParameters handlerParameters) {
		if (command == null) {
			throw new IllegalArgumentException("请先使用setCommand方法去注册命令");
		}
		command.execute(handlerParameters);

		// 递归处理下一级链条
		if (command.getNext() != null) {
			command = command.getNext();
			ProcessRequest(handlerParameters);
		}
	}

}
