package com.lind.common.pattern.command;

import java.util.ArrayList;
import java.util.List;

/**
 * 命令调用者.
 */
public class CommandManager {

	private List<Command> commandList = new ArrayList<Command>();

	/**
	 * 将一个具体的命令加入到命令集合中.
	 * @param command
	 */
	public void add(Command command) {
		commandList.add(command);
	}

	/**
	 * 运行当前集合中的命令.
	 */
	public void run() {
		for (Command command : commandList) {
			command.execute();
		}
		commandList.clear();
	}

}