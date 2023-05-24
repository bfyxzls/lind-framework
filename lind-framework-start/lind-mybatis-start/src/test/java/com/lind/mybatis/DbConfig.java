package com.lind.mybatis;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.lind.mybatis.parser.DaysTableNameParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author lind
 * @date 2023/5/24 13:49
 * @since 1.0.0
 */
@Configuration
public class DbConfig {

	@Bean
	public DynamicTableNameInnerInterceptor tableNamePlusInterceptor() {
		DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
		HashMap<String, TableNameHandler> map = new HashMap<String, TableNameHandler>();

		// 这里为不同的表设置对应表名处理器
		map.put("t_log", new DaysTableNameParser());

		dynamicTableNameInnerInterceptor.setTableNameHandlerMap(map);
		return dynamicTableNameInnerInterceptor;
	}

}
