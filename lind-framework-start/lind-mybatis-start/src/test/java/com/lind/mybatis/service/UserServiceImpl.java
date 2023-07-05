package com.lind.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lind.common.dto.PageData;
import com.lind.mybatis.dao.UserDao;
import com.lind.mybatis.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserServiceImpl extends BaseServiceImpl<UserDao, TUser> implements UserService {

	@Autowired
	UserDao userDao;

	@Override
	public BaseMapper<TUser> getRepository() {
		return userDao;
	}

	@Override
	public IPage<TUser> findByCondition(Pageable pageable) {
		QueryWrapper<TUser> userQueryWrapper = new QueryWrapper<>();
		return userDao.selectPage(new Page<>(pageable.getPageNumber(), pageable.getPageSize()), userQueryWrapper);
	}

	@Override
	public PageData<TUser> page(Map<String, Object> params) {

		IPage<TUser> page = getRepository().selectPage(getPage(params, "sort", true), getWrapper(params));
		return getPageData(page, TUser.class);
	}

	@Override
	public Page getByPage(Page page) {
		return getRepository().selectPage(page, null);
	}

	private QueryWrapper<TUser> getWrapper(Map<String, Object> params) {
		String dictTypeId = (String) params.get("username");
		QueryWrapper<TUser> wrapper = new QueryWrapper<>();
		wrapper.like("username", dictTypeId);

		return wrapper;
	}

}
