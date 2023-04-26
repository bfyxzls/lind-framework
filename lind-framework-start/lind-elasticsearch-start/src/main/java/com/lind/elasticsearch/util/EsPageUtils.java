package com.lind.elasticsearch.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 分页处理.
 */
public class EsPageUtils {

	private static final int DEFAULT_PAGE_SIZE = 10;

	public static Pageable getPageable(int pageNumber, int pageSize) {
		if (pageSize == 0) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		return PageRequest.of(pageNumber, pageSize);
	}

	public static Pageable getPageable(int pageNumber, int pageSize, List<Sort.Order> orders) {
		if (pageSize == 0) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		return PageRequest.of(pageNumber, pageSize, Sort.by(orders));
	}

}
