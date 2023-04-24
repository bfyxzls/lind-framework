package com.lind.common;

import com.lind.common.util.FreeMarkerUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateTest {

	@Test
	public void testPath() {
		FreeMarkerUtil freeMarkerUtil = new FreeMarkerUtil();
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> user = new HashMap<>();
		List<Map<String, Object>> users = new ArrayList<>();
		user.put("name", "lind");
		users.add(user);

		map.put("title", "ok");
		map.put("redCss", "style:color:red");
		map.put("users", users);
		String html = freeMarkerUtil.processTemplate(map, "index.ftl", "user");
		System.out.println(html);
	}

}
