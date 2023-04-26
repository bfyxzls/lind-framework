package com.lind.uaa.jwt.interceptor;

import com.lind.uaa.jwt.utils.UserContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author ruoyi
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		UserContextHolder.setUserId(request.getHeader("user_id"));

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		UserContextHolder.remove();
	}

}
