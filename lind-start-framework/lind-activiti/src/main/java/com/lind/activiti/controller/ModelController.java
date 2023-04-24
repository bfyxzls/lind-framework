package com.lind.activiti.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lind.activiti.repository.ActReNodeRepository;
import com.lind.activiti.util.ActivitiHelper;
import com.lind.activiti.util.Constant;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * 模型相关.
 */
@RestController
@Slf4j
public class ModelController {

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
	 * 建立页面，同时也保存.
	 */
	@ApiOperation("建立工作流模型")
	@GetMapping("/model/create")
	public void createModel(HttpServletRequest request, HttpServletResponse response) {
		try {

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.putPOJO("stencilset", stencilSetNode);

			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, Constant.modelName);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, Constant.description);
			Model modelData = repositoryService.newModel();
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(Constant.modelName);
			modelData.setKey(Constant.modelKey);

			// 保存模型
			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(),
					editorNode.toString().getBytes(StandardCharsets.UTF_8));
			response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
		}
		catch (Exception e) {
			e.getStackTrace();
		}
	}

	/**
	 * 删除模型.
	 * @param modelId
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation("删除模型")
	@GetMapping("/model/delete")
	public void delModel(String modelId, HttpServletResponse response) throws IOException {
		repositoryService.deleteModel(modelId);
	}

	/**
	 * 模型部署成为流程.
	 */
	@ApiOperation("模型部署成为流程")
	@RequestMapping(value = "/model/deploy/{id}", method = RequestMethod.GET)
	public void deploy(@PathVariable String id, HttpServletResponse response) throws IOException {

		// 获取模型
		Model modelData = repositoryService.getModel(id);
		byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());

		if (bytes == null) {
			throw new IllegalArgumentException("模型数据为空，请先成功设计流程并保存");
		}

		try {
			JsonNode modelNode = new ObjectMapper().readTree(bytes);
			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			if (model.getProcesses().size() == 0) {
				throw new IllegalArgumentException("模型不符要求，请至少设计一条主线流程");
			}
			byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
			// 部署发布模型流程
			String processName = modelData.getName() + ".bpmn20.xml";
			Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
					.addString(processName, new String(bpmnBytes, StandardCharsets.UTF_8)).deploy();

			// 设置流程分类 保存扩展流程至数据库
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
					.deploymentId(deployment.getId()).list();

			for (ProcessDefinition pd : list) {
				log.info(pd.getName());
			}
		}
		catch (Exception e) {
			log.error(e.toString());
			throw new IllegalArgumentException(e.getMessage());
		}

	}

	/**
	 * 模模型列表.
	 */
	@ApiOperation("模模型列表")
	@RequestMapping(value = "/model/list", method = RequestMethod.GET)
	public Object modelist(org.springframework.ui.Model model,
			@RequestParam(required = false, defaultValue = "1") int pageindex,
			@RequestParam(required = false, defaultValue = "10") int pagesize) {
		pageindex = (pageindex - 1) * pagesize;
		List<org.activiti.engine.repository.Model> list = processEngine.getRepositoryService().createModelQuery()
				.orderByCreateTime().desc().listPage(pageindex, pagesize);

		return list;
	}

	/**
	 * 通过文件生成模型.
	 * @param file
	 */
	@ApiOperation("通过文件生成模型")
	@PostMapping(value = "deployByFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void deployByFile(@RequestPart("file") MultipartFile file, HttpServletResponse response) throws IOException {
		String fileName = file.getOriginalFilename();
		if (StringUtils.isBlank(fileName)) {
			return;
		}
		try {
			InputStream fileInputStream = file.getInputStream();
			Deployment deployment;
			String extension = FilenameUtils.getExtension(fileName);
			String baseName = FilenameUtils.getBaseName(fileName);
			if ("zip".equals(extension) || "bar".equals(extension)) {
				ZipInputStream zip = new ZipInputStream(fileInputStream);
				deployment = repositoryService.createDeployment().name(baseName).addZipInputStream(zip).deploy();
			}
			else if ("png".equals(extension)) {
				deployment = repositoryService.createDeployment().name(baseName)
						.addInputStream(fileName, fileInputStream).deploy();
			}
			else if (fileName.indexOf("bpmn20.xml") != -1) {
				deployment = repositoryService.createDeployment().name(baseName)
						.addInputStream(fileName, fileInputStream).deploy();
			}
			else if ("bpmn".equals(extension)) {
				deployment = repositoryService.createDeployment().name(baseName)
						.addInputStream(baseName + ".bpmn20.xml", fileInputStream).deploy();
			}
			else {
				throw new IllegalArgumentException("文件格式不支持");
			}
			ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery()
					.deploymentId(deployment.getId()).singleResult();
			convertToModel(processDefinition.getId(), response);

		}
		catch (Exception e) {
			log.error(e.toString());
		}
	}

	/**
	 * 导入再重定向.
	 * @param file
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation("导入再重定向")
	@PostMapping(value = "deploy-file-redirect", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void deployByFileRedirect(@RequestPart("file") MultipartFile file, HttpServletResponse response)
			throws IOException {
		deployByFile(file, response);
		response.sendRedirect("/view/model/list");
	}

	/**
	 * 转化流程为模型.
	 * @param id
	 * @return
	 */
	@ApiOperation("转化流程为模型")
	@RequestMapping(value = "/deployment/convertToModel/{id}", method = RequestMethod.GET)
	public void convertToModel(@PathVariable String id, HttpServletResponse response) throws IOException {

		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
		InputStream bpmnStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), pd.getResourceName());

		try {
			XMLInputFactory xif = XMLInputFactory.newInstance();
			InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
			XMLStreamReader xtr = xif.createXMLStreamReader(in);
			BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
			BpmnJsonConverter converter = new BpmnJsonConverter();

			ObjectNode modelNode = converter.convertToJson(bpmnModel);
			org.activiti.engine.repository.Model modelData = repositoryService.newModel();
			modelData.setKey(pd.getKey());
			modelData.setName(pd.getResourceName());

			ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, pd.getName());
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, pd.getDescription());
			modelData.setMetaInfo(modelObjectNode.toString());

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
		}
		catch (Exception e) {
			log.error(e.toString());
			throw new IllegalArgumentException("转化流程为模型失败");
		}
	}

}
