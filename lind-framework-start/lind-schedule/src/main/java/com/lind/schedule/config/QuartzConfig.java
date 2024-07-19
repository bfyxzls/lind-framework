package com.lind.schedule.config;

import com.lind.schedule.dynamic.DynamicSchedulerFactory;
import com.lind.schedule.service.QuartzService;
import com.lind.schedule.service.QuartzServiceImpl;
import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * Qartz配置, 搭配quartz.properties文件自定义配置, 该处目的使定时任务持久化
 */
@Configuration
public class QuartzConfig {

	/**
	 * 读取quartz.properties 文件 将值初始化
	 * @return
	 */
	@Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}

	/**
	 * 将配置文件的数据加载到SchedulerFactoryBean中
	 * @return
	 * @throws IOException
	 */
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setQuartzProperties(quartzProperties());
		schedulerFactoryBean.setAutoStartup(true);

		return schedulerFactoryBean;
	}

	/**
	 * dynamic scheduler factory 动态 定时任务 配置
	 */
	@Bean
	public DynamicSchedulerFactory dynamicSchedulerFactory(Scheduler scheduler) {
		DynamicSchedulerFactory schedulerFactory = new DynamicSchedulerFactory();
		schedulerFactory.setScheduler(scheduler);
		return schedulerFactory;
	}

	/**
	 * 初始化监听器
	 * @return
	 */
	@Bean
	public QuartzInitializerListener executorListener() {
		return new QuartzInitializerListener();
	}

	/**
	 * 获得Scheduler 对象
	 * @return
	 * @throws IOException
	 */
	@Bean
	public Scheduler scheduler() throws IOException {
		return schedulerFactoryBean().getScheduler();
	}

	/**
	 * 初始化业务逻辑
	 * @param scheduler
	 * @return
	 */
	@Bean
	public QuartzService quartzService(Scheduler scheduler) {
		return new QuartzServiceImpl(scheduler);
	}

}
