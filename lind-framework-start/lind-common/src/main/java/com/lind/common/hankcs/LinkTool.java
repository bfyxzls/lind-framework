package com.lind.common.hankcs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lind
 * @date 2023/2/27 16:39
 * @since 1.0.0
 */
public class LinkTool {

	private static String dig_sets = "〇一二三四五六七八九0123456789";

	private static String NUM_STR = "〇一二三四五六七八九0123456789零";

	public static String findMaxYear(String content) {
		if (content.length() < 4) {
			return "";
		}
		String maxyear = "1900", myyear;
		int pp = 3, n = 0, i = 0;

		while ((pp = content.indexOf("年", pp + 1)) > 0) {
			myyear = "";
			if (pp > 3) {
				for (i = pp - 4; i < pp; i++) {
					n = dig_sets.indexOf(String.valueOf(content.charAt(i))) % 10;
					if (n >= 0) {
						myyear += n + "";
					}
					else {
						break;
					}
				}
				if (i == pp && myyear.compareTo(maxyear) > 0) {
					maxyear = myyear;
				}
			}
		}
		return maxyear;
	}

	public static int getYear(Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public static String getDigitYear(String mytext, int len, String year) {

		String ret = "", ss, mymatch;

		mymatch = "[〇一二三四五六七八九十０-９0-9]{" + len + "}(?=" + year + ")";

		Pattern p = Pattern.compile(mymatch);

		Matcher matcher = p.matcher(mytext);
		if (matcher.find()) {
			ss = matcher.group();
			if (ss.indexOf("十") > -1) {
				if (ss.equals("十")) {
					return "10";
				}
				if (ss.charAt(0) == '十') {
					ss = ss.replace("十", "1");
				}
				else {
					ss = ss.replace("十", "");
				}
			}

			int n;
			for (int i = 0; i < ss.length(); i++) {
				n = dig_sets.indexOf(ss.charAt(i) + "") % 10;
				if (n >= 0) {
					ret += n + "";
				}
				else {
					break;
				}
			}
		}
		return ret;

	}

	public static Integer getDigitYear(String mytext, String yearReg) {

		String ret = "", ss, mymatch;

		// mymatch = "[〇一二三四五六七八九十0-9]{" + len + "}(?=" + yearReg + ")";
		mymatch = "[〇一二三四五六七八九十0-9]{2,4}(?=" + yearReg + ")";

		Pattern p = Pattern.compile(mymatch);

		Matcher matcher = p.matcher(mytext);
		while (matcher.find()) {
			ss = matcher.group();
			if (ss.indexOf("十") > -1) {
				if (ss.equals("十")) {
					return 2010;
				}
				if (ss.charAt(0) == '十') {
					ss = ss.replace("十", "1");
				}
				else {
					ss = ss.replace("十", "");
				}
			}

			ret = "";
			int n;
			for (int i = 0; i < ss.length(); i++) {
				n = dig_sets.indexOf(ss.charAt(i) + "") % 10;
				if (n >= 0) {
					ret += n + "";
				}
				else {
					break;
				}
			}
		} // end of while
		Integer year = null;
		String tempNum = ret;
		if (tempNum.length() == 4) {
			int yearNum = Integer.parseInt(tempNum);
			if (yearNum > 1900 && yearNum < 2030) {
				year = yearNum;
			}
		}
		else if (tempNum.length() == 2) {
			int yearNum = Integer.parseInt(tempNum);
			// 如果大于40那就是19多少年的
			if (yearNum > 40) {
				year = Integer.parseInt("19" + yearNum);
			}
			else {
				if (yearNum < 10) {
					year = Integer.parseInt("200" + yearNum);
				}
				else {
					year = Integer.parseInt("20" + yearNum);
				}
			}
		}
		else if (tempNum.length() == 1) {
			year = Integer.parseInt("200" + tempNum);
		}

		return year;

	}

	private static Pattern NUM_PATTERN = Pattern.compile(".*?([" + NUM_STR + "]{2,4})$");

	/**
	 * 获取结尾的数字
	 * @param content
	 * @return
	 */
	public static String getLastNum(String content) {
		Matcher matcher = NUM_PATTERN.matcher(content);
		while (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private static Pattern patternShuminghao = Pattern.compile("(《)([^《》]+)(》)");

	/**
	 * 获取成对的书名号
	 * @param hitSentence
	 * @param needDepp 是否需要递归查询
	 * @return
	 */
	public static List<HitSentence> innerShuminghao(HitSentence hitSentence, boolean needDepp) {

		List<HitSentence> inners = new ArrayList<>();

		String title = hitSentence.word;

		Matcher matcher = patternShuminghao.matcher(title);
		while (matcher.find()) {
			int start = matcher.start() + hitSentence.begin;
			int end = start + matcher.group().length();
			String group = matcher.group(2);
			HitSentence hit = new HitSentence();
			hit.begin = start;
			hit.end = end;
			hit.word = group.replaceAll("<", "《").replaceAll(">", "》");
			hit.word = "《" + hit.word + "》";
			inners.add(hit);

			String rr = "<" + group + ">";
			title = title.replace("《" + group + "》", rr);
			matcher = patternShuminghao.matcher(title);
		}

		if (!needDepp) {
			List<HitSentence> result = new ArrayList<>();

			for (int i = 0; i < inners.size() - 1; i++) {
				boolean beSurround = false;// 被书名号包围
				HitSentence hi = inners.get(i);
				for (int j = i + 1; j < inners.size(); j++) {
					HitSentence hj = inners.get(j);
					// 说明被包围
					if (hj.begin < hi.end && hj.end > hi.end) {
						beSurround = true;
						break;
					}
				}
				if (beSurround) {
					continue;
				}
				else {
					result.add(hi);
				}
			}
			if (!inners.isEmpty()) {
				result.add(inners.get(inners.size() - 1));
			}
			return result;

		}
		return inners;
	}

}
