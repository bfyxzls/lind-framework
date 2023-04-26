package com.lind.kafka.aop;

import com.lind.kafka.entity.CurrentUserAware;
import com.lind.kafka.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 消息发送拦截器.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class KafkaSenderInterceptor {

	@Autowired(required = false)
	CurrentUserAware currentUserAware;

	public static Date getDaDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 设置为东八区
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		Date date = new Date();
		String dateStr = sdf.format(date);
		// 将字符串转成时间
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date newDate = null;
		try {
			newDate = df.parse(dateStr);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return newDate;
	}

	@Pointcut("execution(* com.lind.kafka.producer.MessageSender.send(..))")
	public void pointcut() {

	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();

		if (args.length > 1) {
			Object arg = args[1];
			if (arg instanceof MessageEntity) {
				// 填充发送时间
				if (((MessageEntity) arg).getSendTime() == null) {
					((MessageEntity) arg).setSendTime(getDaDate());
				}

				// 填充发送消息的人
				String currentUserName = "system";
				if (currentUserAware != null) {
					currentUserName = currentUserAware.getCurrentUserName();
				}
				((MessageEntity) arg).setSendUser(currentUserName);

			}
		}
		return pjp.proceed();
	}

}
