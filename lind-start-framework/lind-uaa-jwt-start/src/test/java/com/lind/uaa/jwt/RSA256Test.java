package com.lind.uaa.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.lind.uaa.jwt.utils.JwtUtils;
import com.lind.uaa.jwt.utils.RSA256Key;
import com.lind.uaa.jwt.utils.SecretKeyUtils;
import lombok.SneakyThrows;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RS256 (采用SHA-256 的 RSA 签名) 是一种非对称算法, 它使用公共/私钥对: 标识提供方采用私钥生成签名, JWT 的使用方获取公钥以验证签名。 由于公钥
 * (与私钥相比) 不需要保护, 因此大多数标识提供方使其易于使用方获取和使用 (通常通过一个元数据URL)。
 */
public class RSA256Test {

	static final Logger logger = LoggerFactory.getLogger(RSA256Test.class);

	@SneakyThrows
	@Test
	public void privateSignAndPublicVerify() {
		RSA256Key rsa256Key = SecretKeyUtils.getRSA256Key(); // 获取公钥/私钥
		Algorithm algorithm = Algorithm.RSA256(rsa256Key.getPublicKey(), rsa256Key.getPrivateKey());
		String token = JwtUtils.createToken(algorithm, "hello");
		logger.info("token={}", token);

		logger.info("验证token是否被篡改:{}", JwtUtils.verifierToken(token).getSignature());
	}

}
