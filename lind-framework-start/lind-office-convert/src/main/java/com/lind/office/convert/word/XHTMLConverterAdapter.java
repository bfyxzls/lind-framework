package com.lind.office.convert.word;

import org.apache.poi.xwpf.converter.core.IXWPFConverter;
import org.apache.poi.xwpf.converter.core.XWPFConverterException;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.xml.sax.ContentHandler;

import java.io.IOException;

/**
 * 重写，解决mac word引起的错误
 */
public class XHTMLConverterAdapter extends XHTMLConverter {

	private static final IXWPFConverter<XHTMLOptions> INSTANCE = new XHTMLConverterAdapter();

	public static IXWPFConverter<XHTMLOptions> getInstance() {
		return INSTANCE;
	}

	@Override
	public void convert(XWPFDocument document, ContentHandler contentHandler, XHTMLOptions options)
			throws XWPFConverterException, IOException {
		try {
			options = options != null ? options : XHTMLOptions.getDefault();
			XHTMLMapperAdapter mapper = new XHTMLMapperAdapter(document, contentHandler, options);
			mapper.start();
		}
		catch (Exception var5) {
			throw new XWPFConverterException(var5);
		}
	}

}
