package com.lind.mybatis;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lind.common.dto.PageDTO;
import com.lind.common.dto.PageParam;
import com.lind.mybatis.config.Constant;
import com.lind.mybatis.config.MybatisAutoConfiguration;
import com.lind.mybatis.dao.LogDao;
import com.lind.mybatis.entity.TLog;
import com.lind.mybatis.entity.TUser;
import com.lind.mybatis.service.UserService;
import com.lind.mybatis.service.UserServiceImpl;
import com.lind.mybatis.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@ActiveProfiles("integTest")
@EnableTransactionManagement
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { UserServiceImpl.class, UserAuditAware.class, MybatisAutoConfiguration.class,
		DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,
		MybatisPlusAutoConfiguration.class, DbConfig.class })
@MapperScan("com.lind.mybatis.dao")
public class BasicTest {

	@Autowired
	UserService userService;

	@Autowired
	LogDao logDao;

	@Test
	public void insert() {
		TUser user = new TUser();
		user.setUsername("lind");
		userService.insert(user);
		print("lind");
	}

	@Test
	public void update() throws InterruptedException {
		TUser user = new TUser();
		user.setUsername("lind");
		userService.insert(user);
		Thread.sleep(1000);
		user.setUsername("lind修改");
		userService.update(user);
		print("lind修改");
	}

	@Test
	public void servicePageList() {
		PageParam pageVo = new PageDTO();
		pageVo.setPageNumber(1);
		pageVo.setPageSize(2);
		userService.findByCondition(PageUtil.initPage(pageVo));
	}

	@Test
	public void pageList() {
		for (int i = 0; i < 10; i++) {
			TUser user = new TUser();
			user.setUsername("lind" + i);
			userService.insert(user);
		}
		QueryWrapper<TUser> wrapper = new QueryWrapper<>();
		wrapper.lambda().isNotNull(TUser::getUsername);
		for (int curr = 1; curr <= 4; curr++) {
			IPage<TUser> page = userService.findAll(curr, 3, wrapper);
			for (TUser item : page.getRecords()) {
				log.info("user={}", item.getUsername());
			}
			log.info("---------------------page------------------");
		}
	}

	@Test
	public void getLogByPage() {
		int[] numbers = IntStream.rangeClosed(1, 10).toArray();
		for (int number : numbers) {
			TUser user = new TUser();
			user.setUsername("lind" + number);
			userService.insert(user);
		}
		Page<TUser> userPages = userService.getByPage(new Page(2, 2));
		userPages.getRecords().forEach(o -> log.info("{}", o.getUsername()));
	}

	private void print(String name) {
		QueryWrapper<TUser> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(TUser::getUsername, name);
		for (TUser item : userService.findAll(wrapper)) {
			log.info("user={}", item.toString());
		}
	}

	@Test
	public void pageData() {
		for (int i = 0; i < 10; i++) {
			TUser user = new TUser();
			user.setUsername("lind" + i);
			userService.insert(user);
		}
		Map<String, Object> params = new HashMap<>();
		params.put(Constant.PAGE, "1");
		params.put(Constant.LIMIT, "3");
		params.put(Constant.ORDER_FIELD, "username");
		params.put(Constant.ORDER, "desc");
		params.put("username", "lind");
		userService.page(params).getList().forEach(o -> log.info("{}", o.getUsername()));
	}

	@Test()
	public void insertLog() {
		TLog log = new TLog();
		log.setMessage("测试");
		logDao.insert(log);
	}

}
