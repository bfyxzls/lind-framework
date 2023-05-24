package com.lind.mybatis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lind.mybatis.entity.TLog;
import com.lind.mybatis.entity.TUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * t_user表的仓储.
 */
@Mapper
public interface LogDao extends BaseMapper<TLog> {

}
