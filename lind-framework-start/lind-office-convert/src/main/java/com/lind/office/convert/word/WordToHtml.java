package com.lind.office.convert.word;

import lombok.SneakyThrows;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * word转html辅助类，适合于http环境.
 */
@Component
public class WordToHtml {

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	/**
	 * 图像服务器域名.
	 */
	@Value("${imageHost:''}")
	private String imageHost;

	/**
	 * doc to html
	 * @param sourceFileName
	 * @param imagePathStr
	 * @param imagePathResolver
	 * @param targetFileName
	 * @return
	 * @throws Exception
	 */
	public String docToHtml(String sourceFileName, String imagePathStr, String imagePathResolver, String targetFileName)
			throws Exception {

		HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(sourceFileName));
		org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
		// 保存图片，并返回图片的相对路径
		wordToHtmlConverter.setPicturesManager((content, pictureType, name, width, height) -> {
			try (FileOutputStream out = new FileOutputStream(imagePathStr + "/" + name)) {
				out.write(content);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			if (!StringUtils.isEmpty(imageHost)) {
				// 去除末尾的/
				if (imageHost.lastIndexOf("/") == imageHost.length() - 1) {
					imageHost = imageHost.substring(0, imageHost.lastIndexOf("/"));
				}
				return imageHost + imagePathResolver + "/" + name;
			}
			return imagePathResolver;
		});
		wordToHtmlConverter.processDocument(wordDocument);
		org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(new File(targetFileName));
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);
		return targetFileName;
	}

	/**
	 * docx to html
	 * @param sourceFileName
	 * @param imagePathStr
	 * @param imagePathResolver
	 * @param targetFileName
	 * @return
	 * @throws Exception
	 */
	public String docxToHtml(String sourceFileName, String imagePathStr, String imagePathResolver,
			String targetFileName) throws Exception {

		OutputStreamWriter outputStreamWriter = null;
		try {
			XWPFDocument document = new XWPFDocument(new FileInputStream(sourceFileName));
			XHTMLOptions options = XHTMLOptions.create();
			// 存放图片的文件夹绝对路径
			options.setExtractor(new FileImageExtractor(new File(imagePathStr)));
			// html中图片的相对路径
			if (!StringUtils.isEmpty(imageHost)) {
				// 去除末尾的/
				if (imageHost.lastIndexOf("/") == imageHost.length() - 1) {
					imageHost = imageHost.substring(0, imageHost.lastIndexOf("/"));
				}
				imagePathResolver = imageHost + imagePathResolver;
			}
			options.URIResolver(new BasicURIResolver(imagePathResolver));
			outputStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFileName), "utf-8");
			XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverterAdapter.getInstance();
			xhtmlConverter.convert(document, outputStreamWriter, options);
		}
		finally {
			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}
		}
		return targetFileName;
	}

	@SneakyThrows
	public String wordToHtml(String docPath, String outDir) {
		String fileNameExt = docPath;
		if (docPath.lastIndexOf("/") > 0) {
			fileNameExt = docPath.substring(docPath.lastIndexOf("/") + 1);
		}
		if (docPath.lastIndexOf("\\") > 0) {
			fileNameExt = docPath.substring(docPath.lastIndexOf("\\") + 1);
		}
		String fileName = fileNameExt.substring(0, fileNameExt.lastIndexOf("."));
		String day = LocalDate.now().format(DATE_FORMATTER);
		String imageUrlPath = "/" + day + "/" + fileName;
		String imagePathStr = Paths.get(outDir, day, fileName).toString();
		String targetFileName = Paths.get(outDir, day, fileName + ".html").toString();
		File file = new File(imagePathStr);
		if (!file.exists()) {
			file.mkdirs();
		}
		switch (docPath.substring(docPath.lastIndexOf(".")).toLowerCase()) {
			case ".doc":
				return docToHtml(docPath, imagePathStr, imageUrlPath, targetFileName);
			case ".docx":
				return docxToHtml(docPath, imagePathStr, imageUrlPath, targetFileName);
			default:
				throw new IllegalArgumentException("不支持的扩展名");
		}
	}

}
