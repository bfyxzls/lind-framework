package com.lind.common.util;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lind
 * @date 2023/2/27 18:07
 * @since 1.0.0
 */
public class ShortNameDictionaryTest {

	final static Logger log = LoggerFactory.getLogger(ShortNameDictionaryTest.class);

	/**
	 * 分词策略
	 * @return 分词信息
	 */
	public List<Term> segmentCategory(String content) {
		return NLPTokenizer.segment(content);
	}

	/**
	 * 分词并计算词频
	 * @param content 需要进行分词的文本
	 * @return 分词后的词组信息及词频
	 */
	public Map<String, SegmentWord> segment(String content) {
		log.debug("开始执行分词");
		List<Term> termList = this.segmentCategory(content);
		Map<String, SegmentWord> map = new ConcurrentHashMap<String, SegmentWord>();
		termList.forEach(i -> Optional.ofNullable(i.nature == Nature.w ? null : i.nature)
				.ifPresent(m -> map.computeIfAbsent(i.word, k -> new SegmentWord(i.word, i.nature.toString()))
						.getFrequency().incrementAndGet()));
		return map;
	}

	/**
	 * 相似度运算
	 * @param s 分词1
	 * @param o 分词2
	 * @return 分词1和分词2的相似度
	 */
	public double similarity(Map<String, SegmentWord> s, Map<String, SegmentWord> o) {
		List<String> keys = new ArrayList<String>();
		keys.addAll(s.keySet());
		keys.retainAll(o.keySet());
		// 运算分子数据
		return keys.stream().map(val -> s.get(val).getFrequency().intValue() * o.get(val).getFrequency().intValue())
				.reduce((a, b) -> a + b).get()
				/ Math.sqrt(s.values().stream().map(val -> Math.pow(val.getFrequency().intValue(), 2))
						.reduce((a, b) -> a + b).get())
				/ Math.sqrt(o.values().stream().map(val -> Math.pow(val.getFrequency().intValue(), 2))
						.reduce((a, b) -> a + b).get());
	}

	@Test
	public void test1() {
		Map<String, SegmentWord> map = segment("我们把香蕉给猴子因为它们饿了");
		Map<String, SegmentWord> map1 = segment("我们不能把香蕉给猴子因为它们还没有成熟");

		System.out.println(similarity(map, map1));
		System.out.println(similarity(segment("abc123"), segment("abc")));
	}

	/**
	 * 分词基本信息
	 */
	public class SegmentWord implements Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = 5662341029767237202L;

		// 词名
		private String name;

		// 词性
		private String pos;

		// 词频
		private AtomicInteger frequency = new AtomicInteger();

		public SegmentWord(String name, String pos) {
			this.name = name;
			this.pos = pos;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPos() {
			return pos;
		}

		public void setPos(String pos) {
			this.pos = pos;
		}

		public AtomicInteger getFrequency() {
			return frequency;
		}

		public void setFrequency(AtomicInteger frequency) {
			this.frequency = frequency;
		}

	}

}
