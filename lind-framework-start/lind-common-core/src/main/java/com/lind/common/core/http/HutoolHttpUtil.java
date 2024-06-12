package com.lind.common.core.http;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HutoolHttpUtil {

	private HutoolHttpUtil() {
	}

	public static JSONObject get(String url, Map<String, Object> queryParams) throws IOException {
		return get(url, queryParams, new HashMap(1));
	}

	public static JSONObject get(String url, Map<String, Object> queryParams, Map<String, String> headers)
			throws IOException {
		String body = ((HttpRequest) HttpRequest.get(url).form(queryParams).addHeaders(headers)).execute().body();
		return JSONObject.parseObject(body);
	}

	public static JSONObject post(String url, String json, Map<String, String> headers) {
		String body = ((HttpRequest) HttpRequest.post(url).body(json).addHeaders(headers)).execute().body();
		return JSONObject.parseObject(body);
	}

}
