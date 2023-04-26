package com.lind.logger.service.impl;

import com.lind.logger.service.CurrentUserAware;

public class DefaultCurrentUserAware implements CurrentUserAware {

	@Override
	public String username() {
		return "system";
	}

}
