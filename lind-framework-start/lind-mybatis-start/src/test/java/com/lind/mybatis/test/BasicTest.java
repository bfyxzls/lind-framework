package com.lind.mybatis.test;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.lind.common.dto.PageDTO;
import com.lind.common.dto.PageParam;
import com.lind.mybatis.dao.LawyerInfoDao;
import com.lind.mybatis.dao.UserDao;
import com.lind.mybatis.entity.LawyerInfo;
import com.lind.mybatis.entity.TUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicTest {

	static final String FILE_NAME = "d:\\bloomfilter.dat";

	@Autowired
	UserDao userDao;

	@Autowired
	UserService userService;

	@Autowired
	LawyerInfoDao lawyerInfoDao;

	@Test
	public void insert() {
		TUser user = new TUser();
		user.setUsername("lind");
		user.setExtensionInfo(new HashMap() {
			{
				put("city", "beijing");
				put("area", "shijingshan");
			}
		});
		user.setLikeList(java.util.Arrays.asList("basketball", "football"));
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

	@Test
	public void read() {
		QueryWrapper<TUser> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(TUser::getCreateBy, 1);
		log.info("user1:{}", userDao.selectList(wrapper));
	}

	@Test
	public void sort() {
		QueryWrapper<TUser> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(TUser::getCreateBy, 2).orderByDesc(TUser::getCreateBy);
		log.info("user1:{}", userDao.selectList(wrapper));
	}

	@Test
	public void serviceRead() {
		userService.readUser();
	}

	@Test
	public void servicePageList() {
		PageParam pageVo = new PageDTO();
		pageVo.setPageNumber(1);
		pageVo.setPageSize(5);
		Page page = new Page(1, 5);

		log.info("1 page:{}", userDao.selectPage(page, new QueryWrapper<>()).getRecords());

	}

	@Test
	public void pageList() {
		for (int i = 0; i < 10; i++) {
			TUser user = new TUser();
			user.setUsername("lind" + i);
			userDao.insert(user);
		}
		QueryWrapper<TUser> wrapper = new QueryWrapper<>();
		wrapper.lambda().isNotNull(TUser::getUsername);
		for (int curr = 1; curr <= 4; curr++) {
			IPage<TUser> page = userDao.selectPage(new Page<>(curr, 5), wrapper);
			for (TUser item : page.getRecords()) {
				log.info("user={}", item.getUsername());
			}
			log.info("---------------------page------------------");
		}
	}

	@Test
	public void readPage() {
		Map<String, Object> params = new HashMap<>();
		params.put("page", "1");// 来自前端URL地址上的字符串型当前页码
		params.put("limit", "10");
		userService.readPage(params).getRecords().forEach(o -> log.info("{}", o.getUsername()));
	}

	/**
	 * mybatis-plus分页查询
	 */
	@Test
	public void plusPage() {
		Page page = new Page();
		page.setCurrent(1);
		page.setSize(10);
		IPage<TUser> users = userDao.selectPage(page, Wrappers.emptyWrapper());
		log.info("{}", users);
	}

	/**
	 * 50万字符占用磁盘(1,198,606B)1.14M
	 */
	@Test
	public void guavaBloomFilter() {
		int expectedInsertions = 1000000;// 预期的插入元素数量
		double fpp = 0.01; // 期望的误判率
		BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()),
				expectedInsertions, fpp);
		QueryWrapper<LawyerInfo> wrapper = Wrappers.emptyWrapper();
		Page page = new Page();
		page.setSize(500);
		for (int i = 1; i <= 1000; i++) {
			page.setCurrent(i);
			lawyerInfoDao.selectPage(page, wrapper).getRecords().forEach(o -> {
				LawyerInfo lawyerInfo = (LawyerInfo) o;
				bloomFilter.put(lawyerInfo.getLawyerName());
			});
		}
		if (FileUtil.exist(FILE_NAME)) {
			FileUtil.del(FILE_NAME);
		}
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
			oos.writeObject(bloomFilter);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SneakyThrows
	@Test
	public void guavaContainBloomFilter() {
		BloomFilter<String> bloomFilter;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
			bloomFilter = (BloomFilter<String>) ois.readObject();
			if (bloomFilter.mightContain("张六岭")) {
				System.out.println("The element '张六岭' might exist in the BloomFilter.");
			}
			else {
				System.out.println("The element '张六岭' definitely does not exist in the BloomFilter.");
			}
		}
	}

}
