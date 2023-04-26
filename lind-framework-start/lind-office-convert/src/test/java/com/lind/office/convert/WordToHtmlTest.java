package com.lind.office.convert;

import com.lind.office.convert.word.WordToHtml;
import org.junit.Test;

public class WordToHtmlTest {

	@Test
	public void docxToHtml() throws Exception {
		new WordToHtml().wordToHtml("D:\\接口管理平台-DOClever使用文档v1.0.docx", "d:\\outHtml");
	}

	@Test
	public void docToHtml() throws Exception {
		new WordToHtml().wordToHtml("D:\\2.doc", "d:\\outHtml");
	}

	@Test
	public void split() throws Exception {
		String a = "http://locahost/";
		System.out.println(a.lastIndexOf("/") == a.length() - 1);
		System.out.println(a.substring(0, a.lastIndexOf("/")));
	}

}
