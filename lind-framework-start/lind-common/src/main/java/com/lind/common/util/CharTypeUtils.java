/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/12/5 15:37</create-date>
 *
 * <copyright file="CharType.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.lind.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 字符类型
 *
 * @author hankcs
 */
@Slf4j
public class CharTypeUtils {

	public static byte[] typeArray;

	static {
		typeArray = new byte[65536];
		log.info("字符类型对应表开始加载 ");
		long start = System.currentTimeMillis();
		int preType = 5;
		int preChar = 0;
		for (int i = 0; i <= Character.MAX_VALUE; ++i) {
			int type = TextUtility.charType((char) i);
			if (type != preType) {

				for (int j = preChar; j <= i - 1; ++j) {
					typeArray[j] = (byte) preType;
				}
				preChar = i;
			}
			preType = type;
		}
		{
			for (int j = preChar; j <= Character.MAX_VALUE; ++j) {
				typeArray[j] = (byte) preType;
			}
		}
		log.info("字符类型对应表加载成功，耗时" + (System.currentTimeMillis() - start) + " ms");

	}

	/**
	 * 获取字符的类型
	 * @param c
	 * @return
	 */
	public static byte get(char c) {
		return typeArray[(int) c];
	}

	/**
	 * 设置字符类型
	 * @param c 字符
	 * @param t 类型
	 */
	public static void set(char c, byte t) {
		typeArray[c] = t;
	}

}
