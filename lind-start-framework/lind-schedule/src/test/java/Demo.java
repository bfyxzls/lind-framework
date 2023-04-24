import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author lind
 * @date 2023/3/2 14:04
 * @since 1.0.0
 */
public class Demo implements Job {

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		System.out.println("demo is running!");
	}

}
