package com.lind.common.jgroup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lind
 * @date 2023/6/8 11:11
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JGroupsExample.class })
public class SendTest {

	@Autowired
	JGroupsExample jGroupsExample;

	@Test
	public void hello() throws Exception {
		jGroupsExample.sendMessage("lind,hello world.");
		Thread.sleep(1000 * 60);
	}

}
