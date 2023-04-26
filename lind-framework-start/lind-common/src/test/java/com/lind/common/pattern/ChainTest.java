package com.lind.common.pattern;

import com.lind.common.pattern.chain.HandlerParameters;
import com.lind.common.pattern.chain.WorkFlow;
import com.lind.common.pattern.chain.bll.WorkFlow1;
import com.lind.common.pattern.chain.bll.WorkFlow2;
import org.junit.Test;

/**
 * 在责任链模式里：很多对象由每一个对象对其下家的引用而连接起来形成一条链.
 */
public class ChainTest {

	@Test
	public void chainFlow1() {
		WorkFlow workFlow = new WorkFlow1(); // WorkFlow1表示一个完整的流程，管理员可以提前配置到数据库里
		workFlow.ProcessRequest(new HandlerParameters("read,edit", "test"));// commandType:read,edit表示当前用户拥有的角色，根据角色来判断执行哪些链条
	}

	@Test
	public void chainFlow2() {
		WorkFlow workFlow = new WorkFlow2();
		workFlow.ProcessRequest(new HandlerParameters("doing", "test"));
	}

}
