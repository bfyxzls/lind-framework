package com.lind.activiti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.lind.activiti.config.ActivitiConfig;
import com.lind.activiti.entity.ActReNode;
import com.lind.activiti.repository.ActReNodeRepository;
import com.lind.activiti.util.ActivitiHelper;
import com.lind.activiti.util.Constant;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 流程实例和任务相关.
 */
@RestController
@Slf4j
public class ExecutionController {

	public static final String FATHER_SPLIT = "-";

	public static final String SON_SPLIT = "_";

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

	@Autowired
	ProcessEngineConfiguration processEngineConfiguration;

	@Autowired
	ActivitiConfig.ActivitiExtendProperties activitiExtendProperties;

	private static int longCompare(Date obj1, Date obj2) {
		return obj1.compareTo(obj2);
	}

	/**
	 * 启动流程实例 数据在ACT_RU_TASK和ACT_RU_JOB和ACT_RU_EXECUTION表生成记录. /execution/list接口可以获取到数据
	 * @param procDefId act_re_procdef.ID_
	 */
	@ApiOperation("启动流程实例")
	@RequestMapping(value = "/execution/start/{procDefId}", method = RequestMethod.GET)
	public void createInstance(@ApiParam("流程定义ID") @PathVariable String procDefId,
			@ApiParam("标题") @RequestParam String title, HttpServletResponse response) throws IOException {
		// 启动流程
		ProcessInstance pi = runtimeService.startProcessInstanceById(procDefId);
		// 设置流程实例名称
		runtimeService.setProcessInstanceName(pi.getId(), title);
	}

	/**
	 * 任务强制完成
	 * @param id
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation("任务强制完成")
	@RequestMapping(value = "/task/complete/{id}", method = RequestMethod.GET)
	public void taskComplete(@ApiParam("任务ID") @PathVariable String id, HttpServletResponse response)
			throws IOException {
		taskService.complete(id);
	}

	/**
	 * 流程实例的任务的审批，需要通过下一节点配置的组选择审核人
	 * @param procInstId 流程实例ID ACT_RU_TASK.PROC_INST_ID_
	 * @param taskId 任务ID ACT_RU_TASK.ID_
	 * @param assignee 前端传过来的分配人,为空表示自己
	 * @param comment 备注
	 */
	@ApiOperation("流程审批")
	@RequestMapping(value = "/execution/pass/{procInstId}/{taskId}", method = RequestMethod.GET)
	public void pass(@ApiParam("流程实例ID") @PathVariable String procInstId, @ApiParam("任务ID") @PathVariable String taskId,
			@ApiParam("分配用户") @RequestParam(required = false) String assignee,
			@ApiParam("审核备注") @RequestParam(required = false) String comment,
			@ApiParam("其它参数") @RequestParam(required = false) String params, HttpServletResponse response)
			throws Exception {

		if (StringUtils.isBlank(comment)) {
			comment = "";
		}
		taskService.addComment(taskId, procInstId, comment);
		Map map = new HashMap();
		map.put(Constant.assignee, assignee);
		if (StringUtils.isNoneBlank(params)) {
			String[] keys = StringUtils.split(params, FATHER_SPLIT);
			for (String val : keys) {
				String[] sonVal = StringUtils.split(val, SON_SPLIT);

				if (sonVal[0].equals(Constant.meeting)) {
					// 会签操作,需要算出会签人员数,人员分配在AssignedEventListener事件里完成
					ProcessInstance pi = runtimeService.createProcessInstanceQuery() // 根据流程实例id获取流程实例
							.processInstanceId(procInstId).singleResult();
					BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
					TaskDefinition taskDefinition = activitiHelper.getNextTaskInfo(procInstId);
					UserTask userTask = (UserTask) bpmnModel.getMainProcess().getFlowElement(taskDefinition.getKey());
					if (StringUtils.isBlank(userTask.getOwner())) {
						throw new ActivitiException("需要为节点指定角色");
					}
					List<String> assignees = Arrays.asList(StringUtils.split(assignee, ","));
					map.put(Constant.meeting, assignees);
				}
				else {
					// 普通操作
					map.put(sonVal[0], sonVal[1]);
				}
			}
		}

		taskService.complete(taskId, map);
	}

	/**
	 * 任务节点审批驳回.
	 * @param procInstId
	 * @param taskId
	 * @param comment
	 * @param destTaskKey 驳回到的节点
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation("审批驳回")
	@RequestMapping(value = "/execution/back/{procInstId}/{taskId}", method = RequestMethod.GET)
	public void back(@ApiParam("流程实例ID") @PathVariable String procInstId, @ApiParam("任务ID") @PathVariable String taskId,
			@ApiParam("审批备注") @RequestParam(required = false) String comment,
			@ApiParam("驳回节点名称") @RequestParam(required = false) String destTaskKey, HttpServletResponse response)
			throws IOException {
		if (comment == null) {
			comment = "";
		}
		Task taskEntity = taskService.createTaskQuery().taskId(taskId).singleResult();
		// 获得当前任务的流程实例ID
		String processInstanceId = taskEntity.getProcessInstanceId();

		// 获得当前任务的流程定义ID
		String processDefinitionId = taskEntity.getProcessDefinitionId();
		// 当前任务key
		String currtaskDefKey = taskEntity.getTaskDefinitionKey();

		// 获得当前活动节点和驳回的目标节点"draft"
		ActivityImpl currActiviti = null;// 当前活动节点
		ActivityImpl destActiviti = null;// 驳回目标节点

		currActiviti = getActivityImpl(currtaskDefKey, processDefinitionId);
		destActiviti = getActivityImpl(destTaskKey, processDefinitionId);

		// 保存当前活动节点的流程流出参数
		List<PvmTransition> hisPvmTransitionList = new ArrayList<PvmTransition>(0);

		for (PvmTransition pvmTransition : currActiviti.getOutgoingTransitions()) {
			hisPvmTransitionList.add(pvmTransition);
		}
		// 清空当前活动节点的所有流出项

		currActiviti.getOutgoingTransitions().clear();
		// 为当前节点动态创建新的流出项

		TransitionImpl newTransitionImpl = currActiviti.createOutgoingTransition();
		// 为当前活动节点新的流出项指定为目标流程节点
		newTransitionImpl.setDestination(destActiviti);
		// 保存驳回意见
		taskEntity.setDescription(comment);// 设置驳回意见
		taskService.saveTask(taskEntity);

		/**
		 * 注意：添加批注的时候，由于Activiti底层代码是使用： String userId =
		 * Authentication.getAuthenticatedUserId(); CommentEntity comment = new
		 * CommentEntity(); comment.setUserId(userId);
		 * 所有需要从Session中获取当前登录人，作为该任务的办理人（审核人），对应act_hi_comment表中的User_ID的字段，
		 * 不添加审核人，该字段为null 所以要求，添加配置执行使用Authentication.setAuthenticatedUserId();添加当前任务的审核人
		 */
		Authentication.setAuthenticatedUserId("1");
		taskService.addComment(taskId, processInstanceId, comment);

		// 设定驳回标志
		Map<String, Object> variables = new HashMap<String, Object>(0);
		// variables.put("outcome", outcome);
		// 执行当前任务驳回到目标任务draft
		taskEntity.setDescription("backFlow");// 设置驳回意见
		taskService.saveTask(taskEntity);
		// 设置审批人为当前审批人，保证前台展示审批人为一个人
		taskService.setAssignee(taskId, "1");
		taskService.complete(taskEntity.getId(), variables);
		// 清除目标节点的新流入项
		destActiviti.getIncomingTransitions().remove(newTransitionImpl);
		// 清除原活动节点的临时流程项
		currActiviti.getOutgoingTransitions().clear();
		// 还原原活动节点流出项参数
		currActiviti.getOutgoingTransitions().addAll(hisPvmTransitionList);

	}

	/**
	 * @return
	 * @Description (通过任务key, 获取对应的节点信息)
	 * @author feizhou
	 * @Date 2018年3月28日下午1:53:29
	 * @version 1.0.0
	 */
	public ActivityImpl getActivityImpl(String destTaskKey, String processDefinitionId) {
		// 获得当前流程的定义模型
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);

		// 获得当前流程定义模型的所有任务节点

		List<ActivityImpl> activitilist = processDefinition.getActivities();
		// 获得当前活动节点和驳回的目标节点"draft"
		ActivityImpl descActiviti = null;// 当前活动节点

		for (ActivityImpl activityImpl : activitilist) {
			// 获取节点对应的key
			String taskKey = activityImpl.getId();
			// 确定当前活动activiti节点
			if (destTaskKey.equals(taskKey)) {
				descActiviti = activityImpl;
				break;
			}
		}
		return descActiviti;
	}

	/**
	 * 获取当前节点的下一节点信息，前台需要它返回的role，来获取role下面的用户.
	 * @param procInstId
	 * @return
	 */
	@ApiOperation("获取当前节点的下一节点信息")
	@RequestMapping(value = "/process/next-node/{procInstId}", method = RequestMethod.GET)
	public Object getNextNode(@ApiParam("流程实例ID") @PathVariable String procInstId) throws Exception {
		TaskDefinition taskDefinition = activitiHelper.getNextTaskInfo(procInstId);
		String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult()
				.getProcessDefinitionId();

		if (null != taskDefinition) {
			ActReNode actReNode = actReNodeRepository.findByNodeIdAndProcessDefId(taskDefinition.getKey(),
					definitionId);
			if (actReNode == null) {
				throw new ActivitiException("请为节点" + taskDefinition.getKey() + "配置角色");
			}
			return ImmutableMap.of("end", false, "id", taskDefinition.getKey(), "name",
					taskDefinition.getNameExpression().getExpressionText(), "role", actReNode.getRoleId(),
					"defaultUserId", actReNode.getDefaultUserId());
		}
		return ImmutableMap.of("end", true);
	}

	/**
	 * 根据任务id查询已经执行的任务节点信息
	 */
	List<Map<String, String>> getRunNodes(String taskId) {
		// 获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
		List<HistoricActivityInstance> historicActivityInstanceList = historyService
				.createHistoricActivityInstanceQuery().processInstanceId(taskId).activityType("userTask") // 用户任务
				.finished() // 已经执行的任务节点
				.orderByHistoricActivityInstanceEndTime().asc().list();
		List<Map<String, String>> list = new ArrayList<>();
		// 已执行的节点ID集合
		if (CollectionUtils.isNotEmpty(historicActivityInstanceList)) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
				if (!map.containsKey(historicActivityInstance.getActivityId())) {
					map.put(historicActivityInstance.getActivityId(), historicActivityInstance.getActivityName());
					list.add(map);
				}
			}
		}
		return list;
	}

	/**
	 * 当前运行中的流程实例列表，应该是启动了的流程（/execution/start/会出现的流程）.
	 */
	@ApiOperation("运行中的流程实例列表")
	@RequestMapping(value = "/execution/list", method = RequestMethod.GET)
	public Object execution(org.springframework.ui.Model model,
			@RequestParam(required = false, defaultValue = "1") int pageindex,
			@RequestParam(required = false, defaultValue = "10") int pagesize) {
		pageindex = (pageindex - 1) * pagesize;
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().orderByProcessInstanceId().desc()
				.listPage(pageindex, pagesize);
		List<Map<String, Object>> result = new ArrayList<>();
		for (ProcessInstance item : list) {
			List<Task> tasks = taskService.createTaskQuery().active().processInstanceId(item.getId()).list();// 并行网关可能是多条任务
			for (Task task : tasks) {

				String owner = task.getOwner() == null ? "" : task.getOwner();
				String assignee = task.getAssignee() == null ? "" : task.getAssignee();
				result.add(new ImmutableMap.Builder<String, Object>().put("id", item.getId())
						.put("proDefId", item.getProcessDefinitionId()).put("isSuspended", item.isSuspended())
						.put("executionId", task.getExecutionId()).put("taskId", task.getId())
						.put("taskName", task.getName()).put("time", task.getCreateTime()).put("owner", owner)
						.put("assignee", assignee).build());
			}
		}
		result = result.stream().sorted((i, j) -> longCompare((Date) j.get("time"), (Date) i.get("time")))
				.collect(Collectors.toList());

		return result;
	}

	/**
	 * 已完成的历史记录列表.
	 */
	@ApiOperation("已完成的任务列表")
	@RequestMapping(value = "/task/list/{id}", method = RequestMethod.GET)
	public Object taskList(@PathVariable String id, org.springframework.ui.Model model) {
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(id)
				.finished().orderByTaskCreateTime().desc().list();
		return list;
	}

	/**
	 * 正在被处理的任务.
	 */
	@ApiOperation("正在被处理的任务")
	@RequestMapping(value = "/execution/getRunningProcess", method = RequestMethod.GET)
	public Object getRunningProcess(@RequestParam(required = false) String name,
			@RequestParam(required = false) String categoryId, @RequestParam(required = false) String key) {

		ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery().orderByProcessInstanceId().desc();

		if (StringUtils.isNotBlank(name)) {
			query.processInstanceNameLike("%" + name + "%");
		}
		if (StringUtils.isNotBlank(categoryId)) {
			query.processDefinitionCategory(categoryId);
		}
		if (StringUtils.isNotBlank(key)) {
			query.processDefinitionKey(key);
		}
		List<ProcessInstance> processInstanceList = query.listPage(1, 10);
		return processInstanceList;
	}

	/**
	 * 第一个流程节点.
	 * @param procDefId act_re_procdef.ID_
	 */
	@ApiOperation("第一个流程节点")
	@RequestMapping(value = "/execution/getFirstNode/{procDefId}", method = RequestMethod.GET)
	public Object getFirstNode(@PathVariable String procDefId) {
		BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
		List<Process> processes = bpmnModel.getProcesses();
		Collection<FlowElement> elements = processes.get(0).getFlowElements();
		// 流程开始节点
		StartEvent startEvent = null;
		for (FlowElement element : elements) {
			if (element instanceof StartEvent) {
				startEvent = (StartEvent) element;
				break;
			}
		}
		FlowElement e = null;
		// 判断开始后的流向节点
		SequenceFlow sequenceFlow = startEvent.getOutgoingFlows().get(0);
		for (FlowElement element : elements) {
			if (element.getId().equals(sequenceFlow.getTargetRef())) {
				if (element instanceof UserTask) {
					return element;
				}
				else {
					throw new IllegalArgumentException("流程设计错误，开始节点后只能是用户任务节点");
				}
			}
		}

		return null;
	}

	/**
	 * 获取流程图片.
	 * @param procInstId 实例ID
	 * @param response
	 */
	@ApiOperation("获取流程图片")
	@RequestMapping(value = "/execution/getHighlightImg/{procInstId}", method = RequestMethod.GET)
	public void getHighlightImg(@PathVariable String procInstId, HttpServletResponse response) {
		Map<String, Object> result = getInputStream(procInstId);
		InputStream inputStream = (InputStream) result.get("inputStream");
		String picName = (String) result.get("name");

		OutputStream o;
		try {
			response.setContentType("application/octet-stream;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(picName, "UTF-8"));
			o = response.getOutputStream();
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buf)) > 0) {
				o.write(buf, 0, bytesRead);
				o.flush();
			}
			inputStream.close();
			o.close();
			response.flushBuffer();
		}
		catch (IOException e) {
			log.error(e.toString());
			throw new IllegalArgumentException("读取流程图片失败");
		}
	}

	/**
	 * 输出图像.
	 * @param procInstId 实例ID
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ApiOperation("输出图像")
	@RequestMapping(value = "/execution/getActivitiImg/{procInstId}", method = RequestMethod.GET,
			produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] getActivitiImg(@PathVariable String procInstId, HttpServletResponse response) throws IOException {

		InputStream inputStream = (InputStream) getInputStream(procInstId).get("inputStream");
		byte[] bytes = new byte[inputStream.available()];
		inputStream.read(bytes, 0, inputStream.available());
		return bytes;
	}

	private Map<String, Object> getInputStream(String id) {
		InputStream inputStream;
		ProcessInstance pi;
		String picName;
		// 查询历史
		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceId(id)
				.singleResult();
		if (hpi != null && hpi.getEndTime() != null) {
			// 已经结束流程获取原图
			ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(hpi.getProcessDefinitionId()).singleResult();
			inputStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), pd.getDiagramResourceName());
			picName = pd.getDiagramResourceName();
		}
		else {
			pi = runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
			BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
			List<String> highLightedActivities = new ArrayList<String>();
			// 高亮任务节点
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(id).list();
			for (Task task : tasks) {
				highLightedActivities.add(task.getTaskDefinitionKey());
			}

			List<String> highLightedFlows = new ArrayList<String>();
			ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
			inputStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivities, highLightedFlows,
					activitiExtendProperties.getActivityFontName(), activitiExtendProperties.getLabelFontName(),
					activitiExtendProperties.getLabelFontName(), null, 1.0);
			picName = pi.getName() + ".png";
		}
		Map<String, Object> result = new HashMap<>();
		result.put("name", picName);
		result.put("inputStream", inputStream);
		return result;
	}

	/**
	 * 删除任务.
	 */
	@ApiOperation("删除任务")
	@RequestMapping(value = "/execution/delete/{ids}", method = RequestMethod.DELETE)
	public Object delete(@PathVariable String[] ids, @RequestParam(required = false) String reason) {

		if (StringUtils.isBlank(reason)) {
			reason = "";
		}
		for (String id : ids) {
			taskService.deleteTask(id, reason);
		}
		return "success";
	}

	/**
	 * 删除任务历史.
	 */
	@ApiOperation("删除任务历史")
	@RequestMapping(value = "/execution/deleteHistoric/{ids}", method = RequestMethod.DELETE)
	public Object deleteHistoric(@PathVariable String[] ids) {

		for (String id : ids) {
			historyService.deleteHistoricTaskInstance(id);
		}
		return "success";
	}

	/**
	 * 通过id删除运行中的实例.
	 */
	@ApiOperation("通过id删除运行中的实例")
	@RequestMapping(value = "/execution/delInsByIds/{ids}", method = RequestMethod.GET)
	public Object delInsByIds(@PathVariable String[] ids, @RequestParam(required = false) String reason) {

		if (StringUtils.isBlank(reason)) {
			reason = "";
		}
		for (String id : ids) {
			// 关联业务状态结束
			ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
			runtimeService.deleteProcessInstance(id, reason);
		}
		return "success";
	}

	/**
	 * 当前任务列表.
	 */
	@ApiOperation("当前任务列表")
	@RequestMapping(value = "/task/list", method = RequestMethod.GET)
	public Object tasks() {
		List<Task> list = taskService.createTaskQuery().orderByExecutionId().desc().list();
		List<Map<String, Object>> result = new ArrayList<>();
		for (Task item : list) {
			log.info("task.id={},proc_inst_id={},proc_def_id={}", item.getId(), item.getProcessInstanceId(),
					item.getProcessDefinitionId());
			result.add(ImmutableMap.of("id", item.getId(), "procInstId", item.getProcessInstanceId(), "procDefId",
					item.getProcessDefinitionId()));
		}

		return result;
	}

	/**
	 * 历史记录列表.
	 */
	@ApiOperation("历史记录列表")
	@RequestMapping(value = "/history/finished-list", method = RequestMethod.GET)
	public List<HistoricTaskInstance> historyList(org.springframework.ui.Model model) {
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().finished()
				.orderByTaskCreateTime().desc().list();
		return list;
	}

}
