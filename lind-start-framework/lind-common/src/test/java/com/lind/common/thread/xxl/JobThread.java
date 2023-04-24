package com.lind.common.thread.xxl;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2022/11/10 17:11
 * @since 1.0.0
 */
public class JobThread extends Thread {

	private static Logger logger = LoggerFactory.getLogger(JobThread.class);

	boolean running = false;

	private int jobId;

	private LinkedBlockingQueue<String> triggerQueue;

	private volatile boolean toStop = false;

	public JobThread(int jobId) {
		this.jobId = jobId;
		this.triggerQueue = new LinkedBlockingQueue<String>();
		this.setName("JobThread-" + jobId + "-" + System.currentTimeMillis());
	}

	public void pushTriggerQueue(String triggerParam) {
		// avoid repeat
		if (triggerQueue.contains(triggerParam)) {
			logger.info(">>>>>>>>>>> repeate trigger job, logId:{}", triggerParam);
		}
		triggerQueue.add(triggerParam);
	}

	@SneakyThrows
	@Override
	public void run() {
		while (!toStop) {
			running = false;
			String triggerParam = triggerQueue.poll(3L, TimeUnit.SECONDS);
			System.out.println(String.format("id%s,value%s", jobId, triggerParam));
			if (triggerParam != null) {
				running = true;
			}
		}
	}

	/**
	 * kill job thread
	 *
	 */
	public void toStop() {
		/**
		 * Thread.interrupt只支持终止线程的阻塞状态(wait、join、sleep)，
		 * 在阻塞出抛出InterruptedException异常,但是并不会终止运行的线程本身； 所以需要注意，此处彻底销毁本线程，需要通过共享变量方式；
		 */
		this.toStop = true;
	}

}
