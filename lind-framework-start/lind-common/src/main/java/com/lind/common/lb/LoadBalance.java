package com.lind.common.lb;

import com.lind.common.lb.impl.RpcLoadBalanceConsistentHashStrategy;
import com.lind.common.lb.impl.RpcLoadBalanceLFUStrategy;
import com.lind.common.lb.impl.RpcLoadBalanceLRUStrategy;
import com.lind.common.lb.impl.RpcLoadBalanceRandomStrategy;
import com.lind.common.lb.impl.RpcLoadBalanceRoundStrategy;

/**
 * @author xuxueli 2018-12-04
 */
public enum LoadBalance {

	RANDOM(new RpcLoadBalanceRandomStrategy()), ROUND(new RpcLoadBalanceRoundStrategy()),
	LRU(new RpcLoadBalanceLRUStrategy()), LFU(new RpcLoadBalanceLFUStrategy()),
	CONSISTENT_HASH(new RpcLoadBalanceConsistentHashStrategy());

	public final RpcLoadBalance xxlRpcInvokerRouter;

	private LoadBalance(RpcLoadBalance xxlRpcInvokerRouter) {
		this.xxlRpcInvokerRouter = xxlRpcInvokerRouter;
	}

	public static LoadBalance match(String name, LoadBalance defaultRouter) {
		for (LoadBalance item : LoadBalance.values()) {
			if (item.name().equals(name)) {
				return item;
			}
		}
		return defaultRouter;
	}

	/*
	 * public static void main(String[] args) { String serviceKey = "service";
	 * TreeSet<String> addressSet = new TreeSet<String>(){{ add("1"); add("2"); add("3");
	 * add("4"); add("5"); }};
	 *
	 * for (LoadBalance item : LoadBalance.values()) { long start =
	 * System.currentTimeMillis(); for (int i = 0; i < 100000; i++) { String address =
	 * LoadBalance.LFU.xxlRpcInvokerRouter.route(serviceKey, addressSet);
	 * //System.out.println(address);; } long end = System.currentTimeMillis();
	 * System.out.println(item.name() + " --- " + (end-start)); }
	 *
	 * }
	 */

}
