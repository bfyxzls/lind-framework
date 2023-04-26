package com.lind.mybatis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lind.mybatis.entity.TUser;
import org.springframework.stereotype.Component;

/**
 * t_user表的仓储.
 */
@Component
public interface UserDao extends BaseMapper<TUser> {

}
