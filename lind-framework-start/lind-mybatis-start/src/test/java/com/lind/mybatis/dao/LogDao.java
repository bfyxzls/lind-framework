package com.lind.mybatis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lind.mybatis.entity.TLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * t_user表的仓储.
 */
@Mapper
public interface LogDao extends BaseMapper<TLog> {

}
