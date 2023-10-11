package com.lind.common.othertest;

import org.jboss.logging.Logger;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimeTask {

	private static final Logger logger = Logger.getLogger(AsyncUserRoleExpireTask.class);

	private final List<Runnable> queue = new ArrayList();

	private void sched(TimerTask task, long time, long period) {
		if (time < 0)
			throw new IllegalArgumentException("Illegal execution time.");
		if (Math.abs(period) > (Long.MAX_VALUE >> 1))
			period >>= 1;

		synchronized (queue) {
			queue.add(task);
		}
	}

	@Test
	public void andSymbol() {
		int i = 16;
		System.err.println(i >>= 1); // 右移数据相当于除以2
		System.err.println(i);
		int c = 10;
		System.err.println(c >>= 1); // 右移数据相当于除以2
	}

	@Test
	public void timerTask() throws InterruptedException {

		logger.info("timetask");
		Timer timer = new Timer();
		AsyncUserRoleExpireTask myTimerTask = new AsyncUserRoleExpireTask("user-role-expire-task");
		// 每1秒执行一次
		timer.schedule(myTimerTask, 2000L, 1000L);
		Thread.sleep(10000);
	}

	public static class AsyncUserRoleExpireTask extends TimerTask {

		private static final Logger logger = Logger.getLogger(AsyncUserRoleExpireTask.class);

		private final String taskName;

		public AsyncUserRoleExpireTask(String taskName) {
			this.taskName = taskName;
		}

		@Override
		public void run() {
			try {
				logger.info(taskName + ":task doing");
			}
			catch (Throwable t) {
			}
		}

	}

}
