package com.lind.common.pattern.command.bll;

import com.lind.common.pattern.command.Command;
import com.lind.common.pattern.command.Stock;

/**
 * 具体命令.
 */
public class SellStock implements Command {

	private Stock abcStock;

	public SellStock(Stock abcStock) {
		this.abcStock = abcStock;
	}

	public void execute() {
		abcStock.sell();
	}

}
