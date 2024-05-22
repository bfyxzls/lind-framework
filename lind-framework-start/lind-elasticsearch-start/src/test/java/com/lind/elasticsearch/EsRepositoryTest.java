package com.lind.elasticsearch;

import com.lind.elasticsearch.entity.EsDto;
import com.lind.elasticsearch.repository.EsRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * springboot2.6.0+spring-boot-starter-data-elasticsearch2.6.0+spring-data-elasticsearch4.3.0
 * 增，删，改和主键查询
 * @author lind
 * @date 2024/5/21 17:17
 * @since 1.0.0
 */
@SpringBootTest
public class EsRepositoryTest {

	@Autowired
	EsRepo esRepo;

	// 如果索引不存在，通过save()方法是可以根据实体上的注解建立索引的
	@Test
	public void test_insert() {
		EsDto esDto = new EsDto();
		esDto.setId("4");
		esDto.setSex(1);
		esDto.setEmail("bfyxzls@sina.com");
		esRepo.save(esDto);
	}

	@Test
	public void test_update() {
		EsDto esDto = new EsDto();
		esDto.setId("1");
		esDto.setName("bfyxzls");
		esDto.setDesc("中华人民共和国，伟大复兴之路");
		esRepo.save(esDto);
	}

	@Test
	public void test_delete() {
		esRepo.deleteById("1");
	}

}
