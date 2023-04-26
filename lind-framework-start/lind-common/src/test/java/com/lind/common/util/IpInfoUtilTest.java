package com.lind.common.util;

import com.lind.common.util.IpInfoUtil;
import org.junit.Test;

public class IpInfoUtilTest {

	@Test
	public void longToIpStr() {
		long a = IpInfoUtil.ip2long("192.168.4.25");
		System.out.println(a);
		System.out.println(IpInfoUtil.long2ip(a));
		System.out.println(IpInfoUtil.ip2long("129.168.4.1") <= a);
		System.out.println(IpInfoUtil.ip2long("192.168.4.26") >= a);

	}

}
