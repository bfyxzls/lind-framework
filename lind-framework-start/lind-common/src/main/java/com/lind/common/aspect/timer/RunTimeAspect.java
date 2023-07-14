package com.lind.common.aspect.timer;

import com.lind.common.core.util.StopWatchUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * 程序运行时间拦截器
 */
@Aspect
@Slf4j
@Order(1)
public class RunTimeAspect {

	/**
	 * 目前只做简单的时间打印
	 * @param point
	 * @param runTime
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(runTime)")
	public Object around(ProceedingJoinPoint point, RunTime runTime) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(point.getSignature().getDeclaringType().getTypeName()).append(".")
				.append(point.getSignature().getName()).append("()");
		String method = stringBuilder.toString();
		return StopWatchUtils.returnTimer(method, () -> {
			Object proceed = null;
			try {
				proceed = point.proceed();
			}
			catch (Throwable throwable) {
				throwable.printStackTrace();
			}
			return proceed;

		});
	}

}
