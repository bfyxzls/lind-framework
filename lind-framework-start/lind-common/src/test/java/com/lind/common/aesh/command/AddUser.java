package com.lind.common.aesh.command;

import org.aesh.AeshRuntimeRunner;
import org.aesh.command.Command;
import org.aesh.command.CommandDefinition;
import org.aesh.command.CommandResult;
import org.aesh.command.invocation.CommandInvocation;
import org.aesh.command.option.Option;

public class AddUser {

	private static final String COMMAND_NAME = "add-user";

	public static void main(String[] args) {
		AeshRuntimeRunner.builder().command(AddUserCommand.class).args(args).execute();
	}

	@CommandDefinition(name = COMMAND_NAME, description = "[options...]")
	public static class AddUserCommand implements Command {

		@Option(shortName = 'r', hasValue = true, description = "Name of realm to add user to")
		private String realm;

		@Option(shortName = 'u', hasValue = true, description = "Name of the user")
		private String user;

		@Override
		public CommandResult execute(CommandInvocation commandInvocation) throws InterruptedException {
			System.out.println("user=" + user);
			return CommandResult.SUCCESS;
		}

	}

}
