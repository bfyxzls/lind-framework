package com.lind.logback.mdc;

import com.lind.logback.user.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.IdGenerator;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MDC拦截器，添加动态的变量.
 *
 * @author lind
 * @date 2023/1/28 11:59
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class LogInterceptor implements HandlerInterceptor, ApplicationContextAware {

	public static final String CURRENT_USER = "currentUser";

	public static final String BROWSER_TYPE = "browserType";

	public static final String OS = "os";

	public static final String URI = "uri";

	public static final String TRACE_ID = "traceId";

	public static final String HTTP_HEADER_TRACE = "Trace-Id";

	private static ApplicationContext context;

	private final CurrentUser currentUser;

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
			throws Exception {
		// MDC就是ThreadLocal访问接口
		MDC.put(CURRENT_USER, currentUser.getUserName());
		MDC.put(BROWSER_TYPE, getBrowserAndOs(httpServletRequest)[1]);
		MDC.put(OS, getBrowserAndOs(httpServletRequest)[0]);
		MDC.put(URI, httpServletRequest.getRequestURL().toString());
		generateTraceId(httpServletRequest, httpServletResponse);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {
		MDC.remove(CURRENT_USER);
		MDC.remove(BROWSER_TYPE);
		MDC.remove(OS);
		MDC.remove(URI);
		MDC.remove(TRACE_ID);
	}

	String[] getBrowserAndOs(HttpServletRequest request) {
		String browserName = request.getHeader("USER-AGENT").toLowerCase();
		UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
		ReadableUserAgent agent = parser.parse(browserName);
		String os = agent.getOperatingSystem().getName() + " "
				+ agent.getOperatingSystem().getVersionNumber().toVersionString();
		String browser = agent.getName() + " " + agent.getVersionNumber().toVersionString();
		return new String[] { os, browser };
	}

	void generateTraceId(HttpServletRequest request, HttpServletResponse response) {
		String traceId = request.getHeader(HTTP_HEADER_TRACE);
		if (StringUtils.isBlank(traceId)) {
			if (context.getBeansOfType(IdGenerator.class).isEmpty()) {
				throw new NoSuchBeanDefinitionException("IdGenerator bean not found");
			}
			IdGenerator idGenerator = context.getBean(IdGenerator.class);
			traceId = idGenerator.generateId().toString().replaceAll("-", "");
		}

		MDC.put(TRACE_ID, traceId);
		response.addHeader(HTTP_HEADER_TRACE, traceId);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

}
