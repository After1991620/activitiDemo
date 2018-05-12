package com.lesso.activitiDemo.service.impl;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lesso.activitiDemo.service.ProcessService;

@Service
public class ProcessServiceImpl implements ProcessService {

	private static final Logger log = LoggerFactory.getLogger(ProcessServiceImpl.class);

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private HistoryService historyService;

	@Override
	public Deployment deployProcess(String fileName) {
		Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/" + fileName).deploy();
		log.info("Number of process definitions: {}", repositoryService.createProcessDefinitionQuery().count());
		return deploy;
	}

	@Override
	public void startProcessByKey(String key, String userId) {
		Map<String, Object> variables = new HashMap<String, Object>();
		User user = identityService.createUserQuery().userId(userId).singleResult();
		variables.put("inputUser", user.getFirstName());
		variables.put("numberOfDays", new Integer(4));
		variables.put("reason", "就是不想上班");
		runtimeService.startProcessInstanceByKey(key, variables);
		
	}

	@Override
	public List<Task> getProcess(String userId) {
		User user = identityService.createUserQuery().userId(userId).singleResult();
		List<Task> list = taskService.createTaskQuery().taskAssignee(user.getFirstName()).list();
		return list;
	}

	@Override
	public InputStream getPicture(String procInstId) {
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
		List<String> activityIds = runtimeService.getActiveActivityIds(pi.getId());
		BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
		ProcessDiagramGenerator p = new DefaultProcessDiagramGenerator();
		InputStream is = p.generateDiagram(bpmnModel, "png", activityIds, Collections.<String> emptyList(), "宋体", "宋体",
				"宋体", null, 1.0);
		return is;
	}

	@Override
	public void completeProcess(String taskId, String userId) {
		Map<String, Object> variables = new HashMap<String, Object>();
		User user = identityService.createUserQuery().userId(userId).singleResult();
		variables.put("inputUser", user.getFirstName());
		taskService.complete(taskId, variables);
	}

	@Override
	public Map<String, Object> getProcessInfo(String procId) {
		List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(procId).list();
		Map<String, Object> result = new LinkedHashMap<>();
		for (HistoricVariableInstance hvi : list) {
			result.put(hvi.getVariableName(), hvi.getValue());
		}
		return result;
	}


}
