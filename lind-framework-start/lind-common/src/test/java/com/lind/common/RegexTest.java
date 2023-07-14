package com.lind.common;

import com.lind.common.core.jackson.JsonSerialization;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

	private static final Pattern CLIENT_ID_PATTERN = Pattern.compile("\\$\\{client_id\\}");

	private static final Pattern DOT_PATTERN = Pattern.compile("\\.");

	private static final String DOT_REPLACEMENT = "\\\\\\\\.";

	private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{(.+?)\\}");

	private static final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

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

	// 字符模板的替换，将${clientId}替换为clientId
	@Test
	public void replaceClientRole() throws IOException {
		// 在上述代码中，protocolClaim 变量通过
		// CLIENT_ID_PATTERN.matcher(protocolClaim).replaceAll(clientId) 进行替换操作，将其中的
		// ${client_id} 表达式替换为 clientId 变量的值。
		//
		// 关于为什么要关心 clientId 中是否有点号的原因，可以从 ${client_id}
		// 表达式的含义来理解。在许多应用程序或配置文件中，${client_id}
		// 是一种常见的占位符语法，用于表示将在运行时动态替换为实际的客户端标识符。这种语法类似于模板变量，其中 ${} 中的部分表示占位符，需要被真实的值替换。
		//
		// 在上述代码中，首先定义了一个名为 DOT_PATTERN 的正则表达式模式，用于匹配字符串中的点号（.）。然后，将点号替换为四个反斜杠加点号的形式，即
		// "\\\\" 表示一个反斜杠，所以四个反斜杠实际表示两个反斜杠。
		//
		// 这样做的目的是在替换 ${client_id} 之前，先将 clientId
		// 中的点号进行转义，以避免点号被解释为正则表达式中的特殊字符。因为点号在正则表达式中表示任意字符，如果不进行转义，可能会导致替换过程出现意外的结果或错误匹配。
		//
		// 综上所述，为了确保替换操作的准确性，protocolClaim 中的 ${client_id} 表达式在进行替换之前需要先处理 clientId
		// 中的点号，将其转义为四个反斜杠加点号的形式。

		String protocolClaim = "resource_access.${client_id}.roles";
		String clientId = "pkulaw.com";
		// 使用 DOT_PATTERN.matcher(clientId) 创建了一个 Matcher 对象，
		// 用于对字符串 clientId 进行匹配操作。然后，调用 replaceAll(DOT_REPLACEMENT) 方法，
		// 将匹配到的点号替换为四个反斜杠加点号的形式,最终解析为2个反斜杠。最终，将处理后的结果赋值给 clientId 变量。
		clientId = DOT_PATTERN.matcher(clientId).replaceAll(DOT_REPLACEMENT);
		log.info("clientId={}", clientId);
		protocolClaim = CLIENT_ID_PATTERN.matcher(protocolClaim).replaceAll(clientId);
		log.info("protocolClaim={}", protocolClaim);
		Map<String, String> map = new HashMap<>();
		map.put(protocolClaim, "test");
		log.info("map={}", JsonSerialization.writeValueAsString(map));
		assertEquals("resource_access.pkulaw\\.com.roles", protocolClaim);

	}

	@Test
	public void filterHttpMethod() {
		String method = "GET";
		assertTrue(allowedMethods.matcher(method).matches());
	}

	// 替换多个花括号{}中间的内容
	@Test
	public void filter2() {
		String input = "Hello, {name}! Welcome to {city}.";

		Matcher matcher = PLACEHOLDER_PATTERN.matcher(input);
		Map<String, String> replacements = new HashMap<>();
		replacements.put("name", "lind");
		replacements.put("city", "beijing");
		StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			String placeholder = matcher.group(1);
			System.out.println("Found placeholder: " + placeholder);
			String replacement = replacements.getOrDefault(placeholder, matcher.group());
			matcher.appendReplacement(result, replacement);
		}
		matcher.appendTail(result);

		System.out.println(result.toString());
	}

	// 获取美元
	@Test
	public void usaDollar() {
		String input = "The price is $10.";
		Pattern dollar_pattern = Pattern.compile("[$]");

		Matcher matcher = dollar_pattern.matcher(input);
		while (matcher.find()) {
			System.out.println("Found dollar symbol at index " + matcher.start());
			System.out.println(input.substring(matcher.start()));
		}
	}

}
