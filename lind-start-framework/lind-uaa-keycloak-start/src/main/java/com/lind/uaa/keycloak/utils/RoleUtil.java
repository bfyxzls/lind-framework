package com.lind.uaa.keycloak.utils;

/**
 * @author lind
 * @date 2022/9/19 17:56
 * @since 1.0.0
 */
public class RoleUtil {

	public static final String ROLE_PREFIX = "ROLE_";

	/**
	 * 角色格式化，对客户端角色进行加工.
	 * @param auth
	 * @return
	 */
	public static String removeRolePrefix(String auth, String resource) {
		int index = auth.startsWith(ROLE_PREFIX) ? ROLE_PREFIX.length() : 0;
		auth = auth.substring(index);
		if (auth.startsWith(resource)) {
			// currentClientId.length() + 1表示clientId/role里的/
			index = auth.startsWith(resource) ? resource.length() + 1 : 0;
			return auth.substring(index);
		}
		return auth;
	}

}
