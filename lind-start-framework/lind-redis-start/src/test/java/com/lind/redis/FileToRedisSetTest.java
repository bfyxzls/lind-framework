package com.lind.redis;

import cn.hutool.core.io.FileUtil;
import com.lind.redis.config.LettuceRedisAutoConfigure;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 文件读取到redis的set里，保证唯一性.
 *
 * @author lind
 * @date 2023/2/2 9:47
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@Slf4j
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
@SpringBootTest(classes = { LettuceConnectionFactory.class, LettuceRedisAutoConfigure.class })
public class FileToRedisSetTest {

	@Autowired
	StringRedisTemplate redisService;

	@Test
	public void readTo() throws IOException {
		InputStream inputStream = FileUtil.getInputStream("d:\\缺失数据\\csv_2.txt");

		InputStreamReader read = new InputStreamReader(inputStream, "utf-8");// 考虑到编码格式
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		int i = 1;
		while ((lineTxt = bufferedReader.readLine()) != null) {
			redisService.opsForSet().add("queshi", lineTxt);
			log.info("line:{}", i);
			i++;
		}
		read.close();
		inputStream.close();
	}

	@Test
	public void writeTo() throws IOException {

	}

}
