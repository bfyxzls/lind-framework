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

import java.time.LocalDateTime;

/**
 * 消息发送拦截器.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class KafkaSenderInterceptor {

	@Autowired(required = false)
	CurrentUserAware currentUserAware;

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
					((MessageEntity) arg).setSendTime(LocalDateTime.now());
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
