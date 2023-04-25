package com.lind.uaa.keycloak.config;

import javax.servlet.http.HttpServletRequest;

public interface Constant {

	// 请求头认证键
	String AUTHORIZATION = "Authorization";

	String CLIENT_ID = "client_id";

	String CLIENT_SECRET = "client_secret";

	String TOKEN = "token";

	String ACTIVE_STATUS = "active";

	// 表示是否登录的cookie
	String COOKIE_IS_LOGIN = "isLogin";

	String TOKEN_APPLET = "token/applet";

	String TOKEN_PASSWORD_LOGIN = "token/passwordLogin";

	String TOKEN_CLIENT_CREDENTIALS_LOGIN = "token/clientCredentialsLogin";

	String TOKEN_AUTHORIZATION_CODE_REDIRECT = "token/authorizationCodeRedirect";

	String TOKEN_AUTHORIZATION_CODE_RESPONSE = "token/authorizationCodeResponse";

	// 验证token有效性
	String VERIFY_TOKEN = "/realms/%s/protocol/openid-connect/token/introspect";

	// kc里的权限列表
	String KC_PERMISSION = "kc_permission::";

	// kc里的角色对应的权限
	String KC_ROLE_PERMISSION = "kc_role_permission::";

	/**
	 * 返回当前Url，除去参数的部分.
	 * @param request
	 * @return
	 */
	static String getCurrentHost(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length())
				.append(request.getSession().getServletContext().getContextPath()).append("/").toString();
		return tempContextUrl;
	}

	/**
	 * 得到当前域的地址.
	 * @param request
	 * @param path
	 * @return
	 */
	static String getCurrentHost(HttpServletRequest request, String path) {
		return getCurrentHost(request) + path;
	}

}
