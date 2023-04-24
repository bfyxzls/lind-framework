/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2015/4/23 22:56</create-date>
 *
 * <copyright file="CharTable.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2015, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.lind.common.util;

import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;

/**
 * 字符正规化表
 *
 * @author hankcs
 */
@Slf4j
public class CharTable {

	/**
	 * 正规化使用的对应表
	 */
	public char[] CONVERT;

	private File convertFile;

	public CharTable(File convertFile) {
		this.convertFile = convertFile;
	}

	public CharTable() {
		try {
			long start = System.currentTimeMillis();

			@Cleanup
			InputStream resourceAsStream = this.getClass().getResourceAsStream("/normalize/CharTable.txt.bin");
			ObjectInputStream in = new ObjectInputStream(resourceAsStream);
			CONVERT = (char[]) in.readObject();
			loadSpace();
			log.info("加载【默认】字符正规化表成功：" + (System.currentTimeMillis() - start) + " ms");

		}
		catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean load(File file) throws IOException {

		CONVERT = new char[Character.MAX_VALUE + 1];
		for (int i = 0; i < CONVERT.length; i++) {
			CONVERT[i] = (char) i;
		}

		Files.asCharSource(file, StandardCharsets.UTF_8).readLines(new LineProcessor<Object>() {
			@Override
			public boolean processLine(String line) throws IOException {

				if (StringUtils.isNotBlank(line) && line.length() == 3) {
					CONVERT[line.charAt(0)] = CONVERT[line.charAt(2)];
				}
				return true;
			}

			@Override
			public Object getResult() {
				return null;
			}
		});

		// 加载空格
		loadSpace();
		return true;
	}

	private void loadSpace() {
		for (int i = Character.MIN_CODE_POINT; i <= Character.MAX_CODE_POINT; i++) {
			if (Character.isWhitespace(i) || Character.isSpaceChar(i)) {
				CONVERT[i] = ' ';
			}
		}
	}

	/**
	 * 将一个字符正规化
	 * @param c 字符
	 * @return 正规化后的字符
	 */
	public char convert(char c) {
		return CONVERT[c];
	}

	public char[] convert(char[] charArray) {
		char[] result = new char[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			result[i] = CONVERT[charArray[i]];
		}

		return result;
	}

	public String convert(String sentence) {
		assert sentence != null;
		char[] result = new char[sentence.length()];
		convert(sentence, result);

		return new String(result);
	}

	public void convert(String charArray, char[] result) {
		for (int i = 0; i < charArray.length(); i++) {
			result[i] = CONVERT[charArray.charAt(i)];
		}
	}

	/**
	 * 正规化一些字符（原地正规化）
	 * @param charArray 字符
	 */
	public void normalization(char[] charArray) {
		assert charArray != null;
		for (int i = 0; i < charArray.length; i++) {
			charArray[i] = CONVERT[charArray[i]];
		}
	}

	/**
	 * 初始化
	 */
	public void init() {
		long start = System.currentTimeMillis();
		try {
			if (!load(convertFile)) {
				throw new IllegalArgumentException("字符正规化表加载失败");
			}
		}
		catch (IOException e) {
			throw new IllegalArgumentException("字符正规化表加载失败");
		}
		log.info("【自定义】字符正规化表加载成功：" + (System.currentTimeMillis() - start) + " ms");
	}

}
