package com.lind.mybatis.util;

import com.lind.common.dto.PageParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页处理.
 */
public class PageUtil {

	/**
	 * 自动化分页
	 * @param page
	 * @return
	 */
	public static Pageable initPage(PageParam page) {

		Pageable pageable = null;
		int pageNumber = page.getPageNumber();
		int pageSize = page.getPageSize();
		String sort = page.getSort();
		String order = page.getOrder();

		if (pageNumber < 1) {
			pageNumber = 1;
		}
		if (pageSize < 1) {
			pageSize = 10;
		}
		if (pageSize > 100) {
			pageSize = 100;
		}
		if (StringUtils.isNotBlank(sort)) {
			Sort.Direction d;
			if (StringUtils.isBlank(order)) {
				d = Sort.Direction.DESC;
			}
			else {
				d = Sort.Direction.valueOf(order.toUpperCase());
			}
			Sort s = Sort.by(d, sort);
			pageable = PageRequest.of(pageNumber, pageSize, s);
		}
		else {
			pageable = PageRequest.of(pageNumber, pageSize);
		}
		return pageable;
	}

	/**
	 * List 手动分页
	 * @param page
	 * @param list
	 * @return
	 */
	public static List listToPage(PageParam page, List list) {

		int pageNumber = page.getPageNumber() - 1;
		int pageSize = page.getPageSize();

		if (pageNumber < 0) {
			pageNumber = 0;
		}
		if (pageSize < 1) {
			pageSize = 10;
		}
		if (pageSize > 100) {
			pageSize = 100;
		}

		int fromIndex = pageNumber * pageSize;
		int toIndex = pageNumber * pageSize + pageSize;

		if (fromIndex > list.size()) {
			return new ArrayList();
		}
		else if (toIndex >= list.size()) {
			return list.subList(fromIndex, list.size());
		}
		else {
			return list.subList(fromIndex, toIndex);
		}
	}

}
