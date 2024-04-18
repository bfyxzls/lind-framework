package com.lind.common.encrypt;

import org.junit.Test;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

/**
 * @author lind
 * @date 2023/11/7 17:42
 * @since 1.0.0
 */
public class Rsa256JwtTest {

	// RSA公钥的模数
	String modulus = "yOCNCy8x280vYwpVl26ToWPXkTFMdkSkLQ-UB6uCK5_1Ojw1-ZqKWhAZg4bNTG03QtG7xJOPJUH0S2Gf2CNbZjm2OEe1ZeY08JCFThZ4h5D6iuAOarmYjlSnzhdW8jpiSkm0Okci-xZkC1dWW2twjGgc0_E6o9JXYEggfrDWbllpGQOib-lx-mU51e0OMqu8TwSrLoa_d0a2dawPJnU3l1a6lyvhD7W_asUphMNeLKgmNf4wf92wElLlDkB2Qozfg1J_6l4JzBbgVLLljO0j7VKv7seyAnul55KmRd9tUjpdecf0FQPFZiNOTW-g4ivMNBQZvKAnsfgDboxy1PzW1Q";

	// RSA公钥的指数
	String exponent = "AQAB";

	String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyOCNCy8x280vYwpVl26ToWPXkTFMdkSkLQ+UB6uCK5/1Ojw1+ZqKWhAZg4bNTG03QtG7xJOPJUH0S2Gf2CNbZjm2OEe1ZeY08JCFThZ4h5D6iuAOarmYjlSnzhdW8jpiSkm0Okci+xZkC1dWW2twjGgc0/E6o9JXYEggfrDWbllpGQOib+lx+mU51e0OMqu8TwSrLoa/d0a2dawPJnU3l1a6lyvhD7W/asUphMNeLKgmNf4wf92wElLlDkB2Qozfg1J/6l4JzBbgVLLljO0j7VKv7seyAnul55KmRd9tUjpdecf0FQPFZiNOTW+g4ivMNBQZvKAnsfgDboxy1PzW1QIDAQAB";

	String KcJwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJFXzZpaDM1eVRMSk1pZUkwdnFnOU1tVFFySjZSY1VTeGlYZU5kY01hb1lrIn0.eyJleHAiOjE3MTMzMzE3MjQsImlhdCI6MTcxMzMyOTkyNCwianRpIjoiYTlmMTg5NTAtMWJkZS00N2VkLWE0MjctYTgyNzNjNmVlNTZhIiwiaXNzIjoiaHR0cHM6Ly90ZXN0Y2FzLnBrdWxhdy5jb20vYXV0aC9yZWFsbXMvZmFiYW8iLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsIm5vZGUtc2VydmljZXMtdGVzdCIsImFjY291bnQiXSwic3ViIjoiMzQ3YzllOWUtMDc2Yy00NWUzLWJlNzQtYzQ4MmZmZmNjNmU1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicGt1bGF3Iiwic2Vzc2lvbl9zdGF0ZSI6IjMzM2Q5NmNjLTUzOGUtNGZhYi1iNjI3LTEzOWRjNjQ1YmNkNyIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsi57uE566h55CG5ZGYIiwiZmVkZXJhdGVkIiwibGF3Iiwiam91cm5hbCIsImNhcnNpIiwiY2FzZSJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJlYWxtLW1hbmFnZW1lbnQiOnsicm9sZXMiOlsidmlldy1yZWFsbSIsInZpZXctaWRlbnRpdHktcHJvdmlkZXJzIiwibWFuYWdlLWlkZW50aXR5LXByb3ZpZGVycyIsImltcGVyc29uYXRpb24iLCJyZWFsbS1hZG1pbiIsImNyZWF0ZS1jbGllbnQiLCJtYW5hZ2UtdXNlcnMiLCJxdWVyeS1yZWFsbXMiLCJ2aWV3LWF1dGhvcml6YXRpb24iLCJxdWVyeS1jbGllbnRzIiwicXVlcnktdXNlcnMiLCJtYW5hZ2UtZXZlbnRzIiwibWFuYWdlLXJlYWxtIiwidmlldy1ldmVudHMiLCJ2aWV3LXVzZXJzIiwidmlldy1jbGllbnRzIiwibWFuYWdlLWF1dGhvcml6YXRpb24iLCJtYW5hZ2UtY2xpZW50cyIsInF1ZXJ5LWdyb3VwcyJdfSwibm9kZS1zZXJ2aWNlcy10ZXN0Ijp7InJvbGVzIjpbImdwdCJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsInZpZXctYXBwbGljYXRpb25zIiwidmlldy1jb25zZW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJtYW5hZ2UtY29uc2VudCIsImRlbGV0ZS1hY2NvdW50Iiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgYXV0aG9yaXphdGlvbiByb2xlcyBwa3VsYXctZXh0ZW5zaW9ucyBlbWFpbCB2NiBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJsb2dpblR5cGUiOiJwYXNzd29yZCIsImdyb3VwVXNlck5hbWUiOiJ0ZXN0IiwiZXh0ZW5zaW9uX3JvbGVzIjp7IndlaXhpbiI6WyJsYXciLCJqb3VybmFsIiwiY2FzZSJdfSwibG9naW5UaW1lUGVyaW9kIjp7ImF1dG8iOjE4OCwicGFzc3dvcmQiOjAsIndlaXhpbiI6Mjk1fSwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzdCIsImdyb3VwVXNlcklkIjoiMzQ3YzllOWUtMDc2Yy00NWUzLWJlNzQtYzQ4MmZmZmNjNmU1IiwicGhvbmVOdW1iZXIiOiIxMzUyMTk3Mjk5MSIsImxvZ2luSXAiOiIxMTEuMTk4LjE0My4xOTQiLCJuaWNrbmFtZSI6InRlc3QiLCJpc0ZlZGVyYWxVc2VyIjowLCJwaG9uZU51bWJlclZlcmlmaWVkIjoiZmFsc2UiLCJpc0dyb3VwVXNlciI6IjEiLCJlbWFpbCI6Inp6bDEyM0BzaW5hLmNvbSJ9.OSbBnOmITPtKsprIg2UU87BwBXNIgnzBXRPk6HieqZZ4y0zG4krMXB_hgWXB-iOucMqddxILgHt-TVVck9IGvgDd7zobwpDkA0TnHX6Jqgbgv2vLGtxQ2aII0ieDBHAeNthXxWte-Nnc9qIeaQ-yyHJ9u1Ec3OOD1Lqn3owjV42DAqGuQhCR3EhDNmFUChk34c5ujK-SjMpqucCGAVrZ38BiUPQrK4MLUdAtXybQQWGKX1hldl7NvJEADLsvwM8s_Yvl49hTVlLsgKV1pC-TbRLxYQ7LR3fSms-bgmDRqwtSwozauS_L6ZHFYaAYjc3O3qeYxwyvkyGJ4mQq94dmaw";

	// 将Base64 URL编码转换为标准Base64编码
	public static String convertBase64URLToBase64(String base64URL) {
		// 将 "-" 替换为 "+"
		// 将 "_" 替换为 "/"
		String base64 = base64URL.replace('-', '+').replace('_', '/');
		// 计算需要添加的填充字符数量
		int padding = (4 - (base64.length() % 4)) % 4;
		if (padding > 0) {
			for (int i = 0; i < padding; i++) {
				base64 += "=";
			}
		}
		return base64;
	}

	/**
	 * 测试失败，这块需要找到与keycloak jwt一样的签名算法
	 * @throws Exception
	 */
	@Test
	public void verifySign() throws Exception {
		String[] jwtParts = KcJwtToken.split("\\.");
		String header = jwtParts[0];
		String payload = jwtParts[1];
		byte[] signatureBytes = jwtParts[2].getBytes();
		// 解码Base64格式的模数和指数
		byte[] decodedModulus = Base64.getUrlDecoder().decode(modulus);// getMimeDecoder()会忽略非Base64字符（如换行符、空格等）
		byte[] decodedExponent = Base64.getUrlDecoder().decode(exponent);
		// 构建RSA公钥对象
		RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(new BigInteger(1, decodedModulus),
				new BigInteger(1, decodedExponent));
		// 验征RSA签名
		PublicKey publicKey = KeyFactory.getInstance("rsa").generatePublic(publicSpec);
		boolean result = RSAUtils.verify(header + "." + payload, publicKey, jwtParts[2]);
		System.out.print("验签结果:" + result);

	}

	@Test
	public void verifyJwtToken() throws Exception {
		String[] jwtParts = KcJwtToken.split("\\.");
		String header = jwtParts[0];
		String payload = jwtParts[1];
		String sign = jwtParts[2];
		PublicKey publicKey = RSAUtils.getPublicKey(publicKeyString);
		boolean result = RSAUtils.verify(header + "." + payload, publicKey, sign);
		System.out.print("验签结果:" + result);
	}

}
