package com.lind.elasticsearch;

import com.lind.elasticsearch.entity.EsDto;
import com.lind.elasticsearch.repository.EsRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lind
 * @date 2024/5/21 17:17
 * @since 1.0.0
 */
@SpringBootTest
public class EsRepositoryTest {

	@Autowired
	EsRepo esRepo;

	@Test
	public void test_insert() {
		EsDto esDto = new EsDto();
		esDto.setId("1");
		esDto.setEmail("bfyxzls@sina.com");
		esRepo.save(esDto);
	}

	@Test
	public void test_update() {
		EsDto esDto = new EsDto();
		esDto.setId("1");
		esDto.setName("bfyxzls");
		esRepo.save(esDto);
	}

	@Test
	public void test_delete() {
		esRepo.deleteById("1");
	}

}
