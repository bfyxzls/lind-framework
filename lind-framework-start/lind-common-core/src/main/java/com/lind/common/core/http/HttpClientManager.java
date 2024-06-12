package com.lind.common.core.http;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author lind
 * @date 2024/6/4 14:39
 * @since 1.0.0
 */
public class HttpClientManager {

	private static CloseableHttpClient httpClient;

	static {
		// 创建连接池管理器
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		// 设置最大连接数
		connectionManager.setMaxTotal(100);
		// 设置每个路由的最大连接数
		connectionManager.setDefaultMaxPerRoute(50);

		// 创建 HttpClient 实例，并关联连接池管理器
		httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
	}

	public static CloseableHttpClient getHttpClient() {
		return httpClient;
	}

}
