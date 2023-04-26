package com.lind.logback.trace;

import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static com.lind.logback.mdc.LogInterceptor.HTTP_HEADER_TRACE;
import static com.lind.logback.mdc.LogInterceptor.TRACE_ID;

/**
 * @author lind
 * @date 2023/1/29 15:07
 * @since 1.0.0
 */
public class RestTemplateTraceIdInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		String traceId = MDC.get(TRACE_ID);
		if (traceId != null) {
			request.getHeaders().set(HTTP_HEADER_TRACE, traceId);
		}
		return execution.execute(request, body);
	}

}
