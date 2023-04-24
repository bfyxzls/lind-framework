package com.lind.uaa.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Exrickx
 */
@Slf4j
public class ResponseUtil {

	/**
	 * 使用response输出JSON
	 * @param response
	 * @param resultMap
	 */
	public static void out(ServletResponse response, Map<String, Object> resultMap) {
		ObjectMapper objectMapper = new ObjectMapper();
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			out = response.getWriter();
			out.println(objectMapper.writeValueAsString(resultMap));
		}
		catch (Exception e) {
			log.error(e + "输出JSON出错");
		}
		finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	public static Map<String, Object> resultMap(boolean flag, Integer code, String msg) {

		Map<String, Object> resultMap = new HashMap<>(16);
		resultMap.put("success", flag);
		resultMap.put("message", msg);
		resultMap.put("code", code);
		resultMap.put("timestamp", System.currentTimeMillis());
		return resultMap;
	}

	public static Map<String, Object> resultMap(boolean flag, Integer code, String msg, Object data) {

		Map<String, Object> resultMap = new HashMap<>(16);
		resultMap.put("success", flag);
		resultMap.put("message", msg);
		resultMap.put("code", code);
		resultMap.put("timestamp", System.currentTimeMillis());
		resultMap.put("result", data);
		return resultMap;
	}

}
