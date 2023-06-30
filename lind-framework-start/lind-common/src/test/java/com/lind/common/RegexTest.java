package com.lind.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class RegexTest {

	private static final Logger logger = LoggerFactory.getLogger(RegexTest.class);

	private static final Pattern DOT = Pattern.compile("\\.");

	/**
	 * ^Bearer：以"Bearer"开头 (?<token>：将匹配到的内容命名为"token"
	 * [a-zA-Z0-9-:.~+/]+=*：匹配一串由大小写字母、数字以及特定字符(:、.、、~、+、/)组成的字符串，长度可以为0或任意正整数
	 * 翻译成中文为：以"Bearer"开头，匹配一串由大小写字母、数字以及特定字符(:、.、_、~、+、/)组成的字符串，长度可以为0或任意正整数，并将其命名为"token"。
	 * 中括号([])表示一个字符集合，里面放置的是可以被匹配的字符，例如[a-z]表示所有小写字母。而中括号后面的符号代表下列含义：
	 * +：表示前面的字符出现至少1次。例如[a-z]+可以匹配至少一个小写字母。 =：表示字符集合里面的字符需要被严格匹配。 *：表示前面的字符出现0次或多次。
	 */
	private static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$",
			Pattern.CASE_INSENSITIVE);

	public static String removeUrlSpaceParams(String needValid) {
		needValid = needValid.replaceAll("%C2%A0", "%20");
		needValid = needValid.replaceAll(" ", "%20");
		needValid = needValid.replaceAll("<", "%3C");
		needValid = needValid.replaceAll(">", "%3E");
		needValid = needValid.replaceAll("\\{", "%7B");
		needValid = needValid.replaceAll("}", "%7D");
		needValid = needValid.replaceAll("\"", "%20");
		needValid = needValid.replaceAll("　", "%E3%80%80");
		needValid = needValid.replaceAll("》", "%E3%80%8B");
		needValid = needValid.replaceAll("《", "%E3%80%8A");
		return needValid;
	}

	@Test
	public void basic() {
		Matcher matcher = Pattern.compile("(?<content>[a-z]+)$").matcher("abc");
		Assert.isTrue(matcher.matches(), "匹配失败");
	}

	@Test
	public void authCode() {
		String[] parsed = DOT.split("abc.123.400.aa", 3);// 限制3位，这个数组被分为abc,123,400.aa，最后一位.就不进行拆分了
		logger.info("{}", parsed);
	}

	@Test
	public void bearer() {
		String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5c+:/~_";
		Matcher matcher = AUTHORIZATION_PATTERN.matcher(token);
		if (!matcher.matches()) {
			log.error("Bearer token is malformed");
		}
		else {
			log.info("Bearer token={}", matcher.group("token"));
		}
	}

	// 邮箱@截取
	@Test
	public void emailSplit() {
		Pattern pattern = Pattern.compile("(.*)@(.*)");

		for (int i = 0; i < 1000; i++) {
			String email = "zzl" + i + "@example.com";
			Matcher matcher = pattern.matcher(email);
			if (matcher.matches()) {
				String username = matcher.group(1); // 获取@前面的部分
				String domain = matcher.group(2); // 获取@后面的部分
				log.info("username={},domain={}", username, domain);
			}
		}
	}

	@Test
	public void emailSplit2() {
		String email = "example@example.com";
		int atIndex = email.indexOf("@");
		String username = email.substring(0, atIndex); // 获取@前面的部分
		String domain = email.substring(atIndex + 1); // 获取@后面的部分
		log.info("username={},domain={}", username, domain);
	}

	/**
	 * 按着顿号进行换行，严格匹配，最后一位无顿号不会匹配
	 */
	@Test
	public void trombone() {
		String text = "这是一本《Python编程入门》、这是一本《Java编程入门》、这是一本《C++编程入门》。";
		Pattern pattern = Pattern.compile("《(.*?)》[^、]*?、");
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			String match = matcher.group(1);
			System.out.println(match);
		}
	}

	/**
	 * 按着顿号进行换行，最后一位无顿号也会匹配
	 */
	@Test
	public void three() {
		String text = "这是一本《Python编程入门》、这是一本《Java编程入门》、这是一本《C++编程入门》。";
		Pattern pattern = Pattern.compile("《(.*?)》"); // 修改匹配规则，去掉不需要的部分
		Matcher matcher = pattern.matcher(text);
		List<String> matches = new ArrayList<>(); // 修改存储方式，存储所有匹配结果
		while (matcher.find()) {
			String match = matcher.group(1);
			matches.add(match);
		}
		System.out.println(matches); // 输出所有匹配结果

	}

	@Test
	public void urlEncode() {
		String redirect_uri = "https://pkulaw.com/chl/4d5b6c562483fccebdfb.html?keyword=最高人民法院 最高人民检察院关于常见犯罪的量刑指导意见(试行）";
		redirect_uri = redirect_uri.replaceAll("\\xa0", "");
		URI uri = URI.create(redirect_uri);
	}

}
