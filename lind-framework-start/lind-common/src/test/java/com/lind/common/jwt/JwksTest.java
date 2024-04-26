package com.lind.common.jwt;

import com.auth0.jwk.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

/**
 * @author lind
 * @date 2024/4/23 10:04
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JwksTest {

	static String jwksUrl = "http://192.168.4.26:8080/auth/realms/fabao/protocol/openid-connect/certs";
	static String certsId = "E_6ih35yTLJMieI0vqg9MmTQrJ6RcUSxiXeNdcMaoYk";
	static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJFXzZpaDM1eVRMSk1pZUkwdnFnOU1tVFFySjZSY1VTeGlYZU5kY01hb1lrIn0.eyJleHAiOjE3MTM4NDAxODIsImlhdCI6MTcxMzgzODM4MiwianRpIjoiNDU0ZDZhMWUtZmVjZS00ZmQ0LWJkZDgtOWU0ZDAwYjY2NDNlIiwiaXNzIjoiaHR0cDovLzE5Mi4xNjguNC4yNjo4MDgwL2F1dGgvcmVhbG1zL2ZhYmFvIiwiYXVkIjpbInJlYWxtLW1hbmFnZW1lbnQiLCJub2RlLXNlcnZpY2VzLXRlc3QiLCJhY2NvdW50Il0sInN1YiI6IjM0N2M5ZTllLTA3NmMtNDVlMy1iZTc0LWM0ODJmZmZjYzZlNSIsInR5cCI6IkJlYXJlciIsImF6cCI6InBrdWxhdyIsInNlc3Npb25fc3RhdGUiOiJmOGI1ZTY2MC1jMzgzLTRhMGMtYjY4Ny03NjdlOWVjMmM2YWEiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIioiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIue7hOeuoeeQhuWRmCIsImZlZGVyYXRlZCIsImxhdyIsImpvdXJuYWwiLCJjYXJzaSIsImNhc2UiXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctcmVhbG0iLCJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidmlldy1hdXRob3JpemF0aW9uIiwicXVlcnktY2xpZW50cyIsInF1ZXJ5LXVzZXJzIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiLCJxdWVyeS1ncm91cHMiXX0sIm5vZGUtc2VydmljZXMtdGVzdCI6eyJyb2xlcyI6WyJncHQiXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LWFwcGxpY2F0aW9ucyIsInZpZXctY29uc2VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwibWFuYWdlLWNvbnNlbnQiLCJkZWxldGUtYWNjb3VudCIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGF1dGhvcml6YXRpb24gcm9sZXMgcGt1bGF3LWV4dGVuc2lvbnMgZW1haWwgdjYgcHJvZmlsZSIsImdyb3VwVXNlcklkIjoiMzQ3YzllOWUtMDc2Yy00NWUzLWJlNzQtYzQ4MmZmZmNjNmU1IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJsb2dpblR5cGUiOiJwYXNzd29yZCIsImdyb3VwVXNlck5hbWUiOiJ0ZXN0IiwibG9naW5JcCI6IjE5Mi4xNjguNC4xMDEiLCJuaWNrbmFtZSI6InRlc3QiLCJpc0ZlZGVyYWxVc2VyIjowLCJwaG9uZU51bWJlclZlcmlmaWVkIjoiZmFsc2UiLCJpc0dyb3VwVXNlciI6IjEiLCJleHRlbnNpb25fcm9sZXMiOnsid2VpeGluIjpbImxhdyIsImpvdXJuYWwiLCJjYXNlIl19LCJsb2dpblRpbWVQZXJpb2QiOnsiYXV0byI6MTk0LCJwYXNzd29yZCI6MCwid2VpeGluIjozMDF9LCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0In0.Nmz1rXHn55hjLrrMBZ2E3bbRrKiMlexe5676ZrnBnv6kSzQsfZbUzFVJAVdnVfSxwYmvD2nLuYUO0q3oCThKHGGXDdGD3ExxoO9MDyx_yR5lRRPg_XMaTJWMfC3HottfGfoDL71XLl9nnN2qyxOBOfvZqTHaqv6EzSYvrjbqbm1VAWzE5zGg0g92obdr2GE1TEf5wiNhU0s3-DyrSmHX-BprNEZKs-uykirroyd1bqzAGsyCGTCXs-1qIf13_0Y1ayuTXwkhig2s0ROWhJXbiAIitBkidChLFDf4kxPG7HwEFDN10Eoki0qjzyrU3zvjDwW0_FgGgrTS2EnaSLixCQ";

	@Autowired
	RedisTemplate<String, Object> restTemplate;

	// 校验jwt
	@Test
	public void valid() throws MalformedURLException, JwkException {
		Jwk jwk = new UrlJwkProvider(new URL(jwksUrl)).get(certsId);
		Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

		DecodedJWT jwt = JWT.decode(token);
		// check JWT is valid
		algorithm.verify(jwt);

		Date expiryDate = jwt.getExpiresAt();
		if (expiryDate.before(new Date()))
			log.error("token is expired");

	}

	/**
	 * 缓存包装器.
	 * @throws MalformedURLException
	 * @throws JwkException
	 */
	@Test
	public void jwkReadCacheWrapper() throws MalformedURLException, JwkException {
		UrlJwkProvider jwkProvider = new UrlJwkProvider(new URL(jwksUrl));
		JwkProvider cachedJwkProvider = new GuavaCachedJwkProvider(jwkProvider);
		Jwk jwk = cachedJwkProvider.get("certsId");
		Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

		DecodedJWT jwt = JWT.decode(token);
		// check JWT is valid
		algorithm.verify(jwt);
	}

}
