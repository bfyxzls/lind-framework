package com.lind.common.pattern.chinaorder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 折扣工厂.
 *
 * @author lind
 * @date 2023/6/28 9:22
 * @since 1.0.0
 */
public class HandlerFactory {

	public static Handler handlerFactory() {
		List<HandlerModel> handlerModels = new ArrayList<>();
		handlerModels
				.add(new HandlerModel("CouponHandler", "com.lind.common.pattern.chinaorder.handler.CouponHandler", 1));
		handlerModels.add(
				new HandlerModel("DiscountHandler", "com.lind.common.pattern.chinaorder.handler.DiscountHandler", 2));
		handlerModels.add(
				new HandlerModel("BigGiftHandler", "com.lind.common.pattern.chinaorder.handler.BigGiftHandler", 3));
		handlerModels.add(new HandlerModel("VipHandler", "com.lind.common.pattern.chinaorder.handler.VipHandler", 4));
		return createHandler(handlerModels);
	}

	private static Handler createHandler(List<HandlerModel> handlerModels) {
		Handler handler = null;
		Handler previousHandler = null;

		for (HandlerModel handlerModel : handlerModels.stream().sorted().collect(Collectors.toList())) {
			try {
				Handler currentHandler = (Handler) Class.forName(handlerModel.getClassPath()).newInstance();
				if (previousHandler != null) {
					previousHandler.setNextHandler(currentHandler);
				}
				else {
					handler = currentHandler;
				}
				previousHandler = currentHandler;
			}
			catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}

		return handler;
	}

}
