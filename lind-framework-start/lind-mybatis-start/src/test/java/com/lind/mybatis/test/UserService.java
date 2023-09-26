package com.lind.mybatis.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lind.mybatis.dao.UserDao;
import com.lind.mybatis.datasource.DataSource;
import com.lind.mybatis.datasource.DataSourceType;
import com.lind.mybatis.entity.TUser;
import com.lind.mybatis.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lind
 * @date 2023/9/25 10:47
 * @since 1.0.0
 */
@Service
@Slf4j
public class UserService extends BaseService<TUser> {

	@Autowired
	UserDao userDao;

	@DataSource(DataSourceType.SLAVE)
	public void readUser() {
		QueryWrapper<TUser> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(TUser::getCreateBy, 2);
		log.info("user1:{}", userDao.selectList(wrapper));
	}

	public IPage<TUser> readPage(Map<String, Object> map) {
		QueryWrapper<TUser> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(TUser::getCreateBy, 1);
		return userDao.selectPage(super.getPage(map, "id", true), wrapper);
	}

}
