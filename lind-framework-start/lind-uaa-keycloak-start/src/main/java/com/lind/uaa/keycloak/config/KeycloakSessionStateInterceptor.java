package com.lind.uaa.keycloak.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.lind.uaa.keycloak.config.Constant.COOKIE_IS_LOGIN;
import static com.lind.uaa.keycloak.config.Constant.getCurrentHost;

/**
 * keycloak校验用户上下文拦截器
 */
@Slf4j
public class KeycloakSessionStateInterceptor implements HandlerInterceptor {

	@Autowired
	UaaProperties uaaProperties;

	@Autowired
	private KeycloakSpringBootProperties keycloakSpringBootProperties;

	/**
	 * 控制器方法调用之前会进行
	 * @param request
	 * @param response
	 * @param handler
	 * @return true就是选择可以调用后面的方法 如果后续有ControllerAdvice的话会去执行对应的方法等。
	 * @throws Exception
	 */
	@SneakyThrows
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws AccessDeniedException {
		if (handler instanceof HandlerMethod) {
			if (request.getMethod().toLowerCase().equals("get")) {
				// 从kc跳回来后，不走这个逻辑，避免反复跳
				if (request.getParameter("kc") == null) {
					String isLogin = "";
					if (request.getCookies() != null) {
						for (Cookie cookie : request.getCookies()) {
							// 取出自己域名下的session_state，如果未登录它是空的，空的将会使用kc域名下的KEYCLOAK_SESSION_LEGACY
							if (cookie.getName().equals(COOKIE_IS_LOGIN)) {
								isLogin = cookie.getValue();
								break;
							}
						}
					}
					// 重定向到kc写入登录状态
					String url = keycloakSpringBootProperties.getAuthServerUrl()
							+ String.format("/realms/%s/sms/kc-sessions?", keycloakSpringBootProperties.getRealm())
							+ COOKIE_IS_LOGIN + "=" + isLogin + "&client=" + keycloakSpringBootProperties.getResource()
							+ "&redirect_uri=" + getCurrentHost(request, Constant.TOKEN_AUTHORIZATION_CODE_REDIRECT)
							+ "&refer_uri=" + request.getRequestURL();
					response.sendRedirect(url);
					return false;

				}
			}
		}
		return true;
	}

}
