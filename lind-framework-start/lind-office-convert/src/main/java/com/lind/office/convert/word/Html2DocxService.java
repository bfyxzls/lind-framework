package com.lind.office.convert.word;

import org.apache.commons.io.FileUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporter;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.AltChunkType;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.wml.RFonts;
import org.jsoup.Jsoup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * docx4j操作.
 */
public class Html2DocxService {

	public static String closeHTML(String str) {
		List arrTags = new ArrayList();
		arrTags.add("br");
		arrTags.add("hr");
		arrTags.add("img");
		arrTags.add("input");
		for (int i = 0; i < arrTags.size(); i++) {
			for (int j = 0; j < str.length();) {
				int tagStart = str.indexOf("<" + arrTags.get(i), j);
				if (tagStart >= 0) {
					int tagEnd = str.indexOf(">", tagStart);
					j = tagEnd;
					String preCloseTag = str.substring(tagEnd - 1, tagEnd);
					if (!"/".equals(preCloseTag)) {
						String preStr = str.substring(0, tagEnd);
						String afterStr = str.substring(tagEnd);
						str = preStr + "/" + afterStr;
					}
				}
				else {
					break;
				}
			}
		}
		return str;
	}

	public static void convert(File inputFile, File outputFile) throws Exception {

		String stringFromFile = FileUtils.readFileToString(inputFile, "UTF-8");

		String unescaped = stringFromFile;
		unescaped = Jsoup.parse(unescaped).html();

		unescaped = unescaped.replace("&nbsp;", "\u00A0");
		unescaped = closeHTML(unescaped);

		System.out.println("Unescaped: " + unescaped);

		// Setup font mapping
		RFonts rfonts = Context.getWmlObjectFactory().createRFonts();
		rfonts.setAscii("Century Gothic");
		XHTMLImporterImpl.addFontMapping("Century Gothic", rfonts);

		// Create an empty docx package
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();

		NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
		wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
		ndp.unmarshalDefaultNumbering();

		// Convert the XHTML, and add it into the empty docx we made
		XHTMLImporter XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
		XHTMLImporter.setHyperlinkStyle("Hyperlink");

		List<Object> convert = XHTMLImporter.convert(unescaped, null);
		wordMLPackage.getMainDocumentPart().addAltChunk(AltChunkType.Xhtml, unescaped.getBytes());

		wordMLPackage.save(outputFile);
	}

}
