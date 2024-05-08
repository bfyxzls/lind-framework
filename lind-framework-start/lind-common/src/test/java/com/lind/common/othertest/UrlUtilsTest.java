package com.lind.common.othertest;

import org.junit.jupiter.api.Test;

public class UrlUtilsTest {

	public static String removeUrlSpaceParams(String needValid) {
		needValid = needValid.replaceAll(" ", "%20");
		needValid = needValid.replaceAll("<", "%3C");
		needValid = needValid.replaceAll(">", "%3E");
		return needValid;
	}

	@Test
	public void testValid() {
		String url = "https://www.pkulaw.com/chl/f338b61bc48f9187bdfb.html?keyword=最高人民检察院、公安部关于印发<最高人民检察院、公安部关于公安机关管辖的刑事案件立案追诉标准的规定%20(二)>的通知";
		url = removeUrlSpaceParams(url);
		java.net.URI.create(url);
	}

}
