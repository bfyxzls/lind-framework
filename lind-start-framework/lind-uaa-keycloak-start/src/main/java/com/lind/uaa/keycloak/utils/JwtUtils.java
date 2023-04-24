package com.lind.uaa.keycloak.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Map;

/**
 * JWT操作token工具类
 *
 * @author liuxd
 */
@Slf4j
public class JwtUtils {

	/**
	 * 解密出token中Payload
	 * @param token
	 * @return
	 */
	public static String decodePayload(String token) {
		String str = null;
		try {
			DecodedJWT jwt = JWT.decode(token);
			str = new String(Base64.decodeBase64(jwt.getPayload()));
		}
		catch (JWTDecodeException e) {
			log.error("JwtUtils decodePayload#get fail:{},params:{}", Throwables.getStackTraceAsString(e));
		}
		return str;
	}

	public static DecodedJWT getJWT(String token) {

		DecodedJWT jwt = JWT.decode(token);
		return jwt;
	}

	/**
	 * 解密出token中Header
	 * @param token
	 * @return
	 */
	public static String decodeHeader(String token) {
		DecodedJWT jwt = JWT.decode(token);
		return new String(Base64.decodeBase64(jwt.getHeader()));
	}

	/**
	 * 获取一个指定名称的头部声明
	 * @param tokenKey Token字符串
	 * @param claimName 声明的Key值
	 * @return 声明对象
	 */
	public static Claim getHeaderClaim(String tokenKey, String claimName) {
		return getDecodeToken(tokenKey).getHeaderClaim(claimName);
	}

	/**
	 * 对Token进行解析
	 * @param tokenKey Token字符串
	 * @return 解密后的Token对象
	 */
	private static DecodedJWT getDecodeToken(String tokenKey) {
		DecodedJWT jwt = JWT.decode(tokenKey);
		return jwt;
	}

	/**
	 * 校验Token是否有效
	 * @param tokenKey Token字符串
	 * @return true-false
	 */
	public static boolean checkToken(String tokenKey) {
		try {
			// 如果Token解析错误会抛出异常
			getDecodeToken(tokenKey);
		}
		catch (JWTVerificationException e) {
			return false;
		}
		return true;
	}

	/**
	 * 获取一个指定名称的载荷声明
	 * @param tokenKey Token字符串
	 * @param claimName 声明的Key值
	 * @return 声明对象
	 */
	public static Claim getPayloadClaim(String tokenKey, String claimName) {
		return getDecodeToken(tokenKey).getClaim(claimName);
	}

	/**
	 * 获取所有的载荷声明
	 * @param tokenKey Token字符串
	 * @return 声明对象的Map
	 */
	public static Map<String, Claim> getAllPayloadClaim(String tokenKey) {
		return getDecodeToken(tokenKey).getClaims();
	}

}
