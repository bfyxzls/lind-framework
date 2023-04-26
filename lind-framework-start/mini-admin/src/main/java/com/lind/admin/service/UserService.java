package com.lind.admin.service;

import com.lind.admin.entity.User;

import java.util.List;

/**
 * @author lind
 * @date 2022/11/15 8:54
 * @since 1.0.0
 */
public interface UserService {

	List<User> pageList(int offset, int pagesize, String username, int role);

	long pageListCount(int offset, int pagesize, String username, int role);

	User loadByUserName(String username);

	int save(User user);

	int update(User user);

	int delete(int id);

}
