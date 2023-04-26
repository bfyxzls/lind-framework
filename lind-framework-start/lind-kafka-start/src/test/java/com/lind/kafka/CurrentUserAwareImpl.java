package com.lind.kafka;

import com.lind.kafka.entity.CurrentUserAware;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserAwareImpl implements CurrentUserAware {

	@Override
	public String getCurrentUserName() {
		return "lind";
	}

	@Override
	public String getCurrentUserId() {
		return "1";
	}

}
