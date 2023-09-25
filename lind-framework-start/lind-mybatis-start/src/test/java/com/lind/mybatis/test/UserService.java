package com.lind.mybatis.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lind.mybatis.dao.UserDao;
import com.lind.mybatis.entity.TUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lind
 * @date 2023/9/25 10:47
 * @since 1.0.0
 */
@Service
@Slf4j
public class UserService {

	@Autowired
	UserDao userDao;

	public void readUser() {
		QueryWrapper<TUser> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(TUser::getCreateBy, 2);
		log.info("user1:{}", userDao.selectList(wrapper));
	}

}
