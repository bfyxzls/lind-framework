import com.pkulaw.schedule.QuartzApplication;
import com.pkulaw.schedule.dynamic.DynamicJob;
import com.pkulaw.schedule.dynamic.DynamicSchedulerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author lind
 * @date 2023/3/2 14:04
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@EnableScheduling
@SpringBootTest(classes = QuartzApplication.class)
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
