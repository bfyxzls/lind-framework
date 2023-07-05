package com.lind.mybatis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lind.common.dto.PageData;
import com.lind.mybatis.entity.TUser;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * 个性化业务.
 */
public interface UserService extends BaseService<TUser> {

	IPage<TUser> findByCondition(Pageable pageable);

	PageData<TUser> page(Map<String, Object> params);

	Page getByPage(Page page);

}
