package com.lind.mybatis.test;

import com.github.pagehelper.PageHelper;
import com.lind.mybatis.dao.UserDao;
import com.lind.mybatis.entity.TUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author lind
 * @date 2024/7/18 16:25
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest
public class MybatisPagerTest {

	@Autowired
	UserDao userDao;

	@Test
	public void testPage() {
		PageHelper.startPage(1, 4);
		List<TUser> users = userDao.queryUsers();

		log.info("users:{}", users);
	}

}
