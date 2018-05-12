package com.lesso.activitiDemo.service.impl;

import org.springframework.stereotype.Service;

import com.lesso.activitiDemo.service.ResumeService;

@Service("resumeService")
public class ResumeServiceImpl implements ResumeService {

	@Override
	public void storeResume() {
		System.out.println("任务已经执行.....................................");
	}

}
