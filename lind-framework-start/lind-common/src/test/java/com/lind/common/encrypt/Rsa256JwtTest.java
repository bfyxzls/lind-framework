package com.lind.common.encrypt;

import org.junit.Test;

/**
 * @author lind
 * @date 2023/11/7 17:42
 * @since 1.0.0
 */
public class Rsa256JwtTest {

	String PUBLIC_KEY_STRING = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyOCNCy8x280vYwpVl26ToWPXkTFMdkSkLQ+UB6uCK5/1Ojw1+ZqKWhAZg4bNTG03QtG7xJOPJUH0S2Gf2CNbZjm2OEe1ZeY08JCFThZ4h5D6iuAOarmYjlSnzhdW8jpiSkm0Okci+xZkC1dWW2twjGgc0/E6o9JXYEggfrDWbllpGQOib+lx+mU51e0OMqu8TwSrLoa/d0a2dawPJnU3l1a6lyvhD7W/asUphMNeLKgmNf4wf92wElLlDkB2Qozfg1J/6l4JzBbgVLLljO0j7VKv7seyAnul55KmRd9tUjpdecf0FQPFZiNOTW+g4ivMNBQZvKAnsfgDboxy1PzW1QIDAQAB";

	String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJFXzZpaDM1eVRMSk1pZUkwdnFnOU1tVFFySjZSY1VTeGlYZU5kY01hb1lrIn0.eyJleHAiOjE2OTk5NDAxNTEsImlhdCI6MTY5OTMzNTM1MSwianRpIjoiYTRiNDA0MGQtZjZlNi00YTIwLWE4ZmQtODY2MzNkZDVhZWJlIiwiaXNzIjoiaHR0cHM6Ly90ZXN0Y2FzLnBrdWxhdy5jb206MTgwODEvYXV0aC9yZWFsbXMvZmFiYW8iLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsIm5vZGUtc2VydmljZXMtdGVzdCIsImFjY291bnQiXSwic3ViIjoiMzQ3YzllOWUtMDc2Yy00NWUzLWJlNzQtYzQ4MmZmZmNjNmU1IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZGVtb2NsaWVudCIsInNlc3Npb25fc3RhdGUiOiI5YmY0YTE2Mi03NWEwLTRlMDYtOTYxNi03NjNmNDU3M2VlNGYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImRlbW9jbGllbnQiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIue7hOeuoeeQhuWRmCIsImxhdyIsImpvdXJuYWwiLCJzdXBlcnZpc2lvbiIsImNhc2UiXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctcmVhbG0iLCJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidmlldy1hdXRob3JpemF0aW9uIiwicXVlcnktY2xpZW50cyIsInF1ZXJ5LXVzZXJzIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiLCJxdWVyeS1ncm91cHMiXX0sIm5vZGUtc2VydmljZXMtdGVzdCI6eyJyb2xlcyI6WyJncHQiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LWFwcGxpY2F0aW9ucyIsInZpZXctY29uc2VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwibWFuYWdlLWNvbnNlbnQiLCJkZWxldGUtYWNjb3VudCIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHJvbGVzIHBrdWxhdy1leHRlbnNpb25zIGVtYWlsIHY2IHByb2ZpbGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImxvZ2luVHlwZSI6InBhc3N3b3JkIiwiZ3JvdXBVc2VyTmFtZSI6InRlc3QiLCJpcEFkZHJlc3MiOiIxOTIuMTY4LjAwNC4xNzZ-MTkyLjE2OC4wMDQuMTc2IiwiZXh0ZW5zaW9uX3JvbGVzIjp7IndlaXhpbiI6WyJsYXciLCJqb3VybmFsIiwiY2FzZSJdfSwibG9naW5UaW1lUGVyaW9kIjp7ImF1dG8iOjI2LCJwYXNzd29yZCI6MCwid2VpeGluIjoxMzN9LCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0IiwiZ3JvdXBVc2VySWQiOiIzNDdjOWU5ZS0wNzZjLTQ1ZTMtYmU3NC1jNDgyZmZmY2M2ZTUiLCJwaG9uZU51bWJlciI6IjEzNTIxOTcyOTkxIiwibmlja25hbWUiOiJ0ZXN0IiwiaXNGZWRlcmFsVXNlciI6MCwicGhvbmVOdW1iZXJWZXJpZmllZCI6ImZhbHNlIiwiaXNHcm91cFVzZXIiOiIxIiwiZW1haWwiOiJ6emwxMjNAc2luYS5jb20ifQ.W2pm6i52GelLVDyQSIIVLy06-6pvb74vqVQPrVp2xAmvYAYG4BELAMx4dyO_JE1_6WcuHWZF2btvweE2gSxQmCjG9Z_djAwokVYzLDMgC72CK0D4J7HDRGEm2R-fjksQgvNZ6ucV0R_KYuDfZKotPUGRh6YDUuuu_Rpr7ZZwCGoDbXFgy_UBRp5pycyIFJuH5rx_IyJgpODpoe3bI-XE-T0WqGMgUHyw49czDPyrOzNwOzBIJ72rnPCRz-xEZs0pW7byt8_PpDJHC6TQ1goX0zmIYuX6UpdBRR0fK4FIVna313jV_bs8V1xFYLUokoxBknrp0UWJNYZf_C-UXEpIUA";

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
		String[] arr = token.split("\\.");
		String sign = convertBase64URLToBase64(arr[2]);

		String body = arr[0] + "." + arr[1];
		boolean result = RSAUtils.verify(body, RSAUtils.getPublicKey(PUBLIC_KEY_STRING), sign);
		System.out.println(result);
	}

}
