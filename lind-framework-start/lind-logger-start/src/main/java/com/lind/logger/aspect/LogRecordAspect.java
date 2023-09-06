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

/**
 * @Aspect 注解的使用：@Aspect 注解通常用于声明切面，而切面是 Spring 管理的组件。因此，@Autowired 注解可以直接用于切面类， 以注入其他
 * Spring 托管的 bean。Spring AOP通过代理机制实现，切面类被 Spring 托管，因此可以利用 Spring 的依赖注入功能。
 *
 * InvocationHandler 接口的实现类：InvocationHandler 接口的实现类通常不是由 Spring 管理的，它们是标准 Java 类。
 * 在这种情况下，Spring 的依赖注入机制不会自动生效，因为 Spring 无法感知和管理这些类。如果你在 InvocationHandler 实现类中需要依赖注入的功能，
 * 你需要手动注入依赖或者在创建代理对象时进行注入。
 *
 * 总之，差异在于组件是否由 Spring 管理。Spring 管理的组件可以利用 @Autowired 注解来实现依赖注入，而标准 Java 类通常需要手动注入依赖。
 * @Aspect 注解的类通常是由 Spring 管理的，因此可以使用 @Autowired 注解来注入其他组件。 而 InvocationHandler
 * 接口的实现类通常不是由 Spring 管理的，所以不能直接使用 @Autowired 注解。
 */
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
			if (((CommonResult) result).isSuccess())// 正常返回
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
