package com.lind.logback.trace;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import static com.lind.logback.mdc.LogInterceptor.HTTP_HEADER_TRACE;
import static com.lind.logback.mdc.LogInterceptor.TRACE_ID;

/**
 * 基于openFeign的拦截器，处理traceId.
 *
 * @author lind
 * @date 2023/1/29 11:44
 * @since 1.0.0
 */
@Configuration
public class FeignTraceIdInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		String traceId = MDC.get(TRACE_ID);

		if (traceId != null) {
			template.header(HTTP_HEADER_TRACE, traceId);
		}
	}

}
