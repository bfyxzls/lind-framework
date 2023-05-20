package com.lind.common.lb;

import com.lind.common.lb.impl.RpcLoadBalanceConsistentHashStrategy;
import org.junit.Test;

/**
 * @author lind
 * @date 2023/5/19 10:40
 * @since 1.0.0
 */
public class HashTest {
    @Test
    public void consistentHash() {
        RpcLoadBalanceConsistentHashStrategy rpcLoadBalanceConsistentHashStrategy=new RpcLoadBalanceConsistentHashStrategy();
        rpcLoadBalanceConsistentHashStrategy.hash("zzl");
    }
}
