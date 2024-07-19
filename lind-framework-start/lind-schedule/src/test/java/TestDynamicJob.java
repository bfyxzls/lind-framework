import com.lind.schedule.dynamic.DynamicJob;
import com.lind.schedule.dynamic.DynamicSchedulerFactory;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;
import org.quartz.impl.RemoteScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author lind
 * @date 2023/3/2 14:04
 * @since 1.0.0
 */
@EnableScheduling
@ContextConfiguration(classes = { RemoteScheduler.class, DynamicSchedulerFactory.class })
public class TestDynamicJob {

	@Test
	public void newJob() throws SchedulerException, InterruptedException {
		DynamicJob dynamicJob = new DynamicJob();
		dynamicJob.jobName("zhansan1");
		dynamicJob.target(Demo.class);
		dynamicJob.cronExpression("0/1 * * * * ?");
		if (DynamicSchedulerFactory.existJob(dynamicJob)) {
			DynamicSchedulerFactory.resumeJob(dynamicJob);
		}
		else {
			DynamicSchedulerFactory.registerJob(dynamicJob);
		}
		Thread.sleep(10000);
	}

}
