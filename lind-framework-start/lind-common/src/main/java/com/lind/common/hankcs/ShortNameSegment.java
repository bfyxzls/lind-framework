package com.lind.common.hankcs;

import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author lind
 * @date 2023/2/27 16:44
 * @since 1.0.0
 */
public class ShortNameSegment {

	Logger logger = LoggerFactory.getLogger(ShortNameSegment.class);

	public static List<HitSentence> segForHit(char[] sentence, ShortNameDictionary shortNameDictionary) {

		// DoubleArrayTrie分词
		final int[] wordNet = new int[sentence.length];
		Arrays.fill(wordNet, 1);
		final Nature[][] natureArray = new Nature[sentence.length][5];// 最多不会超过五个词性

		HashMap<String, HitSentence> map = new HashMap<>();
		shortNameDictionary.parseText(sentence, new AhoCorasickDoubleArrayTrie.IHit<CoreDictionary.Attribute>() {
			@Override
			public void hit(int begin, int end, CoreDictionary.Attribute value) {
				int length = end - begin;
				if (length > wordNet[begin]) {
					wordNet[begin] = length;

					Nature[] natures = value.nature;

					for (int i = 0; i < natures.length; i++) {
						natureArray[begin][i] = value.nature[i];
					}
				}
				// System.out.printf("[%d:%d]=%s %s\n", begin, end, new String(sentence,
				// begin, end - begin), value);
				HitSentence hitSentence = new HitSentence();
				hitSentence.word = new String(sentence, begin, end - begin);
				hitSentence.begin = begin;
				hitSentence.end = end;
				hitSentence.natureSet = new HashSet<>(Arrays.asList(value.nature));
				String key = hitSentence.word + hitSentence.begin + hitSentence.end;
				if (map.containsKey(key)) {
					HitSentence h = map.get(key);
					if (h.natureSet == null) {
						h.natureSet = new HashSet<>();
					}
					h.natureSet.addAll(hitSentence.natureSet);
				}
				else {
					map.put(key, hitSentence);
				}

			}
		});

		Collection<HitSentence> values = map.values();
		List<HitSentence> list = new ArrayList<>(values);
		// Collections.sort(list, new Comparator<HitSentence>() {
		// @Override
		// public int compare(HitSentence o1, HitSentence o2) {
		//
		// if (o1.begin < o2.begin) {
		// return -1;
		// } else if (o1.begin == o2.begin) {
		// if (o1.end > o2.end) {
		// return -1;
		// } else if (o1.end < o2.end) {
		// return 1;
		// } else {
		// return 0;
		// }
		// } else {
		// return 1;
		// }
		// }
		// });

		return list;
	}

}
