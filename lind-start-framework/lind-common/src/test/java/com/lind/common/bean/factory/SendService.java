package com.lind.common.bean.factory;

import com.lind.common.bean.factory.core.Send;
import com.lind.common.bean.factory.core.SendServiceProvider;

@Send
public interface SendService<User> extends SendServiceProvider<User> {

	void hello();

}
