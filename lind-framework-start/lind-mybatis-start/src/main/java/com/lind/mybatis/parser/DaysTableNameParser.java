package com.lind.mybatis.parser;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 按日期生成的表名.
 *
 * @author lind
 * @date 2023/5/24 11:22
 * @since 1.0.0
 */
public class DaysTableNameParser implements TableNameHandler {

	@Override
	public String dynamicTableName(String sql, String tableName) {
		String dateDay = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		return String.format("%s_%s", tableName, dateDay);
	}

}
