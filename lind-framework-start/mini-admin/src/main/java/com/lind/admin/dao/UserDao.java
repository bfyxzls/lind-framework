package com.lind.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lind.admin.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xuxueli 2019-05-04 16:44:59
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

}
