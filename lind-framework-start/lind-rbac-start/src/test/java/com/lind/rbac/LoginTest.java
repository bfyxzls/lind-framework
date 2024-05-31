package com.lind.rbac;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
public class LoginTest {

	protected MockMvc mockMvc;

	ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	public void tokenIndex() throws Exception {
		mockMvc.perform(get("/token/index")).andDo(print()).andExpect(status().isOk());

	}

	@Test
	public void test200() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("username", "Jack");
		map.put("password", "123456");
		String requestBody = objectMapper.writeValueAsString(map);
		mockMvc.perform(post("/login").accept(MediaType.APPLICATION_JSON) // accept指定客户端能够接收的内容类型
				.contentType(MediaType.APPLICATION_JSON) // 代表发送端（客户端|服务器）发送的实体数据的数据类型
				.content(requestBody)).andDo(print()).andExpect(status().isOk());
	}

}
