package com.pkulaw.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestJob implements Job {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

		System.out.println("CRON ----> schedule " + jobExecutionContext.getJobDetail().getKey().getName()
				+ " is running ... + " + dateFormat.format(new Date()));
	}

}
