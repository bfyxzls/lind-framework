package com.lind.common.core.util;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 直接获取bean.
 */
@Component("springContextUtils")
public class SpringContextUtils implements BeanFactoryPostProcessor, ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext = null;

	/** Spring应用上下文环境 */
	private static ConfigurableListableBeanFactory beanFactory;

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 * 在其它配置类中使用它时，它有可能是空的，而beanFactory是有值的，所以获取bean时，我们使用beanFactory来进行获取.
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 实现ApplicationContextAware接口, 注入Context到静态变量中.
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextUtils.applicationContext = applicationContext;
	}

	/**
	 * 通过name获取 Bean.
	 * @param name .
	 * @return .
	 */
	public static <T> T getBean(String name) {
		return (T) beanFactory.getBean(name);
	}

	/**
	 * 通过class获取Bean.
	 * @param clazz .
	 * @param <T> .
	 * @return .
	 */
	public static <T> T getBean(Class<T> clazz) {
		return beanFactory.getBean(clazz);

	}

	/**
	 * 通过class获取Bean.
	 * @param clazz .
	 * @param <T> .
	 * @return .
	 */
	public static <T> List<T> getAllBeans(Class<T> clazz) {
		String[] names = beanFactory.getBeanNamesForType(clazz);
		List<T> list = new ArrayList<>();
		for (String name : names) {
			list.add((T) beanFactory.getBean(name));
		}
		return list;
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean.
	 * @param name .
	 * @param clazz .
	 * @param <T> .
	 * @return .
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return beanFactory.getBean(name, clazz);
	}

	/**
	 * 获取某个类型下所有bean.
	 * @param type
	 * @param <T>
	 * @return
	 */
	public static <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) {
		return beanFactory.getBeansOfType(type);
	}

	/**
	 * 得到所有bin的名称.
	 * @return
	 */
	public static String[] getAllBeanNames() {
		return beanFactory.getBeanDefinitionNames();
	}

	/**
	 * 发布事件.
	 * @param event
	 */
	public static void publishEvent(ApplicationEvent event) {
		if (applicationContext == null) {
			return;
		}
		applicationContext.publishEvent(event);
	}

	/**
	 * 获取aop代理对象
	 * @param invoker
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAopProxy(T invoker) {
		return (T) AopContext.currentProxy();
	}

	/**
	 * 获取当前的环境配置，无配置返回null
	 * @return 当前的环境配置
	 */
	public static String[] getActiveProfiles() {
		return applicationContext.getEnvironment().getActiveProfiles();
	}

	/**
	 * 获取当前的环境配置，当有多个环境配置时，只获取第一个
	 * @return 当前的环境配置
	 */
	public static String getActiveProfile() {
		final String[] activeProfiles = getActiveProfiles();
		return StringUtils.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
	}

	/**
	 * 获取配置文件中的值
	 * @param key 配置文件的key
	 * @return 当前的配置文件的值
	 *
	 */
	public static String getRequiredProperty(String key) {
		return applicationContext.getEnvironment().getRequiredProperty(key);
	}

	@Override
	public void destroy() throws Exception {
		applicationContext = null;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		SpringContextUtils.beanFactory = beanFactory;

	}

}
