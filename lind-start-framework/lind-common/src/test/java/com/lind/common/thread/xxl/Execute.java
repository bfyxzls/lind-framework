package com.lind.common.thread.xxl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lind
 * @date 2022/11/10 17:26
 * @since 1.0.0
 */
public class Execute {

	private static ConcurrentMap<Integer, JobThread> jobThreadRepository = new ConcurrentHashMap<>();

	private static Logger logger = LoggerFactory.getLogger(Execute.class);

	public static JobThread registJobThread(int jobId) {
		JobThread newJobThread = new JobThread(jobId);
		newJobThread.pushTriggerQueue("处理程序" + jobId);
		newJobThread.start();
		logger.info(">>>>>>>>>>> xxl-job regist JobThread success, jobId:{}", new Object[] { jobId });

		JobThread oldJobThread = jobThreadRepository.put(jobId, newJobThread); // putIfAbsent
																				// | oh my
																				// god,
																				// map's
																				// put
																				// method
																				// return
																				// the old
																				// value!!!
		if (oldJobThread != null) {
			oldJobThread.toStop();
			oldJobThread.interrupt();
			logger.info("kill老的");
		}

		return newJobThread;
	}

	public static JobThread removeJobThread(int jobId) {
		JobThread oldJobThread = jobThreadRepository.remove(jobId);
		if (oldJobThread != null) {
			oldJobThread.toStop();
			oldJobThread.interrupt();

			return oldJobThread;
		}
		return null;
	}

	public static JobThread loadJobThread(int jobId) {
		return jobThreadRepository.get(jobId);
	}

}
