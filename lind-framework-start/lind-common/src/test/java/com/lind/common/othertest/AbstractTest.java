package com.lind.common.othertest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest {

	@Autowired
	protected ObjectMapper objectMapper;

	/**
	 * 获取资源文件路径.
	 * @param fileName
	 * @return
	 */
	public String getResourceFilePath(String fileName) {
		String path = ClassUtils.getDefaultClassLoader().getResource(fileName).getPath();
		return path;
	}

	/**
	 * 获取文件流.
	 * @param path 文件路径
	 * @return
	 */
	public String getStringFromJson(String path, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(path), charset);
	}

	/**
	 * 将json转换为对象.
	 * @param path 文件路径
	 */
	public <T> T fromJson(String path, Class<T> cls) throws IOException {
		return objectMapper.readValue(this.getStringFromJson(path, Charsets.UTF_8), cls);
	}

	public <T> T fromJsonType(String path, Class<T> cls) throws IOException {
		String json = this.getStringFromJson(path, Charsets.UTF_8);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.WRAPPER_ARRAY);
		return om.readValue(json, cls);
	}

}
