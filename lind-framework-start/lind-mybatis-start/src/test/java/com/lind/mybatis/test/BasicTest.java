package com.lind.mybatis.test;

import com.lind.mybatis.dao.UserDao;
import com.lind.mybatis.entity.TUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicTest {

	@Autowired
	UserDao userDao;

	@Test
	public void insert() {
		TUser user = new TUser();
		user.setUsername("lind");
		userDao.insert(user);
	}

	@Test
	public void update() throws InterruptedException {
		TUser user = new TUser();
		user.setUsername("lind");
		userDao.insert(user);
		Thread.sleep(1000);
		user.setUsername("lind修改");
		userDao.updateById(user);
	}

	// @Test
	// public void servicePageList() {
	// PageParam pageVo = new PageDTO();
	// pageVo.setPageNumber(1);
	// pageVo.setPageSize(2);
	// userService.findByCondition(PageUtil.initPage(pageVo));
	// }
	//
	// @Test
	// public void pageList() {
	// for (int i = 0; i < 10; i++) {
	// TUser user = new TUser();
	// user.setUsername("lind" + i);
	// userService.insert(user);
	// }
	// QueryWrapper<TUser> wrapper = new QueryWrapper<>();
	// wrapper.lambda().isNotNull(TUser::getUsername);
	// for (int curr = 1; curr <= 4; curr++) {
	// IPage<TUser> page = userService.findAll(curr, 3, wrapper);
	// for (TUser item : page.getRecords()) {
	// log.info("user={}", item.getUsername());
	// }
	// log.info("---------------------page------------------");
	// }
	// }
	//
	// @Test
	// public void getLogByPage() {
	// int[] numbers = IntStream.rangeClosed(1, 10).toArray();
	// for (int number : numbers) {
	// TUser user = new TUser();
	// user.setUsername("lind" + number);
	// userService.insert(user);
	// }
	// Page<TUser> userPages = userService.getByPage(new Page(2, 2));
	// userPages.getRecords().forEach(o -> log.info("{}", o.getUsername()));
	// }
	//
	// private void print(String name) {
	// QueryWrapper<TUser> wrapper = new QueryWrapper<>();
	// wrapper.lambda().eq(TUser::getUsername, name);
	// for (TUser item : userService.findAll(wrapper)) {
	// log.info("user={}", item.toString());
	// }
	// }
	//
	// @Test
	// public void pageData() {
	// for (int i = 0; i < 10; i++) {
	// TUser user = new TUser();
	// user.setUsername("lind" + i);
	// userService.insert(user);
	// }
	// Map<String, Object> params = new HashMap<>();
	// params.put(Constant.PAGE, "1");
	// params.put(Constant.LIMIT, "3");
	// params.put(Constant.ORDER_FIELD, "username");
	// params.put(Constant.ORDER, "desc");
	// params.put("username", "lind");
	// userService.page(params).getList().forEach(o -> log.info("{}", o.getUsername()));
	// }

}
