package com.lind.common.hankcs;

/**
 * @author lind
 * @date 2023/2/27 16:31
 * @since 1.0.0
 */
public class PreTool {

	/**
	 * 把连续的-换成单个-
	 * @param content
	 * @return
	 */
	public static String baesPreproccess(String content) {
		content = content.replaceAll("[―—－]+", "-");
		// 去掉空格
		// StringBuilder sb = new StringBuilder();
		// for (int i = 0; i < content.length() - 1; i++) {
		// //空格转成int型代表数字是32
		// if ((int) content.charAt(i) == 32) {
		// continue;
		// }
		// sb.append(content.charAt(i));
		// }
		// return sb.toString();
		return content;
	}

	/**
	 * 替换所有的括号，书名号等
	 * @return
	 */
	public static String replaceAll(String content) {

		content = content.replaceAll("[〔（]", "(").replaceAll("[）〕]", ")")// 中文括号转换成英文括号
				.replaceAll("，", ",")// 中文逗号转英文逗号
				.replaceAll("'", "\"").replaceAll("“", "\"").replaceAll("”", "\"")// 引号转成英文引号
				.replaceAll("[‘’]", "'")// 单引号转换成英文单引号
				.replaceAll("[<＜〈﹤]", "《").replaceAll("[>＞〉﹥]", "》");// 书名号统一换成双书名号
		// .replaceAll("[-—－]", "—");//

		content = toDBC(content);// 将全角转换为半角

		// //去掉空格
		// StringBuilder sb = new StringBuilder();
		// for (int i = 0; i < content.length() - 1; i++) {
		// //空格转成int型代表数字是32
		// if ((int) content.charAt(i) == 32) {
		// continue;
		// }
		// sb.append(content.charAt(i));
		// }
		//
		// return sb.toString();
		return content;
	}

	/**
	 * 半角转全角
	 * @param input String.
	 * @return 全角字符串.
	 */
	public static String toSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			}
			else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	/**
	 * 全角转半角
	 * @param input String.
	 * @return 半角字符串
	 */
	public static String toDBC(String input) {

		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			}
			else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);

		return returnString;
	}

}
