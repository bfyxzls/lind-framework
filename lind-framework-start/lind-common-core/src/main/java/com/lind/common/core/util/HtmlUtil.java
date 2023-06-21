package com.lind.common.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从类到html渲染相关.
 */
public class HtmlUtil {

	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式

	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式

	private static final String regEx_html = "<([^\u4e00-\u9fa5])+?>"; // 定义HTML标签的正则表达式

	private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符

	public static String delTagA(String htmlStr) {
		if (StringUtils.isBlank(htmlStr)) {
			return "";
		}
		String result = htmlStr.replaceAll("<a.*?>", "").replaceAll("</a>", "");
		return result;
	}

	/**
	 * @param htmlStr
	 * @return 删除Html标签
	 */
	public static String delHTMLTag(String htmlStr) {
		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		// Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		// Matcher m_space = p_space.matcher(htmlStr);
		// htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
		return htmlStr.trim(); // 返回文本字符串
	}

	public static String getTextFromHtml(String htmlStr) {
		htmlStr = delHTMLTag(htmlStr);
		htmlStr = htmlStr.replaceAll("&nbsp;", "");
		// htmlStr = htmlStr.substring(0, htmlStr.indexOf("。")+1);
		return htmlStr;
	}

	/**
	 * <p>
	 * Escapes the value for a HTML element attribute.
	 * </p>
	 * @param value
	 * @return
	 */
	public static String escapeAttribute(String value) {
		StringBuilder escaped = new StringBuilder();

		for (int i = 0; i < value.length(); i++) {
			char chr = value.charAt(i);

			if (chr == '<') {
				escaped.append("&lt;");
			}
			else if (chr == '>') {
				escaped.append("&gt;");
			}
			else if (chr == '"') {
				escaped.append("&quot;");
			}
			else if (chr == '\'') {
				escaped.append("&apos;");
			}
			else if (chr == '&') {
				escaped.append("&amp;");
			}
			else {
				escaped.append(chr);
			}
		}

		return escaped.toString();
	}

}
