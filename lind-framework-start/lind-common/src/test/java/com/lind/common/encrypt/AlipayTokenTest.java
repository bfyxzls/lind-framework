package com.lind.common.encrypt;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 对接支付宝第三方登录.
 *
 * @author lind
 * @date 2024/8/20 13:18
 * @since 1.0.0
 */
public class AlipayTokenTest {

	public static String getTimestamp() {
		// 获取当前的 LocalDateTime
		LocalDateTime now = LocalDateTime.now();
		// 定义日期时间格式
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String timestamp = now.format(formatter);
		return timestamp;
	}

	private static String generateSign(Map<String, String> params, String privateKey) throws Exception {
		// 按照字典顺序排序参数
		TreeMap<String, String> sortedParams = new TreeMap<>(params);
		StringBuilder preSignString = new StringBuilder();

		for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
			preSignString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}

		// 去掉最后一个 '&'
		preSignString.setLength(preSignString.length() - 1);

		// 使用私钥进行签名
		PrivateKey key = getPrivateKeyFromString(privateKey); // 需要实现这个方法
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(key);
		signature.update(preSignString.toString().getBytes("UTF-8"));

		byte[] signedData = signature.sign();
		return Base64.getEncoder().encodeToString(signedData); // 返回 Base64 编码的签名
	}

	private static PrivateKey getPrivateKeyFromString(String privateKeyPem) throws Exception {
		// 去掉 PEM 文件头和尾
		String privateKeyPEM = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "").replaceAll("\\s+", "");

		// 将 Base64 编码的字符串解码为字节数组
		byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

		// 创建 PKCS8EncodedKeySpec
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		// 生成 PrivateKey 对象
		return keyFactory.generatePrivate(keySpec);
	}

	@SneakyThrows
	@Test
	public void sign() {
		String timestamp = getTimestamp();
		Map<String, String> params = new HashMap<>();
		params.put("code", "7fc9d684e2cf4902993c9ac76b4bBC29");
		params.put("method", "alipay.system.oauth.token");
		params.put("timestamp", "2024-08-20 11:43:52");
		params.put("version", "1.0");
		params.put("sign_type", "RSA2");
		params.put("charset", "utf-8");
		params.put("app_id", "2021004169645002");
		params.put("grant_type", "authorization_code");
		String sign = generateSign(params,
				"MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCk9BxDw92p4u0yRvyLMWVTMMPmF+cdL9IQv/pL8uKaJfMXxv6tHVDHKaaq6kturMQshautDyB8mm9tocE72KJh5sKGgzdosIjuauZxpJ8xjotiYYP0P1kuQw85vlK3J77XUhvr1qzK6S8nZ0RJJZ2wKnVeQDS+qJRpiPnUNCVzbvZF7TbhBkuqXiO8J25lzfSYQy78qUt7TzmlWe3ezyzoznU9ZNtzeOpOFZL+asjcc4Aadix0qROf7LVzxlTXyNpyE9dmwRcX6jhAeQmfcDKBakwUa+VuP718j4sCItS2Ex0fQUQdDX3Ds5ND80KbeZjuZK8ggFHZBQMUY7K2LlKtAgMBAAECggEBAJy2io/a6qH3RPBqPo4jPKd/ge39MYYqI4HkUfCL7bsTaNNjLBvd6Rt+N6jTdRzNqIa/6+07nNqkgk5+ya2I0CUA7jiLOz4L0dvo1cyL4O5dBZFPIeUPtjNcZH+wkLcPXKEGkh5KUtloFnNyCY8Q3LI8dEs55qJoWK+3AdsYO6hg8GoeW8OaZIMa0PmImnQ++B8qW7MkVZ1z0edm1WzJWGhjCeqvlpxw1BFIbIY6FvE5Qx2sqIkAbaMvTNP+p7kCXqHCCmCEimD5BIXKDe8vDX4R0b322FYrAaZ3BYe7Wr1PdOIeip8FchbRItVUDRwbntnSn910ljotSbdhz59MLaECgYEA303njQolsUtN9SGpwtgraAAIc79rGvMRBzvl6WHGvxAj3wc6LsbfDVprBYTTRn+x4i90h1ZtUgMtBMq860BsrOXPsOAAeAN2+0pWTCyOOetFJaz+UaazkWrD05EyDi2sdIISXXm3NjGmETZZQ3DAxmWfqkKm7pj6X0I8KgMAKdUCgYEAvRsMuTiSfZNpH6gKUteZyj06zlPJYVmZaku3Ev3WcqIi+ALSvA0sGiHAQzvAQBLvm5QpjispEQVjmDDl8FZzM0AK8xFI3AHnvfz0V4RvZQiITDYDNH5JKpIqREjk5B35zKMsC2B8hFSdz1MQMNviIKtK7TVK6gVXpXeHV56R2XkCgYBxseCrVvdKBeVzYehlZFr1YM7s4BFQb2HEJSMyaK4gqb8Z1zwwhOEck2TaMn6/7lRLrY3POpn+n89UGUWHIg4zGe9dRy/16w2xdG/r6OOhacCTJjl2qUHUUF/UYKDzxx1/VJq1LTuzRCQhr2JTjB0a5rMcpUMJSKpxxe+MFGsI4QKBgE6PYDfKUsEsbi2ih7dmrhMgYL+6exygNmW8Uq/nGCHDTbeupJFO/5VTlCXaOsHjm/sMED9pmsPhc0CUf9HaeD1AS8wy0AvmFroMzg1VjVlxs/3FKouyMv8mfbykc8J42X+0AeYwDUqERJMOtNk3X7emsZfd3eNemMy63VT/ISQxAoGAIuPqBPc4VjYlXXLCwkXiDm8Woks22E3y0Xa3BF8fr9fea350DIbog46Fc6OX/J2/pdBiKClMVmh9Wv0UI5ZqFdjtsiQ0uKDqkcWww2YTCxQKNiNxPthiuVXoadt+BvYo0DfPRt9sqry34w8EwE5l9mzO/PcXxNCqq2J26Gc9enc=");
		System.out.println("sign=" + sign);
	}

}
