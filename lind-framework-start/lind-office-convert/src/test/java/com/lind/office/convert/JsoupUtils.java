package com.lind.office.convert;

import org.jsoup.Jsoup;
import org.junit.Test;

public class JsoupUtils {

	@Test
	public void removeHtml() {

		String dirtyHTML = "<p><img alt=\"\" src=\"http://back.chinalawinfo.com/upFiles/images/7(16).png\" data-bd-imgshare-binded=\"1\" width=\"549\" height=\"299\">";
		String cleanHTML = Jsoup.parse(dirtyHTML).html();
		System.out.println(cleanHTML);
	}

	@Test
	public void replenish() {
		String xml = "<html><body><b>测试<span>dsafasdfa</body></html>";
		String html = Jsoup.parse(xml).body().html();
		System.out.println(html);
	}

	@Test
	public void replenish2() {
		String xml = "<table border=\"0\" align=\"center\" width=\"100%\">\n" + "<tr><td class=\"header\">A\n"
				+ "<td class=\"header\">B\n" + "<td class=\"header\">C\n" + "</tr>\n" + "</table>";
		String html = Jsoup.parse(xml).body().html();
		System.out.println(html);
	}

}
