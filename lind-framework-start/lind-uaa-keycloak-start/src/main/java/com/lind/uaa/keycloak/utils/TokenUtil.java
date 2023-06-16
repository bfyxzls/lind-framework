package com.lind.uaa.keycloak.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lind.uaa.keycloak.config.Constant.ACTIVE_STATUS;
import static com.lind.uaa.keycloak.config.Constant.CLIENT_ID;
import static com.lind.uaa.keycloak.config.Constant.CLIENT_SECRET;
import static com.lind.uaa.keycloak.config.Constant.TOKEN;
import static com.lind.uaa.keycloak.config.Constant.VERIFY_TOKEN;

/**
 * @author lind
 * @date 2022/9/20 13:58
 * @since 1.0.0
 */
public class TokenUtil {

	// 需要刷新token,被写到响应头中
	public final static String NEED_REFRESH_TOKEN = "Need-Refresh-Token";

	/**
	 * 正则说明： ^Bearer：以"Bearer"开头 (?<token>：将匹配到的内容命名为"token"
	 * [a-zA-Z0-9-:.~+/]+=*：匹配一串由大小写字母、数字以及特定字符(:、.、、~、+、/)组成的字符串，长度可以为0或任意正整数
	 * 翻译成中文为：以"Bearer"开头，匹配一串由大小写字母、数字以及特定字符(:、.、_、~、+、/)组成的字符串，长度可以为0或任意正整数，并将其命名为"token"。
	 */
	public static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$",
			Pattern.CASE_INSENSITIVE);

	public static String getSubject(String token) {
		if (token.startsWith("bearer ") || token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		return JwtUtils.getJWT(token).getSubject();
	}

	/**
	 * 验证token是否在线. KC适配器集成了token有效性校验，没有集成在线校验，本方法实现在线校验. 它的每次验证仍然会与keycloak进行通讯.
	 */
	public static void isOnline(KeycloakSpringBootProperties keycloakSpringBootProperties, String tokenString) {
		if (tokenString.startsWith("bearer ") || tokenString.startsWith("Bearer ")) {
			tokenString = tokenString.substring(7);
		}
		Map<String, Object> params = new HashMap<>();
		params.put(CLIENT_ID, keycloakSpringBootProperties.getResource());
		params.put(CLIENT_SECRET, keycloakSpringBootProperties.getClientKeyPassword());
		params.put(TOKEN, tokenString);
		String url = keycloakSpringBootProperties.getAuthServerUrl()
				.concat(String.format(VERIFY_TOKEN, keycloakSpringBootProperties.getRealm()));

		String verifyResult = HttpUtil.post(url, params);
		try {
			JSONObject jsonObj = JSON.parseObject(verifyResult);
			// 验证在线token，如果已退出，直接401，由业务方自己跳转
			if (!jsonObj.getBoolean(ACTIVE_STATUS)) {
				throw new AuthenticationCredentialsNotFoundException("access_token is not online");
			}
		}
		catch (RuntimeException ex) {
			throw new AuthenticationCredentialsNotFoundException("access_token is not online");
		}
	}

	/**
	 * 从token中获取bearer里的字符.
	 * @param authorization
	 * @return
	 */
	public static String resolveFromAuthorizationHeader(String authorization) {
		if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
			return null;
		}
		Matcher matcher = AUTHORIZATION_PATTERN.matcher(authorization);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Bearer token is malformed");
		}
		return matcher.group("token");
	}

}
