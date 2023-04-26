package com.lind.office.convert.word;

import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.converter.xhtml.internal.XHTMLMapper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFNum;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr;
import org.xml.sax.ContentHandler;

public class XHTMLMapperAdapter extends XHTMLMapper {

	public XHTMLMapperAdapter(XWPFDocument document, ContentHandler contentHandler, XHTMLOptions options)
			throws Exception {
		super(document, contentHandler, options);
	}

	@Override
	protected XWPFNum getXWPFNum(CTNumPr numPr) {
		CTDecimalNumber numID = numPr.getNumId();
		if (numID == null) {
			// numID can be null, ignore the numbering
			// see https://code.google.com/p/xdocreport/issues/detail?id=239
			return null;
		}
		if (document.getNumbering() != null) {
			XWPFNum num = document.getNumbering().getNum(numID.getVal());
			return num;
		}
		return null;
	}

}
