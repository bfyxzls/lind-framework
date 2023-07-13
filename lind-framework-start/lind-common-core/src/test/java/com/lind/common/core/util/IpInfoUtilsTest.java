package com.lind.common.core.util;

import org.junit.Test;

public class IpInfoUtilsTest {

	@Test
	public void longToIpStr() {
		long a = IpInfoUtils.ip2long("192.168.4.25");
		System.out.println(a);
		System.out.println(IpInfoUtils.long2ip(a));
		System.out.println(IpInfoUtils.ip2long("129.168.4.1") <= a);
		System.out.println(IpInfoUtils.ip2long("192.168.4.26") >= a);

	}

}
