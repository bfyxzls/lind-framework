package com.lind.activiti.config;

import com.lind.activiti.event.AssignRoleEventListener;
import com.lind.activiti.event.LoggerEventListener;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * springboot程序启动后要执行的事件.
 */
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

	private static Logger logger = LoggerFactory.getLogger(ApplicationReadyEventListener.class);

	@Autowired
	LoggerEventListener loggerEventListener;

	@Autowired
	AssignRoleEventListener assignRoleEventListener;

	@Autowired
	RuntimeService runtimeService;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		logger.debug("ApplicationReadyEventListener start...");
		// 添加TASK_CREATED触发时订阅的事件
		runtimeService.addEventListener(assignRoleEventListener, ActivitiEventType.TASK_CREATED);
		// 添加TASK_COMPLETED触发时订阅的事件
		runtimeService.addEventListener(loggerEventListener, ActivitiEventType.TASK_COMPLETED);
	}

}