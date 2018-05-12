package com.lesso.activitiDemo.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lesso.activitiDemo.service.ProcessService;

@RestController
@RequestMapping("process")
public class ProcessController {
	
	private final String suffix = ".bpmn";
	
	@Autowired
	private ProcessService processService;
	
	@PostMapping
	public Object deployProcess(String fileName) {
		
		Deployment deployProcess = processService.deployProcess(fileName + suffix);
//		Map<String, Object> resultMap = new LinkedHashMap<String, Object>(){
//			private static final long serialVersionUID = 1L;
//			{
//				put("id",deployProcess.getId());
//				put("name",deployProcess.getName());
//				put("deploymentTime",deployProcess.getDeploymentTime());
//				put("category",deployProcess.getCategory());
//				put("key",deployProcess.getKey());
//				put("tenantId",deployProcess.getTenantId());
//			}
//		};
		return deployProcess;
	}
	
	@PutMapping
	public void startProcess(String key, String userId) {
		processService.startProcessByKey(key, userId);
	}
	
	@GetMapping("picture")
	public void getPicture(HttpServletResponse response, @RequestParam String procInstId) {
		try {
			InputStream inputStream = processService.getPicture(procInstId);
			ServletOutputStream outputStream = response.getOutputStream();
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = inputStream.read(buff, 0, 100)) > 0) {
				outputStream.write(buff, 0, rc);
			}
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping
	public Object getProcess(String userId) {
		List<Task> list = processService.getProcess(userId);
		List<Map<String, Object>> result = new ArrayList<>();
		for (Task task : list) {
			Map<String, Object> map = new LinkedHashMap<>();
			result.add(map);
			map.put("id", task.getId());
			map.put("key", task.getName());
			map.put("assignee", task.getAssignee());
		}
		return result;
	}
	
	@PutMapping("complete")
	public void completeProcess(String taskId, String userId) {
		processService.completeProcess(taskId, userId);
	}
	
	@GetMapping("info")
	public Object getProcessInfo(String procId) {
		Map<String, Object> resultMap = processService.getProcessInfo(procId);
		return resultMap;
	}
	
	
}
