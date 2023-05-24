package com.lind.mybatis.parser;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;

/**
 * 按id取模分表处理器
 *
 * @author lind
 * @date 2023/5/24 11:23
 * @since 1.0.0
 */
public class IdModTableNameParser implements TableNameHandler {

	// 使用ThreadLocal防止多线程相互影响
	private static ThreadLocal<Integer> id = new ThreadLocal<Integer>();

	// 模数，是指在多少个表中取模，如mod=10，表示在10个表中取模
	private Integer mod;

	IdModTableNameParser(Integer modValue) {
		mod = modValue;
	}

	public static void setId(Integer idValue) {
		id.set(idValue);
	}

	@Override
	public String dynamicTableName(String sql, String tableName) {
		Integer idValue = id.get();
		if (idValue == null) {
			throw new RuntimeException("请设置id值");
		}
		else {
			String suffix = String.valueOf(idValue % mod);
			// 这里清除ThreadLocal的值，防止线程复用出现问题
			id.set(null);
			return String.format("%s_%s", tableName, suffix);
		}
	}

}
