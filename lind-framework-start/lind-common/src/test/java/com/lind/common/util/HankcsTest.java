package com.lind.common.util;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.junit.Test;

import java.util.List;

/**
 * @author lind
 * @date 2023/2/27 17:37
 * @since 1.0.0
 */
public class HankcsTest {

	@Test
	public void fenci() {
		// 开始根据 词性 对句子进行分类
		// 比如：
		List<Term> termList = HanLP.segment("李狗蛋拿着篮球去了足球场");
		for (Term term : termList) {
			String nature = term.nature.toString();
			if (nature.equals("n")) {
				System.out.println("名词：" + term.word);
			}
			else if (nature.equals("v")) {
				System.out.println("动词：" + term.word);
			}
			else if (nature.equals("a")) {
				System.out.println("形容词：" + term.word);
			}
		}
	}

}
