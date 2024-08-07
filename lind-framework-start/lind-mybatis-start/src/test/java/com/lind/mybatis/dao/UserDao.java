package com.lind.mybatis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lind.mybatis.entity.TUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * t_user表的仓储.
 */
@Mapper
public interface UserDao extends BaseMapper<TUser> {

	List<TUser> queryUsers();

}
