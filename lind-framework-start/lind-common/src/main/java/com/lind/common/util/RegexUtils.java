package com.lind.common.util;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 常用正则封装.
 */
public class RegexUtils {

	/**
	 * 密码长度不少于8位且至少包含大写字母、小写字母、数字和特殊符号中的四种
	 */
	public static final String password1 = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";

	/**
	 * 密码长度8-20位且至少包含大写字母、小写字母、数字或特殊符号中的任意三种，排序组合算法
	 */
	public static final String password2 = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,20}$";

	/**
	 * 密码长度8-20位且至少包含大写字母、小写字母、数字或特殊符号中的任意两种，排序组合算法
	 */
	public static final String password3 = "^(?![a-z]+$)(?![0-9]+$)(?![\\W_]+$)(?![A-Z]+$)[a-zA-Z0-9\\W_]{8,20}$";

	/**
	 * 邮件email
	 */
	public static String email = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";

	/**
	 * 隐藏出生日期、职位、年龄、工作、民族等信息.
	 */
	public static String hideOtherText(String myText) {
		String rtnText = myText;
		rtnText = StringUtils.replacePattern(rtnText,
				"([，。,；;,][\\d一二三四五六七八九零０１２３４５６７８９]+年[\\d一二三四五六七八九零０１２３４５６７８９]+月[\\d一二三四五六七八九零０１２３４５６７８９]+日出?生)([，。,；;,])",
				"$1▲$2");
		rtnText = StringUtils.replacePattern(rtnText, "([，。,；;,])(家?住[^房院])", "$1▲$2");
		rtnText = StringUtils.replacePattern(rtnText, "([，。,；;,])((联系|注册)地)", "$1▲$2");
		rtnText = StringUtils.replacePattern(rtnText, "([，。,；;,])([男女][，。,；;,])", "$1▲$2");
		String rplText = "";
		rtnText = StringUtils.replacePattern(rtnText, "([，。,；;,][^，。,；;,▲]*▲[^。]*)", rplText);
		rtnText = rtnText.replaceAll("▲", "");
		return rtnText;
	}

	/**
	 * 替换为手机识别的HTML，去掉样式及属性，保留回车.
	 * @param html html格式的手机号
	 * @return 手机号
	 */
	public static String replaceMobileHtml(String html) {
		if (html == null) {
			return "";
		}
		return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
	}

	/**
	 * 替换掉HTML标签方法.
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)) {
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 缩略字符串（不区分中英文字符）.
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return 结果
	 */
	public static String abbreviate(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				}
				else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 是否包含大写字母.
	 * @return
	 */
	public static boolean isContainUpper(String str) {
		String regex = ".*[A-Z]+.*";
		Matcher m = Pattern.compile(regex).matcher(str);
		return m.matches();
	}

	/**
	 * 是否包含小写字母.
	 * @return
	 */
	public static boolean isContainLower(String str) {
		String regex = ".*[a-z]+.*";
		Matcher m = Pattern.compile(regex).matcher(str);
		return m.matches();
	}

	/**
	 * 对URL中的字符进行解码.
	 * @param msg
	 * @return
	 */
	public static String fixURLs(String msg) {
		Pattern hrefs = Pattern.compile("href=\"([^\"]*)\"");
		Matcher matcher = hrefs.matcher(msg);
		while (matcher.find()) {
			String original = matcher.group(0);
			String href = original.replaceAll("&#61;", "=").replaceAll("\\.\\.", ".").replaceAll("&amp;", "&");
			msg = msg.replace(original, href);
		}
		return msg;
	}

}
