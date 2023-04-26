package com.lind.activiti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lind.activiti.entity.ActReNode;
import com.lind.activiti.repository.ActReNodeRepository;
import com.lind.activiti.util.ActivitiHelper;
import com.lind.activiti.vo.ProcessDefinitionVo;
import com.lind.activiti.vo.ProcessNodeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 流程部署相关.
 */
@RestController
@Api("流程部署相关")
@Slf4j
public class DeploymentController {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	HistoryService historyService;

	@Autowired
	TaskService taskService;

	@Autowired
	ActivitiHelper activitiHelper;

	@Autowired
	ActReNodeRepository actReNodeRepository;

	/**
	 * 激活流程定义，通过/deployment/list来查看流程列表里的ACT_RE_PROCDEF.
	 * @param procDefId ACT_RE_PROCDEF.ID_
	 */
	@ApiOperation("激活流程定义")
	@RequestMapping(value = "/deployment/active/{procDefId}", method = RequestMethod.GET)
	public String active(@ApiParam("流程定义ID") @PathVariable String procDefId) {
		repositoryService.activateProcessDefinitionById(procDefId, true, new Date());
		return "激活成功";
	}

	/**
	 * 挂起.
	 * @param procDefId
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation("挂起")
	@RequestMapping(value = "/deployment/suspend/{procDefId}", method = RequestMethod.GET)
	public void deploymentDel(@ApiParam("流程定义ID") @PathVariable String procDefId, HttpServletResponse response)
			throws IOException {
		processEngine.getRepositoryService().suspendProcessDefinitionById(procDefId);
	}

	/**
	 * 导出部署流程资源.
	 * @param procDefId
	 * @param response
	 */
	@ApiOperation("导出部署流程资源")
	@RequestMapping(value = "/deployment/export", method = RequestMethod.GET)
	public void exportResource(@ApiParam("流程定义ID") @RequestParam String procDefId, HttpServletResponse response) {
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId)
				.singleResult();
		String resourceName = pd.getResourceName();
		InputStream inputStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

		try {
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + URLEncoder.encode(resourceName, "UTF-8"));
			byte[] b = new byte[1024];
			int len = -1;
			while ((len = inputStream.read(b, 0, 1024)) != -1) {
				response.getOutputStream().write(b, 0, len);
			}
			response.flushBuffer();
		}
		catch (IOException e) {
			log.error(e.toString());
			throw new ActivitiException("导出部署流程资源失败");
		}
	}

	/**
	 * 部署列表.
	 */
	@ApiOperation("部署列表")
	@RequestMapping(value = "/deployment/list", method = RequestMethod.GET)
	public Object deployment(org.springframework.ui.Model model,
			@ApiParam("状态") @RequestParam(required = false) String status,
			@ApiParam("页码") @RequestParam(required = false, defaultValue = "1") int pageindex,
			@ApiParam("每页记录数") @RequestParam(required = false, defaultValue = "10") int pagesize) {
		pageindex = (pageindex - 1) * pagesize;
		List<Deployment> list = processEngine.getRepositoryService().createDeploymentQuery().orderByDeploymenTime()
				.desc().listPage(pageindex, pagesize);
		List<Map<String, Object>> result = new ArrayList<>();

		for (Deployment item : list) {
			ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery()
					.deploymentId(item.getId()).singleResult();
			HashMap<String, Object> map = new HashMap<>();
			map.put("id", item.getId());
			map.put("time", item.getDeploymentTime());
			map.put("name", item.getName());
			map.put("key", processDefinition.getKey());
			map.put("proDefId", processDefinition.getId());
			map.put("isSuspended", processDefinition.isSuspended());
			if (StringUtils.isBlank(status)) {
				result.add(map);
			}
			else if (status.equals("1")) {
				if (!processDefinition.isSuspended())
					result.add(map);
			}
			else if (status.equals("2")) {
				if (processDefinition.isSuspended())
					result.add(map);
			}

		}
		return result;

	}

	/**
	 * 通过流程定义id获取流程节点.
	 * @param procDefId 流程定义ID
	 */
	@ApiOperation("通过流程定义id获取流程节点")
	@RequestMapping(value = "/deployment/node-list/{procDefId}", method = RequestMethod.GET)
	public Object getProcessNode(@ApiParam("流程定义ID") @PathVariable String procDefId,
			org.springframework.ui.Model model) {
		BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
		List<Process> processes = bpmnModel.getProcesses();
		List<ProcessNodeVo> processNodeVos = new ArrayList<>();
		for (Process process : processes) {
			Collection<FlowElement> elements = process.getFlowElements();
			for (FlowElement element : elements) {
				if (element instanceof UserTask) {
					ProcessNodeVo node = new ProcessNodeVo();
					node.setNodeId(element.getId());
					node.setTitle(element.getName());
					ActReNode actReNode = actReNodeRepository.findByNodeIdAndProcessDefId(element.getId(), procDefId);
					if (actReNode != null) {
						node.setRoleId(actReNode.getRoleId()); // 指定的角色
					}
					processNodeVos.add(node);
				}
			}
		}
		log.info("processNodeVos:{}", processNodeVos);
		return processNodeVos;
	}

	/**
	 * 节点配置保存.
	 * @param procDefId
	 * @param nodeId
	 * @param roleId 分配者角色
	 * @param defaultUserId 默认分配用户
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation("节点配置保存")
	@RequestMapping(value = "/deployment/node-save", method = RequestMethod.POST)
	public void saveProcessNode(@ApiParam("流程定义ID") @RequestParam String procDefId, @ApiParam("节点ID") String[] nodeId,
			@ApiParam("分配者角色") String[] roleId, @ApiParam("默认分配用户") String[] defaultUserId,
			@ApiParam("是否显示驳回") String[] reject, HttpServletResponse response) throws IOException {
		BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
		Process process = bpmnModel.getMainProcess(); // 获取主流程的，不考虑子流程
		for (int i = 0; i < nodeId.length; i++) {
			UserTask flowElement = (UserTask) process.getFlowElement(nodeId[i]);
			flowElement.setOwner(roleId[i]);
			flowElement.setAssignee(defaultUserId[i]);
			process.setValues(flowElement);// 数据只保存在内存里，需要添加节点分配数据表才能实现
			ActReNode actReNode = actReNodeRepository.findByNodeIdAndProcessDefId(nodeId[i], procDefId);
			if (actReNode == null) {
				actReNode = new ActReNode();
				actReNode.setId(UUID.randomUUID().toString());
				actReNode.setCreateTime(new Date());
			}
			actReNode.setNodeId(nodeId[i]);
			actReNode.setRoleId(roleId[i]);
			actReNode.setDefaultUserId(defaultUserId[i]);
			actReNode.setRejectFlag(Integer.parseInt(reject[i]));
			actReNode.setProcessDefId(procDefId);
			actReNodeRepository.save(actReNode);
		}
	}

	/**
	 * 节点配置保存并重定向.
	 * @param procDefId
	 * @param nodeId
	 * @param roleId
	 * @param reject
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation("节点配置保存并重定向")
	@RequestMapping(value = "/deployment/node-save-redirect", method = RequestMethod.POST)
	public void getProcessNodeRedirect(@ApiParam("流程定义ID") @RequestParam String procDefId,
			@ApiParam("节点ID") String[] nodeId, @ApiParam("分配者角色") String[] roleId,
			@ApiParam("默认分配用户") String[] defaultUserId, @ApiParam("是否显示驳回") String[] reject,
			HttpServletResponse response) throws IOException {
		saveProcessNode(procDefId, nodeId, roleId, defaultUserId, reject, response);
		response.sendRedirect("/view/deployment/list");
	}

	@ApiOperation("按模块的key获取当前被激活的流程定义ID")
	@RequestMapping(value = "/deployment/processDefinition", method = RequestMethod.GET)
	public ProcessDefinitionVo getProcessDefinitionByKey(String key) {
		ProcessDefinition deployment = processEngine.getRepositoryService().createProcessDefinitionQuery().active()
				.processDefinitionKey(key).singleResult();
		ProcessDefinitionVo processDefinitionVo = new ProcessDefinitionVo();
		processDefinitionVo.setCategory(deployment.getCategory());
		processDefinitionVo.setKey(deployment.getKey());
		processDefinitionVo.setName(deployment.getName());
		processDefinitionVo.setProDefId(deployment.getId());
		return processDefinitionVo;
	}

}
