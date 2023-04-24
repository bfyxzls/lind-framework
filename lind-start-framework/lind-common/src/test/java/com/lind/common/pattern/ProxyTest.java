package com.lind.common.pattern;

import com.lind.common.pattern.proxy.IProject;
import com.lind.common.pattern.proxy.Project;
import com.lind.common.pattern.proxy.SubjectProxyHandler;
import java.lang.reflect.Proxy;
import org.junit.Test;

public class ProxyTest {

	@Test
	public void daynmicTest() {
		SubjectProxyHandler subjectProxyHandler = new SubjectProxyHandler(Project.class);
		IProject proxy = (IProject) Proxy.newProxyInstance(ProxyTest.class.getClassLoader(),
				new Class[] { IProject.class }, subjectProxyHandler);
		proxy.print();
	}

}
