package com.lind.activiti.event;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日志订阅.
 */
@Slf4j
@Component
public class LoggerEventListener implements org.activiti.engine.delegate.event.ActivitiEventListener {

	@Autowired
	RepositoryService repositoryService;

	@Override
	public void onEvent(ActivitiEvent event) {
		log.info("Task Complete[processDefinitionId:{},processInstanceId:{}]", event.getProcessDefinitionId(),
				event.getProcessInstanceId());
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}

}
