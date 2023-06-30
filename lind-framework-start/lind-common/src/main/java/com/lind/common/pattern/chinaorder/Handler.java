package com.lind.common.pattern.chinaorder;

/**
 * @author lind
 * @date 2023/6/27 16:14
 * @since 1.0.0
 */
public interface Handler {

	void handleRequest(Order order);

	void setNextHandler(Handler nextHandler);

}
