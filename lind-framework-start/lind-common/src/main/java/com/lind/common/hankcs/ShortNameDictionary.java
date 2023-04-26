package com.lind.common.hankcs;

import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.collection.trie.bintrie.BinTrie;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.BaseSearcher;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author lind
 * 字典树（Trie树）是一种树形结构，是一种专门处理字符串匹配的数据结构。典型的应用场景是在一组字符串集合中快速查找某个字符串，也可以用于字符串的排序和搜索。
 *
 * 字典树的性质是，从根节点到某一个结点，路径上经过的字符连接起来，为该结点对应的字符串。字典树的根节点不包含字符，每个节点都只包含一个字符。除根节点外，每个节点可以有多个子节点，每个子节点代表一个字符，每个节点有一个父节点。
 *
 * Trie树的优点是：查找效率高，插入和删除效率也高，可以有效地存储大量字符串，它也可以高效地检索以特定前缀开头的字符串，支持模糊查找。
 * @date 2023/2/27 16:40
 * @since 1.0.0
 */
public class ShortNameDictionary {

	private static Logger logger = LoggerFactory.getLogger(ShortNameDictionary.class);

	/**
	 * 用于储存用户动态插入词条的二分trie树，这是基本的trie【字典树】
	 */
	public BinTrie<CoreDictionary.Attribute> trie;

	public boolean insert(String word, Nature nature) {
		logger.debug("简称字典 " + word);
		if (StringUtils.isBlank(word)) {
			return false;
		}
		if (trie == null)
			trie = new BinTrie<CoreDictionary.Attribute>();
		if (trie.containsKey(word)) {
			CoreDictionary.Attribute attribute = trie.get(word);
			Nature[] natures = attribute.nature;

			List<Nature> natureList = new ArrayList<>(Arrays.asList(natures));

			int i = natureList.indexOf(nature);

			if (i >= 0) {// 说明已经包含这个词性，就把词+1
				int fr = attribute.frequency[i];
				attribute.frequency[i] = fr + 1;
				trie.put(word, attribute);
				return true;

			}
			else {// 不包含这个词性
				natureList.add(nature);
				int[] frN = new int[attribute.frequency.length + 1];
				// 旧词频复制到新的数组中
				System.arraycopy(attribute.frequency, 0, frN, 0, attribute.frequency.length);
				frN[frN.length - 1] = 1;
				attribute.frequency = frN;

				Nature[] natureN = new Nature[natureList.size()];
				natureList.toArray(natureN);
				attribute.nature = natureN;
				trie.put(word, attribute);
				return true;
			}
		}
		else {
			CoreDictionary.Attribute att = new CoreDictionary.Attribute(nature, 1);
			trie.put(word, att);
		}
		return true;
	}

	/**
	 * 解析一段文本（目前采用了BinTrie形式，此方法可以统一两个数据结构）
	 * @param text 文本
	 * @param processor 处理器
	 */
	public void parseText(char[] text, AhoCorasickDoubleArrayTrie.IHit<CoreDictionary.Attribute> processor) {
		if (trie != null) {
			BaseSearcher searcher = getSearcher(text);
			int offset;
			Map.Entry<String, CoreDictionary.Attribute> entry;

			while ((entry = searcher.next()) != null) {
				offset = searcher.getOffset();
				processor.hit(offset, offset + entry.getKey().length(), entry.getValue());
			}
		}
	}

	/**
	 * 获取一个BinTrie的查询工具
	 * @param charArray 文本
	 * @return 查询者
	 */
	public BaseSearcher getSearcher(char[] charArray) {
		return new Searcher(charArray);
	}

	class Searcher extends BaseSearcher<CoreDictionary.Attribute> {

		/**
		 * 分词从何处开始，这是一个状态
		 */
		int begin;

		private LinkedList<Map.Entry<String, CoreDictionary.Attribute>> entryList;

		protected Searcher(char[] c) {
			super(c);
			entryList = new LinkedList<Map.Entry<String, CoreDictionary.Attribute>>();
		}

		@Override
		public Map.Entry<String, CoreDictionary.Attribute> next() {

			if (trie == null) {
				return null;
			}

			// 保证首次调用找到一个词语
			while (entryList.size() == 0 && begin < c.length) {
				entryList = trie.commonPrefixSearchWithValue(c, begin);
				++begin;
			}
			// 之后调用仅在缓存用完的时候调用一次
			if (entryList.size() == 0 && begin < c.length) {
				entryList = trie.commonPrefixSearchWithValue(c, begin);
				++begin;
			}
			if (entryList.size() == 0) {
				return null;
			}
			Map.Entry<String, CoreDictionary.Attribute> result = entryList.getFirst();
			entryList.removeFirst();
			offset = begin - 1;
			return result;
		}

	}

}
