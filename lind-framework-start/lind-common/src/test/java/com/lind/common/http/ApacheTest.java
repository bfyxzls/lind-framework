package com.lind.common.http;

import com.alibaba.fastjson.JSON;
import com.lind.common.core.jackson.serialization.JsonSerialization;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 有连接池的概念.
 *
 * @author lind
 * @date 2023/1/30 9:37
 * @since 1.0.0
 */
@Slf4j
public class ApacheTest {

	static List<Header> headers;
	static CloseableHttpClient httpClient;
	static {
		// 公共请求头
		headers = new ArrayList<>();
		/*
		 * headers.add(new BasicHeader(HttpHeaders.CONTENT_TYPE,
		 * ContentType.APPLICATION_JSON.getMimeType())); headers.add(new
		 * BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, x-gzip, deflate"));
		 * headers.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
		 */

		// 创建 一个默认的 httpClient
		httpClient = HttpClients.custom().setDefaultHeaders(headers) // 设置默认请求头
				.build();
	}

	@Test
	public void kcPublicKey() throws IOException {

		HttpGet getMethod = new HttpGet("http://192.168.4.26:8080/auth/realms/fabao");
		httpClient = HttpClients.custom().build();
		CloseableHttpResponse response = httpClient.execute(getMethod);
		int code = response.getStatusLine().getStatusCode();
		log.info("code={}", code);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		try {
			Map map = JsonSerialization.readValue(is, Map.class);
			log.info("public_key={}", map.get("public_key"));
		}
		finally {
			is.close();

		}
	}

	/**
	 * 批量请求高延时接口，如果不采用连接池，tcp每次连接会占用一个端口，导致端口被用完.
	 */
	@Test
	public void bulk() throws InterruptedException, IOException {
		get();
	}

	@Test
	public void get() throws IOException {
		httpClient = HttpClients.custom().setDefaultHeaders(headers) // 设置默认请求头
				.build();
		String name = URLEncoder.encode("张三", "utf-8");
		// 请求路径及参数
		String url = "http://localhost:8181/s?wd=" + name;

		// 创建 GET 请求对象
		HttpGet httpGet = new HttpGet(url);

		// 调用 HttpClient 的 execute 方法执行请求
		CloseableHttpResponse response = httpClient.execute(httpGet);
		// 获取请求状态
		int code = response.getStatusLine().getStatusCode();
		// 如果请求成功
		if (code == HttpStatus.SC_OK) {
			log.info("响应结果200");
		}

	}

	@Test
	public void postJson() throws IOException {
		// 请求参数
		String url = "http://localhost:10010/user/body";
		HttpPost httpPost = new HttpPost(url);
		// 构建对象
		User user = User.builder().name("zhangsan").age(20)
				.address(User.Address.builder().county("china").city("beijing").build()).build();

		// 创建 字符串实体对象
		HttpEntity httpEntity = new StringEntity(JSON.toJSONString(user));
		httpPost.setEntity(httpEntity);
		// 发送 POST 请求
		httpClient.execute(httpPost);
	}

	@Test
	public void postForm() throws IOException {
		String url = "http://localhost:10010/user/body";
		HttpPost httpPost = new HttpPost(url);
		// 创建 ContentType 对象为 form 表单模式
		ContentType contentType = ContentType.create("application/x-www-form-urlencoded", StandardCharsets.UTF_8);
		// 添加到 HttpPost 头中
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, contentType.getMimeType());
		HttpEntity httpEntity = new UrlEncodedFormEntity(
				Arrays.asList(new BasicNameValuePair("name", "张三"), new BasicNameValuePair("age", "20")),
				StandardCharsets.UTF_8);
		// 把 HttpEntity 设置到 HttpPost 中
		httpPost.setEntity(httpEntity);
		httpClient.execute(httpPost);
	}

	@Data
	@Builder
	static class User {

		private String name;

		private Integer age;

		private Address address;

		@Data
		@Builder
		static class Address {

			private String county;

			private String city;

		}

	}

}
