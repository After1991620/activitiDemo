package com.lesso.activitiDemo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lesso.activitiDemo.ActivitiDemoApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ActivitiDemoApplication.class)
public class ResumeServiceImplTest {
	
	@Autowired
	RepositoryService repositoryService;

	@Autowired
    RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;
	
	@Test
	public void testStoreResume() {
		try {
			Map<String, Object> variables = new HashMap<>();
			variables.put("applicantName", "John Doe");
			variables.put("email", "john.doe@activiti.com");
			variables.put("phoneNumber", "123456789");
			repositoryService.createDeployment().addClasspathResource("processes/MyProcess.bpmn").deploy();
//			runtimeService.startProcessInstanceByKey("myProcess", variables);
			
			List<Comment> taskComments = taskService.getTaskComments("");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
