package com.lind.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.lind.mybatis.plugins.LindPaginationInnerInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * mybatis plus 相关配置. register mapper.
 */
@Configuration
public class MybatisPlusConfig implements ApplicationContextAware {

	ApplicationContext applicationContext;

	/**
	 * 拦截器
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		// 分页插件, 对于单一数据库类型来说,都建议配置该值,避免每次分页都去抓取数据库类型
		PaginationInnerInterceptor lindPaginationInnerInterceptor = new LindPaginationInnerInterceptor();
		// 设置最大单页限制数量，默认 500 条，-1 不受限制
		lindPaginationInnerInterceptor.setMaxLimit(500L);
		lindPaginationInnerInterceptor.setDbType(DbType.MYSQL);
		// 开启 count 的 join 优化,只针对部分 left join
		lindPaginationInnerInterceptor.setOptimizeJoin(true);
		// 注册分页插件
		interceptor.addInnerInterceptor(lindPaginationInnerInterceptor);
		// 防止全表更新与删除
		interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
		// 加载个性化的分表配置，它可能是用户在当前项目定义的，然后我们统一对它们进行装配
		Optional.ofNullable(applicationContext.getBeanNamesForType(DynamicTableNameInnerInterceptor.class))
				.ifPresent(o -> {
					for (String beanName : o) {
						DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = applicationContext
								.getBean(beanName, DynamicTableNameInnerInterceptor.class);
						interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
					}
				});
		return interceptor;
	}

	/**
	 * 审计字段自动填充
	 * @return {@link MetaObjectHandler}
	 */
	@Bean
	public AuditFieldFillMetaObjectHandler mybatisPlusMetaObjectHandler() {
		return new AuditFieldFillMetaObjectHandler();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
