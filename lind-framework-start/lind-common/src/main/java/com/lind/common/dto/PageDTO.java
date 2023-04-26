package com.lind.common.dto;

import lombok.Data;

/**
 * 分页参数.
 */
@Data
public class PageDTO implements PageParam {

	/**
	 * 页号.
	 */
	private int pageNumber = 0;

	/**
	 * 页面记录数.
	 */
	private int pageSize = 10;

	/**
	 * 排序字段.
	 */
	private String sort;

	/**
	 * 排序方式 asc/desc.
	 */
	private String order;

	@Override
	public int getPageNumber() {
		return pageNumber < 1 ? 1 : pageNumber;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

}
