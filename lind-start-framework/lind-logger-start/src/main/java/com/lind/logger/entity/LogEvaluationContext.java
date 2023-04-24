package com.lind.logger.entity;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

public class LogEvaluationContext extends MethodBasedEvaluationContext {

	/**
	 * constructor **@paramRootObject Data source object *@paramDiscoverer Parameter
	 * parser
	 */
	public LogEvaluationContext(LogRootObject rootObject, ParameterNameDiscoverer discoverer) {
		super(rootObject, rootObject.getMethod(), rootObject.getArgs(), discoverer);
	}

}
