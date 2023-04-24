package com.lind.common.pattern.chain;

/**
 * 链条处理的参数.
 */
public class HandlerParameters {

	private String CommandType;

	private String data;

	public HandlerParameters(String commandType, String data) {
		CommandType = commandType;
		this.data = data;
	}

	public String getCommandType() {
		return CommandType;
	}

	public String getData() {
		return data;
	}

}
