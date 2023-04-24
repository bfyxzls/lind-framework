package com.lind.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.lind.mybatis.plugins.LindPaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis plus 相关配置. register mapper.
 */
@Configuration
public class MybatisPlusConfig {

	/**
	 * 拦截器
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		// 分页插件, 对于单一数据库类型来说,都建议配置该值,避免每次分页都去抓取数据库类型
		interceptor.addInnerInterceptor(new LindPaginationInnerInterceptor());
		// 防止全表更新与删除
		interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
		return interceptor;
	}

	/**
	 * 审计字段自动填充
	 * @return {@link MetaObjectHandler}
	 */
	@Bean
	public MybatisPlusMetaObjectHandler mybatisPlusMetaObjectHandler() {
		return new MybatisPlusMetaObjectHandler();
	}

}
