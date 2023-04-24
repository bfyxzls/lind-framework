package com.lind.uaa.keycloak.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lind.uaa.keycloak.config.UaaProperties;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.rotation.AdapterTokenVerifier;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import static com.lind.uaa.keycloak.config.Constant.COOKIE_IS_LOGIN;
import static com.lind.uaa.keycloak.config.Constant.TOKEN_APPLET;
import static com.lind.uaa.keycloak.config.Constant.TOKEN_AUTHORIZATION_CODE_REDIRECT;
import static com.lind.uaa.keycloak.config.Constant.TOKEN_AUTHORIZATION_CODE_RESPONSE;
import static com.lind.uaa.keycloak.config.Constant.TOKEN_CLIENT_CREDENTIALS_LOGIN;
import static com.lind.uaa.keycloak.config.Constant.TOKEN_PASSWORD_LOGIN;
import static com.lind.uaa.keycloak.config.Constant.getCurrentHost;

@RestController
@Slf4j
public class TokenController {

	@Autowired
	KeycloakSpringBootProperties keycloakSpringBootProperties;

	@Autowired
	UaaProperties uaaProperties;

	/**
	 * 直接在响应体上输出token信息.
	 * @param response
	 * @param map
	 * @param headers
	 * @throws IOException
	 */
	private void writeTokenResponse(HttpServletResponse response, MultiValueMap<String, String> map,
			HttpHeaders headers) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-type", "application/json;charset=utf-8");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		// 获取OutputStream输出流
		OutputStream outputStream = response.getOutputStream();
		// 将字符转换成字节数组，指定以UTF-8编码进行转换
		String result = JSON.toJSONString(restTemplate.postForEntity(getTokenUri(), request, Map.class).getBody());
		// 使用OutputStream流向客户端输出字节数组
		JSONObject jsonNode = JSONObject.parseObject(result);
		JSONObject.writeJSONString(outputStream, jsonNode);
	}

	/**
	 * 重定向并附加token信息.
	 * @param response
	 * @param map
	 * @param headers
	 * @throws IOException
	 */
	private void writeTokenRedirect(HttpServletResponse response, MultiValueMap<String, String> map,
			HttpHeaders headers, HttpServletRequest httpServletRequest) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		Map maps = restTemplate.postForEntity(getTokenUri(), request, Map.class).getBody();

		// 向自己域名写cookie
		Cookie cookie = new Cookie(COOKIE_IS_LOGIN, "1");
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		// 构建重定向地址，如果refer_uri为空，则走默认的重定向地址
		String redirect = httpServletRequest.getParameter("refer_uri");
		if (redirect == null) {
			redirect = uaaProperties.getRedirectUri();
		}
		response.sendRedirect(
				redirect + "?access_token=" + maps.get("access_token") + "&refresh_token=" + maps.get("refresh_token"));
	}

	/**
	 * 得到获取code的URI.
	 * @return
	 */
	private String getCodeUri(String callUrl) {

		return String.format(
				"%s/realms/%s/protocol/openid-connect/auth?scope=openid&response_type=code&client_id=%s&redirect_uri=%s",
				keycloakSpringBootProperties.getAuthServerUrl(), keycloakSpringBootProperties.getRealm(),
				keycloakSpringBootProperties.getResource(), callUrl);
	}

	/**
	 * 得到获取tokenURI.
	 * @return
	 */
	private String getTokenUri() {
		return String.format("%s/realms/%s/protocol/openid-connect/token",
				keycloakSpringBootProperties.getAuthServerUrl(), keycloakSpringBootProperties.getRealm());
	}

	/**
	 * 通过code获取token,并输出在页面上.
	 * @param code
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation("授权码认证")
	@GetMapping(path = TOKEN_AUTHORIZATION_CODE_RESPONSE)
	public void authorizationCodeLogin(@RequestParam(required = false) String code, HttpServletResponse response,
			HttpServletRequest httpServletRequest) throws IOException {
		String callUrl = getCurrentHost(httpServletRequest, TOKEN_AUTHORIZATION_CODE_RESPONSE);
		if (StringUtils.isBlank(code)) {
			// step1
			response.sendRedirect(getCodeUri(callUrl));
		}
		else {
			// step2
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("grant_type", "authorization_code");
			map.add("code", code);
			map.add("client_id", keycloakSpringBootProperties.getResource());
			map.add("client_secret", keycloakSpringBootProperties.getClientKeyPassword());
			map.add("redirect_uri", callUrl);
			writeTokenResponse(response, map, headers);
		}

	}

	/**
	 * 通过code获取token,并重定向.
	 * @param code
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation("授权码认证")
	@GetMapping(path = TOKEN_AUTHORIZATION_CODE_REDIRECT)
	public void authorizationCodeLoginAndRedirect(@RequestParam(required = false) String code,
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		String callUrl = getCurrentHost(request, TOKEN_AUTHORIZATION_CODE_REDIRECT);
		if (StringUtils.isBlank(code)) {
			// step1
			response.sendRedirect(getCodeUri(callUrl));
		}
		else {
			// step2
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("grant_type", "authorization_code");
			map.add("code", code);
			map.add("client_id", keycloakSpringBootProperties.getResource());
			map.add("client_secret", keycloakSpringBootProperties.getClientKeyPassword());
			map.add("redirect_uri", callUrl);
			writeTokenRedirect(response, map, headers, request);
		}
	}

	/**
	 * 客户端认证.
	 * @param scope
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation("客户端认证")
	@GetMapping(path = TOKEN_CLIENT_CREDENTIALS_LOGIN)
	public void clientCredentialsLogin(@RequestParam(required = false) String scope, HttpServletResponse response)
			throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grant_type", "client_credentials");
		map.add("client_id", keycloakSpringBootProperties.getResource());
		if (StringUtils.isNoneBlank(scope)) {
			map.add("scope", scope);
		}
		map.add("client_secret", keycloakSpringBootProperties.getClientKeyPassword());
		writeTokenResponse(response, map, headers);
	}

	/**
	 * 密码认证.
	 * @param username
	 * @param password
	 * @param response
	 * @throws IOException
	 */
	@ApiOperation("密码认证")
	@PostMapping(path = TOKEN_PASSWORD_LOGIN)
	public void passwordLogin(String username, String password, HttpServletResponse response) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grant_type", "password");
		map.add("client_id", keycloakSpringBootProperties.getResource());
		map.add("client_secret", keycloakSpringBootProperties.getClientKeyPassword());
		map.add("username", username);
		map.add("password", password);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		writeTokenResponse(response, map, headers);
	}

	/**
	 * weixin unionId认证,通过unionId认证对应的用户.
	 * 回调地址是规定的，不能使用authorizationCodeRedirect,而能使用authorizationCodeResponse,所以它不需要配置回调地址了.
	 * keycloak服务端需要添加keycloak-services-social-weixin-applet组件
	 * @param unionId
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ApiOperation("weixin-unionId认证")
	@GetMapping(path = TOKEN_APPLET)
	public ResponseEntity login(String unionId, HttpServletRequest request) throws IOException {
		HttpContext httpContext = new BasicHttpContext();
		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(10000).build();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String callbackUrl = getCurrentHost(request) + TOKEN_AUTHORIZATION_CODE_RESPONSE;
		String kcUrl = keycloakSpringBootProperties.getAuthServerUrl() + "/realms/"
				+ keycloakSpringBootProperties.getRealm()
				+ "/protocol/openid-connect/auth?scope=openid&response_type=code&client_id="
				+ keycloakSpringBootProperties.getResource() + "&unionId=" + unionId + "&redirect_uri=" + callbackUrl;
		HttpGet httpGet = new HttpGet(kcUrl);
		httpGet.setConfig(defaultRequestConfig);
		HttpResponse response = httpclient.execute(httpGet, httpContext);
		org.apache.http.HttpEntity entity = response.getEntity();
		System.out.println("1.Get Response Status: " + response.getStatusLine());
		String readContent = null;
		if (entity != null) {
			// getResponse
			InputStream in = entity.getContent();
			int count = 0;
			while (count == 0) {
				count = Integer.parseInt("" + entity.getContentLength());
			}
			byte[] bytes = new byte[count];
			int readCount = 0; // 已经成功读取的字节的个数
			while (readCount <= count) {
				if (readCount == count)
					break;
				readCount += in.read(bytes, readCount, count - readCount);
			}
			// 转换成字符串
			readContent = new String(bytes, 0, readCount, "UTF-8");

			System.out.println("Response token:\n" + readContent);
		}

		ResponseEntity bodyBuilder = ResponseEntity.ok().header("Content_Type", "application/json;charset=UTF-8")
				.body(readContent);
		return bodyBuilder;
	}

	/**
	 * 验证token的合法性，也可以使用kc服务器的rest方法/auth/realms/{realmId}/{clientId}?token={your token}
	 * @param token
	 * @return
	 */
	@GetMapping("/token/verify")
	public ResponseEntity verifyToken(String token) {
		AccessToken accessToken = null;
		try {
			// 1、设置client配置信息
			AdapterConfig adapterConfig = new AdapterConfig();
			// realm name
			adapterConfig.setRealm(keycloakSpringBootProperties.getRealm());
			// client_id
			adapterConfig.setResource(keycloakSpringBootProperties.getResource());
			// 认证中心keycloak地址
			adapterConfig.setAuthServerUrl(keycloakSpringBootProperties.getAuthServerUrl());
			// 访问https接口时，禁用证书检查。
			adapterConfig.setDisableTrustManager(true);
			// 2、根据client配置信息构建KeycloakDeployment对象
			KeycloakDeployment deployment = KeycloakDeploymentBuilder.build(adapterConfig);
			// 3、执行token签名验证和有效性检查（不通过会抛异常）
			accessToken = AdapterTokenVerifier.verifyToken(token, deployment);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (accessToken != null) {
			return ResponseEntity.ok(true);
		}
		else {
			return ResponseEntity.ok(false);
		}
	}

}
