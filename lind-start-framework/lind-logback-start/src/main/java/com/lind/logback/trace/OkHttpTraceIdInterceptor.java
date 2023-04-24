package com.lind.logback.trace;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.MDC;

import java.io.IOException;

import static com.lind.logback.mdc.LogInterceptor.HTTP_HEADER_TRACE;
import static com.lind.logback.mdc.LogInterceptor.TRACE_ID;

/**
 * 基于okHttp的拦截器，处理traceId.
 *
 * @author lind
 * @date 2023/1/28 17:21
 * @since 1.0.0
 */
@Slf4j
public class OkHttpTraceIdInterceptor implements Interceptor {

	@NotNull
	@Override
	public Response intercept(@NotNull Chain chain) throws IOException {
		String traceId = MDC.get(TRACE_ID);
		Request request = null;
		if (traceId != null) {
			// 添加请求体
			request = chain.request().newBuilder().addHeader(HTTP_HEADER_TRACE, traceId).build();
		}
		return chain.proceed(request);
	}

}
