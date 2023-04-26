package com.lind.logger.aspect;

import com.lind.common.rest.CommonResult;
import com.lind.logger.anno.LogRecord;
import com.lind.logger.entity.LogEvaluationContext;
import com.lind.logger.entity.LogEvaluator;
import com.lind.logger.entity.LogRootObject;
import com.lind.logger.entity.LoggerInfo;
import com.lind.logger.service.CurrentIpAware;
import com.lind.logger.service.CurrentUserAware;
import com.lind.logger.service.LoggerService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Aspect
public class LogRecordAspect {

	/**
	 * 日志SpEL解析器
	 */
	private final LogEvaluator evaluator = new LogEvaluator();

	@Autowired
	CurrentUserAware currentUserAware;

	@Autowired
	LoggerService loggerService;

	@Autowired
	CurrentIpAware currentIpAware;

	/**
	 * 切点
	 */
	@Pointcut("@annotation(com.lind.logger.anno.LogRecord)")
	public void pointcut() {
	}

	@AfterReturning(pointcut = "pointcut()", returning = "result")
	public void after(JoinPoint joinPoint, Object result) {
		if (result instanceof CommonResult) {
			// 方法正常返回之后，记录日志
			if (((CommonResult) result).getCode() == 200)// 正常返回
			{
				loggerService.insert(generateLog(joinPoint));
			}
		}
	}

	/**
	 * 生成日志实体
	 * @param joinPoint 切入点
	 * @return 日志实体
	 */
	private LoggerInfo generateLog(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		Object[] args = joinPoint.getArgs();
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(joinPoint.getTarget());
		LogRecord annotation = method.getAnnotation(LogRecord.class);
		if (annotation != null) {
			LogRootObject rootObject = new LogRootObject(method, args, targetClass);
			LogEvaluationContext context = new LogEvaluationContext(rootObject, evaluator.getDiscoverer());
			Object content = evaluator.parse(annotation.detail(), context);

			Integer moduleType = annotation.moduleType();
			if (LogRootObject.getVar("moduleType") != null) {
				moduleType = Integer.parseInt(LogRootObject.getVar("moduleType").toString());
			}
			Integer operateType = annotation.operateType();
			if (LogRootObject.getVar("operateType") != null) {
				operateType = Integer.parseInt(LogRootObject.getVar("operateType").toString());
			}
			String dataId = Optional.ofNullable(evaluator.parse(annotation.dataId(), context)).orElse("").toString();
			if (LogRootObject.getVar("dataId") != null) {
				dataId = LogRootObject.getVar("dataId").toString();
			}
			String dataTitle = Optional.ofNullable(evaluator.parse(annotation.dataId(), context)).orElse("").toString();
			if (LogRootObject.getVar("dataTitle") != null) {
				dataTitle = LogRootObject.getVar("dataTitle").toString();
			}
			LoggerInfo loggerInfo = LoggerInfo.builder().detail(content.toString()).moduleType(moduleType)
					.dataId(dataId).dataTitle(dataTitle).operateType(operateType).operateTime(LocalDateTime.now())
					.operatorIp(currentIpAware.address()).operator(currentUserAware.username()).build();

			return loggerInfo;
		}
		return null;
	}

}
