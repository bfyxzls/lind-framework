package com.lind.common.lb;

import com.lind.common.lb.impl.RpcLoadBalanceConsistentHashStrategy;
import org.junit.Test;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * @author lind
 * @date 2023/5/19 10:40
 * @since 1.0.0
 */
public class HashTest {

	@Test
	public void consistentHash() {
		RpcLoadBalanceConsistentHashStrategy rpcLoadBalanceConsistentHashStrategy = new RpcLoadBalanceConsistentHashStrategy();

		TreeSet<String> treeSet0 = new TreeSet<String>() {
			{
				add("192.168.1.2");
				add("192.168.1.3");
				add("192.168.1.4");
				add("192.168.1.5");
				add("192.168.1.6");
			}
		};
		TreeSet<String> treeSet = new TreeSet<>(Arrays.asList("192.168.1.2", "192.168.1.3", "192.168.1.4"));
		rpcLoadBalanceConsistentHashStrategy.doRoute("zzl", treeSet);

	}

}
