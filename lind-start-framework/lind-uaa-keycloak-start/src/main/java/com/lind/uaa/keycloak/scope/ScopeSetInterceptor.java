package com.lind.uaa.keycloak.scope;

import com.lind.uaa.keycloak.config.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * scopeSet注解拦截器，主要拦截具有@ScopeSet的方法. 过滤器：可以获得Http原始的请求和响应信息，但是拿不到响应方法的信息
 * 拦截器：可以获得Http原始的请求和响应信息，也可以得到响应方法的信息，但不能拿到方法参数 切面Aspect:
 * 可以拿得到方法响应中参数的值，但是拿不到原始的Http请求和相对应响应的方法
 */
@Slf4j
public class ScopeSetInterceptor implements HandlerInterceptor {

	/**
	 * 控制器方法调用之前会进行 和上面的Filter一样，继承的某些接口方法中也加了default关键字，可以不用重写，这里为了演示就都写了
	 * @param request
	 * @param response
	 * @param handler
	 * @return true就是选择可以调用后面的方法 如果后续有ControllerAdvice的话会去执行对应的方法等。
	 * @throws Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws AccessDeniedException {
		// scope授权范围
		if (handler instanceof HandlerMethod) {
			if (((HandlerMethod) handler).getMethodAnnotation(ScopeSet.class) != null) {
				log.info("ScopeSet HandlerInterceptor:[{}].[{}]",
						((HandlerMethod) handler).getBean().getClass().getName(),
						((HandlerMethod) handler).getMethod().getName());
				String value = ((HandlerMethod) handler).getMethodAnnotation(ScopeSet.class).value();
				if (!Arrays.asList(SecurityUser.getScope()).contains(value)) {
					throw new AccessDeniedException("抱歉，您没有访问权限");
				}
			}
		}
		return true;
	}

}
