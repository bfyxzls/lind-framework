package com.lind.common.aspect.repeat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import java.util.Date;

/**
 * 运行出错后重试拦截器
 */
@Aspect
@Slf4j
@Order(1)
@RequiredArgsConstructor
public class TryDoAspect {

	/**
	 * 重试
	 * @param point
	 * @param tryDo
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(tryDo)")
	public Object around(ProceedingJoinPoint point, TryDo tryDo) throws Throwable {

		int retry = 0;
		while (retry++ < tryDo.limit()) {
			try {
				System.out.println("try do " + retry + new Date());
				return point.proceed();
			}
			catch (Exception ex) {
				if (retry >= tryDo.limit()) {
					throw ex;
				}
				try {
					Thread.sleep(tryDo.frequency() * retry);
				}
				catch (InterruptedException interruptedException) {
				}
			}

		}
		return null;
	}

}
