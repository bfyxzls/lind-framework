package com.lind.common;

import cn.hutool.core.date.DateTime;
import com.lind.common.encrypt.HashUtils;
import com.lind.common.tree.Trie;
import com.lind.common.util.BinHexSwitchUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StringTest {

	public static boolean ipExistsInRange(String ip, String ipSection) {
		try {
			ipSection = ipSection.trim();
			String beginIP, endIP;
			ip = ip.trim();
			int idx = ipSection.indexOf('~');
			if (idx == -1) {
				beginIP = ip;
				endIP = ip;
			}
			else {
				beginIP = ipSection.substring(0, idx);
				if (ipSection.length() == idx + 1) {
					endIP = beginIP;
				}
				else {
					endIP = ipSection.substring(idx + 1);
				}
			}
			return getIp2long(beginIP) <= getIp2long(ip) && getIp2long(ip) <= getIp2long(endIP);
		}
		catch (Exception ex) {
			System.err.println("IpUtils.error,ip:" + ip + ",range:" + ipSection + ",error:" + ex.getMessage());
			return false;
		}
	}

	static long getIp2long(String ip) {
		ip = ip.trim();
		String[] ips = ip.split("\\.");
		long ip2long = 0L;
		for (int i = 0; i < 4; ++i) {
			ip2long = ip2long << 8 | Integer.parseInt(ips[i]);
		}
		return ip2long;
	}

	@Test
	public void split() {
		String msg = "admin|中国";
		log.info(StringUtils.split(msg, "|")[1]);
	}

	@Test
	public void StringTo16() {
		for (int i = 1; i < 10000; i++) {
			String str = "abc";
			String str16 = BinHexSwitchUtils.bytesToHexString(str.getBytes());
			log.info("result={}", str16);
			log.info("result={}", new String(BinHexSwitchUtils.hexStringToBytes(str16)));
		}
	}

	/**
	 * 去掉http协议头 resource=www.sina.com/auth/bobo
	 */
	@Test
	public void substringHttp() {
		String resourceUrl = "http://www.sina.com/auth/bobo";
		if (resourceUrl.indexOf("://") > -1) {
			resourceUrl = resourceUrl.substring(resourceUrl.indexOf("://") + 3);
		}
		System.out.println("resource=" + resourceUrl);
	}

	@Test
	public void substringHttpHost() {
		String resourceUrl = "http://www.sina.com/auth/bobo";
		URI uri = URI.create(resourceUrl);
		System.out.println(uri.getPath());
	}

	@Test
	public void dateToString() {
		String fileName = "./DataCollectionErrorFileEventHandler_" + DateTime.now().toString("yyyyMMdd") + ".log";
		System.out.println(fileName);
	}

	@Test
	public void equals() {
		// 作为对象使用
		String objectString1 = new String("java");
		String objectString2 = new String("java");
		System.out.println(objectString1 == objectString2); // false
		System.out.println(objectString1.equals(objectString2)); // true

		// 作为基本类型使用
		String valueString1 = "java";
		String valueString2 = "java";
		System.out.println(valueString1 == valueString2); // true
		System.out.println(valueString1.equals(valueString2)); // true

		String concat1 = "hello".concat("world");
		String concat2 = "helloworld";
		System.out.println(concat1 == concat2); // false
		System.out.println(concat1.equals(concat2)); // true

		String msg1 = "hello world";
		String msg2 = "hello" + " world";
		System.out.println(msg1 == msg2); // true
		System.out.println(msg1.equals(msg2)); // true

		String substr1 = "hello";
		String substr2 = "hello world".substring(0, 5);
		System.out.println(substr1 == substr2); // false
		System.out.println(substr1.equals(substr2)); // true
	}

	@Test
	public void substring() {
		String sSublist = "0401";
		String newS = sSublist.substring(0, 2);
		log.info("sSublist:{} newS:{} equal:{}", sSublist, newS, newS == "04");
		log.info("sSublist:{} newS:{} equal:{}", sSublist, newS, newS.equals("04"));

	}

	@Test
	public void ipStringSub() {
		ipExistsInRange("192.168.1.1", "058.168.1.2");
		ipExistsInRange("192.168.1.1", "058.220.147.161~058.220.147.190");
		ipExistsInRange("192.168.1.1", "192.168.1.1~");
	}

	@Test
	public void md5Test() {
		log.info(HashUtils.md5("1970324841128405bdyh"));
	}

	@Test
	public void intToStr() {
		System.out.println(Integer.parseInt("058"));
		System.out.println(Integer.valueOf("058"));

		String str = "000000001234034120";
		String newStr = str.replaceAll("^(0+)", "");
		System.out.println(newStr);

	}

	@Test
	public void splitIp() {
		String ipStr = "111.023.044.193#111.023.044.194$;111.023.044.208#111.023.044.211$;113.240.234.161#113.240.234.166$;113.240.234.001#113.240.234.062$;113.240.246.162#113.240.246.166$;202.197.112.001#202.197.127.254$;210.043.046.009#210.043.046.010$;210.043.047.029#210.043.047.030$;211.142.219.049#211.142.219.054$;211.067.232.001#211.067.239.254$;222.240.162.129#222.240.162.158$;058.020.050.129#058.020.050.129$;058.020.050.214#058.020.050.233$;058.020.053.001#058.020.053.031$;218.077.045.128#218.077.045.207$;110.053.183.066#110.053.183.090$;183.215.137.110#183.215.137.110$;119.039.127.130#119.039.127.158$;183.215.139.082#183.215.139.086$;222.240.097.241#222.240.097.245$;220.168.016.210#220.168.016.214$;222.240.097.079#222.240.097.094$;058.020.053.197#058.020.053.254$;183.215.178.088#183.215.178.091$";
		String[] duan1Arr = ipStr.split(";");
		List<String> ipAddress = new ArrayList<>();
		for (String duan1 : duan1Arr) {
			String[] duan2Arr = duan1.split("#");
			if (duan2Arr.length > 1) {
				String end = duan2Arr[1];
				if (end.endsWith("$")) {
					end = end.substring(0, end.lastIndexOf("$"));
				}
				ipAddress.add(duan2Arr[0] + "~" + end);
			}
			else {
			}
		}
		log.info("{}", ipAddress);
	}

	@Test
	public void search() {
		String word="hello";
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			log.info("ch={}",ch);
		}
	}

}
