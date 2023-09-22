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
 * 在@Configuration注解中，proxyBeanMethods属性的默认值是true，如果省略了这个属性，则该注解会生成一个代理对象来创建@Bean方法中被注入的其他@Bean方法，以保证这些方法在容器中的单例模式。
 * 如果设置了proxyBeanMethods =
 * false，那么则会禁止使用CGLIB代理来调用@Bean方法中的其他@Bean方法，每次调用@Bean方法都会创建一个新的对象。这个属性可以提高应用程序的启动速度，特别是当应用程序具有许多@Configuration类和@Bean方法时。
 * 一般情况下，proxyBeanMethods =
 * true（默认值）是最好的选择，它可以确保所注入的@Bean方法是单例的。但当应用程序只是在运行时少量地进行动态的依赖注入时，可以考虑将proxyBeanMethods属性设置为false。
 */
@Configuration(proxyBeanMethods = true)
public class LindMybatisAutoConfiguration implements ApplicationContextAware {

	ApplicationContext applicationContext;

	/**
	 * 拦截器
	 */
	@Bean
	public MybatisPlusInterceptor lindMybatisPlusInterceptor() {
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
	public AuditFieldFillMetaObjectHandler lindMybatisPlusMetaObjectHandler() {
		return new AuditFieldFillMetaObjectHandler();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
