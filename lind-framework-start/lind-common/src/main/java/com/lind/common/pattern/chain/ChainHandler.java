package com.lind.common.pattern.chain;

/**
 * 基本链条.
 */
public abstract class ChainHandler {

	private ChainHandler next;

	public abstract void execute(HandlerParameters parameters);

	public ChainHandler getNext() {
		return next;
	}

	public ChainHandler setNext(ChainHandler next) {
		this.next = next;
		return this.next;
	}

	/**
	 * 链条的处理方法，单向链表的遍历。
	 * @param handlerParameters
	 */
	public void ProcessRequest(ChainHandler command, HandlerParameters handlerParameters) {
		if (command == null) {
			throw new IllegalArgumentException("请先使用setCommand方法去注册命令");
		}
		command.execute(handlerParameters);

		// 递归处理下一级链条
		if (command.getNext() != null) {
			ProcessRequest(command.getNext(), handlerParameters);
		}
	}

}
