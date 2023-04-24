package com.lind.common.util;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.lind.common.hankcs.HitSentence;
import com.lind.common.hankcs.LinkTool;
import com.lind.common.hankcs.PreTool;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * @author lind
 * @date 2023/2/27 16:33
 * @since 1.0.0
 */
public class DicGeneratorImpl {

	private static Pattern yinfaPattern = Pattern.compile("(.{1,})(印发|发布|颁布|颁发)(《.+》)(的.{0,4})(通知|函)$");

	private static Pattern innerYinfaPattern = Pattern.compile("^(印发|发布|颁布|颁发).+等$");

	private static Pattern zhuanfaPattern = Pattern.compile("^(关于|印发|发布|颁布|颁发|转发|适用)(.+)");

	private static Pattern yinfa2Pattern = Pattern.compile("^关于(印发|发布|颁布|颁发)(.+)");

	// 书名号包裹
	private static Pattern bookMarkPackagePattern = Pattern.compile("^[《](.+)》$");

	private static Pattern guanyuPattern = Pattern.compile(
			"(.{1,}?)(关于.{1,})(的.{0,4})(" + "(通知|办法|函|决定|通令|指示|规定|解答|意见|批复|复函|命令|复电|决议|解释|复示|通报|通告)(.{0,1}))$");

	// 只有关于的情况
	private static Pattern onlyGuanyuPattern = Pattern.compile("(.{2,})关于(.{3,})$");

	private static Pattern kuohaoEndPattern = Pattern.compile("(.+)(\\(.+\\))$");

	// 前缀中如果包含 对...转发...等信息，前缀是这之前的内容
	private static Pattern prefixZhuanfaPattern = Pattern.compile("(.{2,})(对|转发|致|复|批转|印发)(?!外)");

	// 前缀书名号
	private static Pattern prefixShuminghaoPattern = Pattern.compile("(.+)(《)");

	// 预处理阶段
	private String befor(String title, Set<LawWord> set) {
		title = title.replaceAll("\\s", "");
		title = PreTool.baesPreproccess(title);
		title = PreTool.replaceAll(title);

		// 删除标题中的“[失效]”
		title = title.replace("[失效]", "").replaceAll("(?<=.{2,})\\([含附].*?\\)$", "")// 全国人大常委会关于决定(含:中国人民水法、中华人民共和职业病防治法、)
				.replaceAll("(?<=.{2,})\\(\\d+([年修])?.*?\\)$", "")// 刑法（1997）
				.replaceAll("(?<=.{2,})\\([^\\(\\)]+号\\)$", "")// xxxx(保监许可xxx号)
				.replaceAll("人民代表大会", "人大").replaceAll("常务委员会", "常委会").replaceAll("最高人民检察院", "最高检")
				.replaceAll("最高人民法院", "最高法").replaceAll("省政府", "省").replaceAll("省人民政府", "省").replaceAll("市政府", "市")
				.replaceAll("市人民政府", "市").replaceAll("县政府", "县").replaceAll("县人民政府", "县");

		//  如果标题中含有“号—”，且在“号—”之前又没有“《”，则标题去掉“号”之后的所有内容。
		int i = title.indexOf("号-");
		if (i > 0) {
			if (!title.substring(0, i).contains("《")) {

				// 将“号-”后面的作为一个表
				LawWord lawWord = new LawWord();
				lawWord.setName(title.substring(i + 2));
				lawWord.setNature(Nature.a);
				lawWord.setTotalFrequency(10);
				lawWord.setWorkedTitle(title);
				set.add(lawWord);

				title = title.substring(0, i + 1);
			}

		}

		//  如果标题以“通知”+“(不是书名号的任意字符)”结尾，则标题将后面的“(任意字符)”去掉。
		int tIndex = title.lastIndexOf("的通知");
		if (tIndex > 3 && title.length() > tIndex + 3 && title.charAt(tIndex + 3) != '》') {

			title = title.substring(0, title.lastIndexOf("的通知") + 3);
		}

		LawWord lawWord = new LawWord();
		lawWord.setName(title);
		lawWord.setNature(Nature.a);
		lawWord.setTotalFrequency(10);
		lawWord.setWorkedTitle(title);
		set.add(lawWord);

		return title;
	}

	public Set<LawWord> generate(String title) {

		Set<LawWord> set = new HashSet<>();

		if (Pattern.matches("(中华人民共和国)?(国务院令|主席令).*?", title)) {
			return set;
		}

		title = befor(title, set);

		Params params = new Params();

		// 预处理后的标题
		params.titleAfterPreprocessing = new String(title);
		params.workedTitle = new String(title);

		// 特殊处理
		if (title.contains("国务院机构改革方案的决定")) {
			LawWord lawWord = new LawWord();
			lawWord.setName("国务院机构改革方案");
			lawWord.setNature(Nature.a);
			lawWord.setTotalFrequency(999);
			lawWord.setWorkedTitle(params.titleAfterPreprocessing);
			set.add(lawWord);
			return set;
		}

		Matcher yinfaMatcher = yinfaPattern.matcher(params.workedTitle);

		if (yinfaMatcher.find()) {
			// 处理印发
			String group1 = yinfaMatcher.group(1);
			String group2 = yinfaMatcher.group(2);
			String group3 = yinfaMatcher.group(3);
			String group4 = yinfaMatcher.group(4);
			String group5 = yinfaMatcher.group(5);
			// 印发作为分隔
			params.workedTitle = group2 + group3;
			// 前缀
			params.preSplitName = Optional.of(group1);
			// 前缀位置
			// params.preSplitPosition = Optional.of(params.workedTitle.indexOf(group1));
			// 后缀位置
			// params.sufPplitPosition =
			// Optional.of(params.workedTitle.lastIndexOf(group4));
			// 后缀
			params.sufSplitName = Optional.of(group5);
			// 后缀全部
			params.holeSuffix = Optional.of(group4 + group5);

			// 去掉“的”的部分
			params.de = Optional.of(group4.substring(1));

			params.name = Optional.of(group2 + group3);

			// set.add(toLawWord(params));
		}
		else {
			// 删除最后的括号及括号中的内容，保留括号的内容判断是否为数字
			Matcher matcher = kuohaoEndPattern.matcher(params.workedTitle);
			if (matcher.find()) {
				String group = matcher.group(2);
				if (!group.equals("(修正)")) {
					params.sufKuohao = Optional.of(group);
				}
				params.workedTitle = matcher.group(1);
			}

			// 关于
			matcher = guanyuPattern.matcher(params.workedTitle);
			if (matcher.find()) {
				String pre = matcher.group(1);
				String middle = matcher.group(2);
				String de = matcher.group(3);

				String suf = matcher.group(4);

				params.preSplitName = Optional.of(pre);
				// params.preSplitPosition = Optional.of(params.workedTitle.indexOf(pre));

				params.name = Optional.of(middle);

				params.sufSplitName = Optional.of(suf);

				// 后缀全部
				params.holeSuffix = Optional.of(de + suf);

				// 去掉“的”的部分
				params.de = Optional.of(de.substring(1));

				// params.sufPplitPosition =
				// Optional.of(params.workedTitle.lastIndexOf(suf));
				// set.add(toLawWord(params));

			}
			else {

				// 只有关于的情况
				Matcher onlyMatcher = onlyGuanyuPattern.matcher(params.workedTitle);
				if (onlyMatcher.find()) {
					String pre = onlyMatcher.group(1);

					String main = onlyMatcher.group(2);

					params.preSplitName = Optional.of(pre);

					params.name = Optional.of(main);

					// 没有后缀
					// set.add(toLawWord(params));

				}
				else {
					params.name = Optional.of(params.workedTitle);

				}

			}
		} // 前缀后缀切分完毕

		// 处理前缀
		if (params.preSplitName.isPresent()) {

			if (params.preSplitName.get().startsWith("《")) {
				params.name = params.name.map(s -> params.preSplitName.get() + s);
				params.preSplitName = Optional.of("");
			}

			// 前缀中如果包含书名号，那么前缀是书名号前面的内容
			Matcher matcher = prefixShuminghaoPattern.matcher(params.preSplitName.get());
			if (matcher.find()) {
				params.preSplitName = Optional.of(matcher.group(1));
				params.name = Optional.of(matcher.group(2) + params.name.get());
			}

			// 前缀中如果包含 对...转发...等信息，前缀是这之前的内容
			matcher = prefixZhuanfaPattern.matcher(params.preSplitName.get());
			if (matcher.find()) {
				params.preSplitName = Optional.of(matcher.group(1));
			}

			// 如果前缀以引号结尾，去掉引号
			if (params.preSplitName.get().endsWith("\"")) {
				params.preSplitName = params.preSplitName.map(s -> s.substring(0, s.length() - 1));
			}

			if (params.preSplitName.get().endsWith("关于")) {
				params.preSplitName = params.preSplitName.map(s -> s.substring(0, s.length() - 2));
			}

			String pre = params.preSplitName.orElse("");

			// 前缀长度小于3，不要
			// if (pre.length() < 3) {
			// params.preSplitName = Optional.empty();
			// }

			if (StringUtils.isBlank(pre)) {
				params.preSplitName = Optional.empty();
			}

		}

		// 处理后缀
		if (params.sufSplitName.isPresent()) {
			// 如果以书名号结尾，那么没有后缀
			if (params.sufSplitName.get().endsWith("》")) {
				params.name = params.name.map(s -> s + params.sufSplitName.get());
				params.sufSplitName = Optional.empty();
			}
			else
			// 如果后缀是以引号结尾，去掉引号
			if (params.sufSplitName.get().endsWith("\"")) {
				params.sufSplitName = params.sufSplitName.map(s -> s.substring(0, s.length() - 1));
			}

			// 加上后面括号里不是修正的内容
			String suf = params.sufKuohao.filter(s -> !s.equals(("修正"))).orElse("");
			// 加上"联合"
			params.sufSplitName = params.sufSplitName.map(s -> s + suf);
			// params.sufSplitName = params.sufSplitName.map(s -> params.de.get() + s +
			// suf);

			params.sufKuohao = Optional.empty();

			String sufResult = params.sufSplitName.orElse("");

			if (StringUtils.isBlank(sufResult)) {
				params.sufSplitName = Optional.empty();
			}

		}

		// 判断是否为关于印发
		// 不是关于印发、、、的通知类的，就不拆后缀
		String middle = "关于" + params.name.get();

		if (!yinfa2Pattern.matcher(middle).find() && params.sufSplitName.isPresent()) {
			params.name = params.name.map(s -> s + "的" + params.de.orElse("") + params.sufSplitName.get());
			params.sufSplitName = Optional.empty();
		}

		// 前缀后缀处理完了，先存一版
		set.add(toLawWord(params));
		// 处理书名号
		List<HitSentence> innerShuminghao = LinkTool
				.innerShuminghao(new HitSentence(params.name.get(), 0, params.name.get().length()), false);

		// 长度小于7并且不是例则法结尾的法规，不要
		innerShuminghao = innerShuminghao.stream().filter(hitSentence -> {
			String name = hitSentence.word;
			if (name.length() < 7 && !Pattern.matches(".+[例则法]$", name)) {
				return false;
			}
			return true;
		}).collect(toList());

		if (innerShuminghao.size() == 1) {
			// 如果是单个书名号并且开头是“印发、颁发”等，并且书名号下一个字符为“等”，拆分
			params.name = params.name.filter(s -> innerYinfaPattern.matcher(s).find());
			// 存
			if (params.name.isPresent()) {
				set.add(toLawWord(params));
			}
		}
		else {
			// 如果是多个书名号，标题开头是“印发，颁发”等，结尾是通知或者函，那么进行拆分
			List<HitSentence> collect = innerShuminghao.stream()
					.filter(h -> params.sufSplitName.filter(s -> s.contains("通知") || s.contains("函")).isPresent()
							&& params.name.filter(s -> {
								boolean matches = Pattern.matches("^(印发|发布|颁布|办法|实施|转发|修订|颁发).*?", s);

								int i = s.lastIndexOf("》");
								if (i > 0) {
									String substring = s.substring(i);
									if (substring.contains("问题") || substring.contains("文本")) {
										matches = false;
									}
								}
								return matches;

							}).isPresent())
					.collect(toList());
			// 添加进set
			collect.forEach(h -> {
				params.name = Optional.of(h.word);
				set.add(toLawWord(params));
			});
		}

		Set<LawWord> set2 = new HashSet<>();

		set.stream().map(lawWord -> {
			Nature nature = lawWord.getNature();
			String name = lawWord.getName();
			// 如果标题以“中华人民共和国”开头，后面紧跟着的不是“关、”的，前缀为“中华人民共和国”。
			if (nature == Nature.a && name.startsWith("中华人民共和国") && name.charAt(7) != '关') {
				try {
					LawWord clone = (LawWord) lawWord.clone();
					name = clone.getName();
					if (name.charAt(7) == '、') {
						name = name.substring(8);
					}
					else {
						name = name.substring(7);
					}
					clone.setName(name);
					set2.add(clone);
				}
				catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
			return lawWord;
		}).map(lawWord -> {
			String name = lawWord.getName();
			// 去掉主体开头的关于、印发、发布、转发
			Matcher zhuanfaMatcher = zhuanfaPattern.matcher(name);
			boolean zhuanfaFind = zhuanfaMatcher.find();

			Matcher bookMarkMatcher = bookMarkPackagePattern.matcher(name);
			boolean bookMarkFind = bookMarkMatcher.find();

			// 循环去掉外层书名号和转发
			while (zhuanfaFind || bookMarkFind) {
				if (zhuanfaFind) {
					name = zhuanfaMatcher.group(2);
				}
				else {
					name = bookMarkMatcher.group(1);
				}
				zhuanfaMatcher = zhuanfaPattern.matcher(name);
				zhuanfaFind = zhuanfaMatcher.find();
				bookMarkMatcher = bookMarkPackagePattern.matcher(name);
				bookMarkFind = bookMarkMatcher.find();
			}

			lawWord.setName(name);
			return lawWord;
		})

				.map(lawWord -> {
					String name = lawWord.getName();
					if (name.length() < 7 && !(name.endsWith("例") || name.endsWith("则") || name.endsWith("法"))
							&& lawWord.getNature() == Nature.b) {
						lawWord.setSuffix(null);
						lawWord.setName(lawWord.getName() + params.holeSuffix.orElse(""));
						lawWord.setNature(Nature.a);
					}
					return lawWord;
				}).forEach(lawWord -> {
					set2.add(lawWord);
					LawWord nLaw = null;
					try {
						nLaw = (LawWord) lawWord.clone();
					}
					catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
					String name = nLaw.getName();
					name = name.replaceAll("《", "").replaceAll("》", "");
					nLaw.setName(name);
					String workedTitle = nLaw.getWorkedTitle();
					workedTitle = workedTitle.replaceAll("《", "").replaceAll("》", "");
					nLaw.setWorkedTitle(workedTitle);
					set2.add(nLaw);
				});

		HashSet<LawWord> set3 = new HashSet<>(set2);
		set2.forEach(lawWord -> {
			String name = lawWord.getName();
			if (name.indexOf("中华人民共和国") > 0) {
				try {
					LawWord lawWord1 = (LawWord) lawWord.clone();
					lawWord1.setName(lawWord.getName().replaceAll("中华人民共和国", ""));
					lawWord1.setWorkedTitle(lawWord.getWorkedTitle().replaceAll("中华人民共和国", ""));
					set3.add(lawWord1);
				}
				catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		});

		// 进行最后的过滤
		Set<LawWord> collect = set3.stream().filter(lawWord -> {
			// 长度小于3的前缀不要，避免出现“实施"等词汇，影响判断
			if (lawWord.getPrefix() != null && lawWord.getPrefix().length() < 3) {
				return false;
			}
			String name = lawWord.getName();
			if (name.length() < 3 && !name.equals("刑法") && !name.equals("宪法")) {
				return false;
			}

			if (name.length() < 7 && !(name.endsWith("例") || name.endsWith("则") || name.endsWith("法"))) {
				return false;
			}
			// 以执照、证照结尾的不作为法规
			if (name.endsWith("执照") || name.endsWith("证照")) {
				return false;
			}
			return true;
		}).collect(toSet());

		// HashSet<LawWord> result = new HashSet<>();
		//
		// collect.forEach(lawWord -> {
		// String name = lawWord.getName();
		// //长度小于7并且不是例则法结尾的法规，不要
		// if (name.length() < 7 && !Pattern.matches(".+[例则法]$", name)) {
		// return false;
		// }
		// });

		return collect;
	}

	private LawWord toLawWord(Params params) {

		LawWord word;
		// 有后缀，有前缀
		if (params.sufSplitName.isPresent() && params.preSplitName.isPresent()) {
			// 前面已经加过最后的括号，所以在处理后缀的时候不要再加括号了
			word = new LawWord(params.preSplitName.get(), params.sufSplitName.get(), params.name.get(), Nature.a);
			// 有前缀，没后缀
		}
		else if (params.preSplitName.isPresent() && !params.sufSplitName.isPresent()) {
			word = new LawWord(params.preSplitName.get(), null, params.name.get() + params.sufKuohao.orElse(""),
					Nature.b);
			// 没前缀，有后缀
		}
		else if (!params.preSplitName.isPresent() && params.sufSplitName.isPresent()) {
			// 后缀合并到主体上，作为law0
			word = new LawWord(null, null, params.name.get() + params.sufSplitName.get() + params.sufKuohao.orElse(""),
					Nature.a);
		}
		else {
			word = new LawWord(null, null, params.name.get() + params.sufKuohao.orElse(""), Nature.a);
		}

		word.setWorkedTitle(params.titleAfterPreprocessing);
		word.setTotalFrequency(10);

		return word;
	}

	@Data
	public class LawWord implements Serializable, Cloneable {

		private String prefix;

		private String suffix;// 后缀

		private String name;

		private int totalFrequency;

		private Nature nature;

		private String workedTitle;

		public LawWord() {
		}

		public LawWord(String prefix, String suffix, String name, Nature nature) {
			this.prefix = prefix;
			this.suffix = suffix;
			this.name = name;
			this.nature = nature;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			LawWord lawWord = (LawWord) o;

			if (totalFrequency != lawWord.totalFrequency)
				return false;
			if (prefix != null ? !prefix.equals(lawWord.prefix) : lawWord.prefix != null)
				return false;
			if (suffix != null ? !suffix.equals(lawWord.suffix) : lawWord.suffix != null)
				return false;
			if (name != null ? !name.equals(lawWord.name) : lawWord.name != null)
				return false;
			return nature == lawWord.nature;
		}

		@Override
		public int hashCode() {
			int result = prefix != null ? prefix.hashCode() : 0;
			result = 31 * result + (suffix != null ? suffix.hashCode() : 0);
			result = 31 * result + (name != null ? name.hashCode() : 0);
			result = 31 * result + totalFrequency;
			result = 31 * result + (nature != null ? nature.hashCode() : 0);
			return result;
		}

		@Override
		public Object clone() throws CloneNotSupportedException {
			return super.clone();
		}

	}

	class Params {

		Optional<String> sufKuohao = Optional.empty();

		// 前缀
		Optional<String> preSplitName = Optional.empty();

		// 后缀
		Optional<String> sufSplitName = Optional.empty();

		// 主体
		Optional<String> name = Optional.empty();

		// 完整后缀，如 联合通知
		Optional<String> holeSuffix = Optional.empty();

		Optional<String> de = Optional.empty();

		// 前分隔符位置
		Optional<Integer> preSplitPosition = Optional.empty();

		// 后分隔符位置
		Optional<Integer> sufPplitPosition = Optional.empty();

		String titleAfterPreprocessing;

		String workedTitle;

		HitSentence currentSentence;

		// 书名号数量
		int bookMarkNum;

	}

}
