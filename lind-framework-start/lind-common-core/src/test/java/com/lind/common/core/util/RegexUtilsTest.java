package com.lind.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.PatternMatchUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@SuppressWarnings("unchecked")
public class RegexUtilsTest {

	private static final Pattern DATA_FILE_RE = Pattern.compile("data\\.([0-9]+)"); // data.1
	static List<String> roots = Arrays.asList("cat", "bat", "rat");
	static String sentence = "the cattle was rattled by the battery";

	@Test
	public void test() {
		// 将下面的国家中间的数字替换成空格
		String str = "China12345America678922England342343434Mexica";
		System.out.println(str.replaceAll("\\d+", " "));

		// 将下面的国家重叠的字符替换成 一个, 也就是去掉重复的分隔符
		str = "China|||||America::::::England&&&&&&&Mexica";
		System.out.println(str.replaceAll("(.)\\1+", "$1▲$1"));

		// 将字符串 "李阳 王丽 李明 张俊 小雷" 的空格和tab 替换成 逗号
		str = "李阳 	王丽		李明    张俊 小雷";
		System.out.println(str.replaceAll("[ \\t]+", ","));

		str = "123aa-34345bb-234cc-00";
		System.out.println(str.replaceAll("(\\d{3,5})([a-z]{2})", "$1▲$2"));

		/**
		 * 1、英文句点.符号：匹配单个任意字符。
		 *
		 * 表达式t.o 可以匹配：tno，t#o，teo等等。不可以匹配：tnno，to，Tno，t正o等。
		 *
		 * 2、中括号[]：只有方括号里面指定的字符才参与匹配，也只能匹配单个字符。
		 *
		 * 表达式：t[abcd]n 只可以匹配：tan，tbn，tcn，tdn。不可以匹配：thn，tabn，tn等。
		 *
		 * 3、| 符号。相当与“或”，可以匹配指定的字符，但是也只能选择其中一项进行匹配。
		 *
		 * 表达式：t(a|b|c|dd)n 只可以匹配：tan，tbn，tcn，tddn。不可以匹配taan，tn，tabcn等。
		 */
		// ^表示以什么开头,[]里面是可取的,\\s+表示后面可以有多个空格

		str = "g_d  ios  text";
		System.out.println(str.replaceAll("(g[_]d\\s+)(i[o|q|oos]s\\s+)([^text])", "$1\n$2\n$3"));

		str = "addd b c d";// 第一位为a，第二位有a-z之前的3个字符，然后用_替换
		System.out.println(str.replaceAll("([a][a-z]{3}\\s)", "_"));

		str = "，男，";// 第一位为[，。,；;,]里的一个字符，第二位男或女，最后是[，。,；;,]的一个字符
		System.out.println(str.replaceAll("([，。,；;,])([男 女][，。,；;,])", "$1▲$2"));

		str = "，家住北京大兴15房院";// 第二位配置住和家住这两个词，然后以房院结尾，最后[^房院]是可选的
		System.out.println(str.replaceAll("([，。,；;,])(家?住[^房院])", "$1▲$2"));

		str = "，住北京大兴15房院";// 第二位配置家住这个词，然后以房院结尾
		System.out.println(str.replaceAll("([，。,；;,])(家?住[^房院])", "$1▲$2"));

		str = "，家住北京";// 第二位配置家住这个词，然后以房院结尾
		System.out.println(str.replaceAll("([，。,；;,])(家?住[^房院])", "$1▲$2"));
	}

	@Test
	public void hideOtherText() {
		String msg = "，1983年03月18日出生，";
		log.info(RegexUtils.hideOtherText(msg));
	}

	@Test
	public void replaceMobileHtml() {
		String mobile = RegexUtils.replaceMobileHtml("<p style='font-size:5px;'>136011712991</p>");
		log.info("mobile={}", mobile);
	}

	@Test
	public void abbreviate() {
		log.info(RegexUtils.abbreviate("hello world!", 10));
		log.info(RegexUtils.abbreviate("镇人民共和国", 10));

	}

	@Test
	public void containUpper() {
		Assert.assertTrue(RegexUtils.isContainUpper("abcDefg"));
	}

	@Test
	public void fileName() {
		Matcher matcher = DATA_FILE_RE.matcher("data.123");
		Assert.assertTrue(matcher.matches());
		Assert.assertTrue(DATA_FILE_RE.matcher("data.42").matches());
	}

	@Test
	public void max() {
		System.out.println(Math.max(1, 2));
		System.out.println(Math.max(2, 22));
		Long.parseLong("0001");
	}

	@Test
	public void group() {
		Matcher matcher = DATA_FILE_RE.matcher("data.001");
		matcher.matches();
		matcher.group(1);
	}

	@Test
	public void matcherGroup() {
		List<String> ids = Arrays.asList("data.1", "data.3", "data.2", "data.2");
		long maxFileId = -1L;
		for (String id : ids) {
			Matcher matcher = DATA_FILE_RE.matcher(id);
			matcher.matches();
			maxFileId = Math.max(Long.parseLong(matcher.group(1)), maxFileId); // group（0）就是指的整个串，group（1）
																				// 指的是第一个括号里的东西，group（2）指的第二个括号里的东西
			System.out.println("maxFileId=" + maxFileId);
		}

	}

	@Test
	public void hyperLinkDecode() {
		String linkMsg = "<a href=\"www.baidu.com?a=1&amp;b=2\">ok</a>sdafasfasdafs<a href=\"www.baidu.com?a=1&amp;word=3\">ok2</a>";
		String linkMsgSimple = RegexUtils.fixURLs(linkMsg);
		System.out.println(linkMsgSimple);

	}

	@Test
	public void replaceStr() {
		String s1 = "my name is khan my name is java";
		String replaceString = s1.replace("is", "was");// replaces all occurrences of "is"
														// to "was"
		System.out.println(replaceString);

	}

	/**
	 * 前缀哈希【通过】 遍历句子中每个单词，查看单词前缀是否为词根。 将所有词根 roots 存储到集合 Set
	 * 中。遍历所有单词，判断其前缀是否为词根。如果是，则使用前缀代替该单词；否则不改变该单词。
	 */
	@Test
	public void replaceWords1() {
		Set<String> rootset = new HashSet();
		for (String root : roots)
			rootset.add(root);

		StringBuilder ans = new StringBuilder();
		for (String word : sentence.split("\\s+")) {
			String prefix = "";
			for (int i = 1; i <= word.length(); ++i) {
				prefix = word.substring(0, i);
				if (rootset.contains(prefix))
					break;
			}
			if (ans.length() > 0)
				ans.append(" ");
			ans.append(prefix);
		}
		log.info(ans.toString());
	}

	/**
	 * 前缀树【通过】 思路和算法 把所有的词根放入前缀树中，在树上查找每个单词的最短词根，该操作可在线性时间内完成。
	 */
	@Test
	public void replaceWords2() {
		TrieNode trie = new TrieNode();
		for (String root : roots) {
			TrieNode cur = trie;
			for (char letter : root.toCharArray()) {
				if (cur.children[letter - 'a'] == null)
					cur.children[letter - 'a'] = new TrieNode();
				cur = cur.children[letter - 'a'];
			}
			cur.word = root;
		}

		StringBuilder ans = new StringBuilder();

		for (String word : sentence.split("\\s+")) {
			if (ans.length() > 0)
				ans.append(" ");

			TrieNode cur = trie;
			for (char letter : word.toCharArray()) {
				if (cur.children[letter - 'a'] == null || cur.word != null)
					break;
				cur = cur.children[letter - 'a'];
			}
			ans.append(cur.word != null ? cur.word : word);
		}
		log.info(ans.toString());
	}

	@Test
	public void findStr() {
		String query = "user.userName";
		String[] kv = query.split("\\.");
		log.info("len:{} {} {}", kv.length, kv[0], kv[1]);

	}

	@Test
	public void simpleMatch() {
		log.debug("true {}", PatternMatchUtils.simpleMatch("/**", "/index"));
		log.debug("true {}", PatternMatchUtils.simpleMatch("/user/**", "/user/add/1"));
		log.debug("true {}", PatternMatchUtils.simpleMatch("/user/*", "/user/add/1"));
		log.debug("false {}", PatternMatchUtils.simpleMatch("/user", "/user/add/1"));
	}

	@Test
	public void verifyBearerToken() {
		// CASE_INSENSITIVE 不区分大小写
		Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$",
				Pattern.CASE_INSENSITIVE);
		String token = "bearer abcd";
		Matcher matcher = authorizationPattern.matcher(token);
		if (!matcher.matches()) {
			log.error("Bearer token is malformed");
		}
		else {
			log.info("Bearer token is {}", matcher.group("token"));
		}

	}

	@Test
	public void ipAddressReg() {
		String range1 = "192.168.0.1-192.168.0.255";
		String range2 = "10.0.0.1-10.0.0.10";
		String range3 = "10.0.0.1/10.1.1.2";
		String regex = "^\\b(?:\\d{1,3}\\.){3}\\d{1,3}-\\b(?:\\d{1,3}\\.){3}\\d{1,3}$";

		System.out.println(range1 + " is valid format: " + range1.matches(regex));
		System.out.println(range2 + " is valid format: " + range2.matches(regex));
		System.out.println(range3 + " is valid format: " + range3.matches(regex));
	}

	class TrieNode {

		TrieNode[] children;

		String word;

		TrieNode() {
			children = new TrieNode[26];
		}

	}

}
