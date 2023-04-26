package com.lind.logger;

import com.lind.logger.anno.LogRecord;
import org.springframework.stereotype.Component;

@Component
public class UserService {

	@LogRecord(detail = "修改了订单的配送员:${#user.getName()}", dataId = "1", operateType = 1, moduleType = 1)
	public void insert(User user) {
		System.out.println("insert user");
	}

	public void update(User user) {
		System.out.println("update user");
	}

}
