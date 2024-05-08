package com.lind.common.othertest;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import java.io.IOException;

/**
 * @author lind
 * @date 2023/7/7 17:39
 * @since 1.0.0
 */
@Slf4j
public class HttpClientTest {

	@SneakyThrows
	@Test
	public void options() {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpOptions httpOptions = new HttpOptions("https://cas.pkulaw.com/auth");
			HttpResponse response = httpClient.execute(httpOptions);

			// 处理OPTIONS请求的响应
			log.info("response={}", response);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
