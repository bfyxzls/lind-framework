package com.lind.office.convert;

import com.lind.office.convert.word.Html2DocxService;
import com.lind.office.convert.word.HtmlToWord;
import com.lind.office.convert.word.RemoteHtmlToWord;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;

public class HtmlToWordTest {

	/**
	 * 写成word文件
	 */
	@Test
	public void writeWordFile() {
		HtmlToWord.writeWordFile("<Html><body>hello world</body></html>", "d:\\outDoc");
	}

	@SneakyThrows
	@Test
	public void writeWordFileFromHtml() {
		HtmlToWord.writeWordFile(ResourceUtils.getFile("classpath:demo.html"), "d:\\outDoc");
	}

	@SneakyThrows
	@Test
	public void remoteHtmlToWord() {
		new RemoteHtmlToWord().excute(" https://www.demo.com/qikan/3fb3d1086a5a3708d6e81133085c4eecbdfb.html");
	}

	@SneakyThrows
	@Test
	public void doc4j() {
		Html2DocxService service = new Html2DocxService();
		service.convert(ResourceUtils.getFile("classpath:demo.html"), new File("d:\\outDoc\\1.docx"));
	}

	@SneakyThrows
	@Test
	public void doc4jBigContent() {
		Html2DocxService.convert(ResourceUtils.getFile("classpath:demo.html"), new File("d:\\outDoc\\bigContent.docx"));
	}

	@SneakyThrows
	@Test
	public void doc4jImage() {
		Html2DocxService.convert(ResourceUtils.getFile("classpath:image-demo.html"),
				new File("d:\\outDoc\\image.docx"));
	}

}
