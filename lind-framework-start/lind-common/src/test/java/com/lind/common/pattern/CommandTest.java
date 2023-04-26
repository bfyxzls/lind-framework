package com.lind.common.pattern;

import com.lind.common.pattern.command.*;
import com.lind.common.pattern.command.bll.BuyStock;
import com.lind.common.pattern.command.bll.ClearStock;
import com.lind.common.pattern.command.bll.SellStock;
import org.junit.Test;

public class CommandTest {

	@Test
	public void commandOrder() {
		Stock abcStock = new Stock();

		BuyStock buyStockOrder = new BuyStock(abcStock);
		SellStock sellStockOrder = new SellStock(abcStock);
		ClearStock clearStock = new ClearStock(abcStock);

		CommandManager commandManager = new CommandManager();
		commandManager.add(buyStockOrder);
		commandManager.add(sellStockOrder);
		commandManager.add(clearStock);

		commandManager.run();
	}

}
