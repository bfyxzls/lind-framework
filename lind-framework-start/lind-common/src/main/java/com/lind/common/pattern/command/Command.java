package com.lind.common.pattern.command;

/**
 * 抽象命令，可能有多个具体的命令，在统一的具体工厂中被添加，统一执行这些命令.
 */
public interface Command {

	void execute();

}
