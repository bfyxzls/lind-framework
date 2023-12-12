package com.lind.mybatis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lind.mybatis.entity.LawyerInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * lawyer_info表的仓储.
 */
@Mapper
public interface LawyerInfoDao extends BaseMapper<LawyerInfo> {

}
