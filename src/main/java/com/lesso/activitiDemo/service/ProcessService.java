package com.lesso.activitiDemo.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;

public interface ProcessService {
	
	Deployment deployProcess(String fileName);
	
	void startProcessByKey(String key, String userId);
	
	List<Task> getProcess(String userId);
	
	InputStream getPicture(String procInstId);

	void completeProcess(String taskId, String userId);

	Map<String, Object> getProcessInfo(String procId);
	

}
