package com.lind.common.aspect.repeat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

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

		long retry = 0;
		while (retry++ < tryDo.limit()) {
			try {
				return point.proceed();
			}
			catch (Exception ex) {
				if (retry >= tryDo.limit()) {
					throw ex;
				}
				Thread.sleep(tryDo.frequency() * retry);
			}

		}
		return null;
	}

}
