package com.lind.common.hankcs;

/**
 * @author lind
 * @date 2023/2/27 16:44
 * @since 1.0.0
 */

import com.hankcs.hanlp.corpus.tag.Nature;

import java.util.Set;

/**
 * 分出来的词
 */
public class HitSentence {

	public String word;

	public int begin;

	public int end;

	public Set<Nature> natureSet;

	public HitSentence(String word, int begin, int end) {
		this.word = word;
		this.begin = begin;
		this.end = end;
	}

	public HitSentence() {
	}

	@Override
	public String toString() {
		return "HitSentence{" + "word='" + word + '\'' + ", begin=" + begin + ", end=" + end + ", natureSet="
				+ natureSet + '}';
	}

}
