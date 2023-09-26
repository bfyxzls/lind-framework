package com.lind.mybatis.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lind.common.core.util.CopyUtils;
import com.lind.common.dto.PageData;
import com.lind.mybatis.base.BaseEntity;
import com.lind.mybatis.config.Constant;

import java.util.List;
import java.util.Map;

/**
 * @author lind
 * @date 2022/7/1 17:41
 * @description
 */
public abstract class BaseService<T extends BaseEntity> {

	/**
	 * 获取分页对象
	 * @param params 分页查询参数
	 * @param defaultOrderField 默认排序字段
	 * @param isAsc 排序方式
	 */
	protected IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
		// 分页参数
		long curPage = 1;
		long limit = 10;

		if (params.get(Constant.PAGE) != null) {
			curPage = Long.parseLong((String) params.get(Constant.PAGE));
		}
		if (params.get(Constant.LIMIT) != null) {
			limit = Long.parseLong((String) params.get(Constant.LIMIT));
		}

		// 分页对象
		Page<T> page = new Page<>(curPage, limit);

		// 分页参数
		params.put(Constant.PAGE, page);

		// 排序字段
		String orderField = (String) params.get(Constant.ORDER_FIELD);
		String order = (String) params.get(Constant.ORDER);

		// 前端字段排序
		if (StringUtils.isNotBlank(orderField) && StringUtils.isNotBlank(order)) {
			if (Constant.ASC.equalsIgnoreCase(order)) {
				return page.addOrder(OrderItem.asc(orderField));
			}
			else {
				return page.addOrder(OrderItem.desc(orderField));
			}
		}

		// 没有排序字段，则不排序
		if (StringUtils.isBlank(defaultOrderField)) {
			return page;
		}

		// 默认排序
		if (isAsc) {
			page.addOrder(OrderItem.asc(defaultOrderField));
		}
		else {
			page.addOrder(OrderItem.desc(defaultOrderField));
		}

		return page;
	}

	protected <T> PageData<T> getPageData(List<?> list, long total, Class<T> target) {
		List<T> targetList = CopyUtils.copyListProperties(list, target);

		return new PageData<>(targetList, total);
	}

	protected <T> PageData<T> getPageData(IPage page, Class<T> target) {
		return getPageData(page.getRecords(), page.getTotal(), target);
	}

}
