package com.lind.common.lb;

import java.util.TreeSet;

/**
 * @author lind
 * @date 2023/2/8 11:22
 * @since 1.0.0
 */
public class RouteTest {

	public static void main(String[] args) {
		String serviceKey = "service";
		TreeSet<String> addressSet = new TreeSet<String>() {
			{
				add("1");
				add("2");
				add("3");
				add("4");
				add("5");
			}
		};

		for (LoadBalance item : LoadBalance.values()) {
			long start = System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {
				String address = LoadBalance.LFU.xxlRpcInvokerRouter.route(serviceKey, addressSet);
				System.out.println(address);

			}
			long end = System.currentTimeMillis();
			System.out.println(item.name() + " --- " + (end - start));
		}

	}

}
