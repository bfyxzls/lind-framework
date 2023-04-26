package com.lind.uaa.jwt.utils;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.UUID;

public class JwtUtils {

	private static final String ISSUER = "abc123";

	/*------------------------------Using RS256---------------------------------*/
	/* 获取签发的token，返回给前端 */
	public static String generTokenByRS256(UserDetails user) throws Exception {

		RSA256Key rsa256Key = SecretKeyUtils.getRSA256Key(); // 获取公钥/私钥
		Algorithm algorithm = Algorithm.RSA256(rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());

		return createToken(algorithm, user);

	}

	/**
	 * 签发token, privateKey,header+poyload
	 * @param algorithm
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String createToken(Algorithm algorithm, Object data) throws Exception {
		String[] audience = { "app", "web" };
		return JWT.create().withAudience(audience) // 观众，相当于接受者
				.withIssuedAt(new Date()) // 生成签名的时间
				.withExpiresAt(DateUtils.addHours(new Date(), 2)) // 生成签名的有效期
				.withClaim("data", JSON.toJSONString(data)) // 存数据
				.withNotBefore(new Date()) // 生效时间
				.withJWTId(UUID.randomUUID().toString()) // 编号
				.sign(algorithm); // 签入
	}

	/**
	 * 验证token. publicKey,header+payload
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static DecodedJWT verifierToken(String token) throws Exception {
		RSA256Key rsa256Key = SecretKeyUtils.getRSA256Key(); // 获取公钥/私钥
		// 其实按照规定只需要传递 publicKey 来校验即可，这个我在2021-03-08实验过，不需要私钥即可
		Algorithm algorithm = Algorithm.RSA256(rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());
		JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier
																// instance 可复用的验证实例
		DecodedJWT jwt = verifier.verify(token);
		return jwt;

	}

}
