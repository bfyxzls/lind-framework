package com.lind.common.json;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lind
 * @date 2023/2/8 10:18
 * @since 1.0.0
 */
public class ReadTest {

	static final Logger logger = LoggerFactory.getLogger(ReadTest.class);

	private static String trimTrailingCharacter(String string, char c) {
		if (string.length() > 0 && string.charAt(string.length() - 1) == c) {
			return string.substring(0, string.length() - 1);
		}
		return string;
	}

	private static String trimLeadingCharacter(String string, char c) {
		if (string.length() > 0 && string.charAt(0) == c) {
			return string.substring(1);
		}
		return string;
	}

	@Test
	public void updateR() throws IOException {
		// 读取JSON文件内容
		String filePath = "D:\\1.json";
		String content = new String(Files.readAllBytes(Paths.get(filePath)));

		// 使用JsonParser解析JSON字符串
		JSONArray jsonObject = JSONUtil.parseArray(content);
		jsonObject.forEach(o -> {
			String url = "https://lawyer-mgr.demo.com/prod-api/lawyer/lawyerInfo";
			Map<String, Object> map = new HashMap<>();
			map.put("gid", JSONUtil.parseObj(o).getStr("gid"));
			map.put("phone", JSONUtil.parseObj(o).getStr("phone"));
			try {
				HttpResponse response = HttpRequest.put(url).header("Content-Type", "application/json") // 添加Content-Type头部
						.header("Authorization",
								"Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjBkZDNiN2FjLWMzODktNGE0My05YmU1LTk2MDZjNzc1MmE5MSJ9.zMYZsPhH8zOOFNeTzb27uQWznKPo26INtyNzye6ms71MCndFIrFl83e9C59XWiY6qa0Y-ijRe2IHPAq7jtBj0A") // 添加Authorization头部
						.body(new ObjectMapper().writeValueAsString(map)).execute();
				logger.info(response.body());
			}
			catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			;
		});
	}

	@Test
	public void readJsonString() {
		DemoEntity entity = DemoEntity.builder().title("test-1").count(10)
				.sons(Arrays.asList(DemoEntity.builder().title("test-1-1").count(1).build())).build();
		String result = BasicJson.toJson(entity);
		logger.debug("json={}", result);
		Map map = BasicJson.parseMap(result);
		logger.debug("map={}", map);
	}

	@Test
	public void leadingAndTrialing() {
		String msg = "(a=123)";
		String result = trimLeadingCharacter(trimTrailingCharacter(msg, ')'), '(');
		logger.debug("result:{}", result);
		trimTrailingCharacter(null, ')');
	}

}
