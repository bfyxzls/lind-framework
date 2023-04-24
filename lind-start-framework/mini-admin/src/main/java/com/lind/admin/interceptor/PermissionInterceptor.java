package com.lind.admin.interceptor;

import com.lind.admin.annotation.PermissionLimit;
import com.lind.admin.entity.User;
import com.lind.admin.exception.PermissionLimitException;
import com.lind.admin.service.LoginService;
import com.lind.admin.util.I18nUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截
 *
 * @author xuxueli 2015-12-12 18:09:04
 */
@Component
public class PermissionInterceptor implements AsyncHandlerInterceptor {

	@Resource
	private LoginService loginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (!(handler instanceof HandlerMethod)) {
			return true; // proceed with the next interceptor
		}

		// if it needs login
		boolean needLogin = true;
		boolean needAdminuser = false;
		String[] permissions = null;
		HandlerMethod method = (HandlerMethod) handler;
		PermissionLimit permissionLimit = method.getMethodAnnotation(PermissionLimit.class);
		if (permissionLimit != null) {
			needLogin = permissionLimit.limit();
			needAdminuser = permissionLimit.adminuser();
			permissions = permissionLimit.permissions();
		}

		if (needLogin) {
			User loginUser = loginService.ifLogin(request, response);
			if (loginUser == null) {
				response.setStatus(302);
				response.setHeader("location", request.getContextPath() + "/toLogin");
				return false;
			}
			if (needAdminuser && loginUser.getRole() != 1) {
				throw new PermissionLimitException(I18nUtil.getString("system_permission_limit"));
			}
			if (!loginUser.validPermission(permissions)) {
				throw new PermissionLimitException(I18nUtil.getString("system_permission_limit"));
			}

			request.setAttribute(LoginService.LOGIN_IDENTITY_KEY, loginUser);
		}

		return true; // proceed with the next interceptor
	}

}
