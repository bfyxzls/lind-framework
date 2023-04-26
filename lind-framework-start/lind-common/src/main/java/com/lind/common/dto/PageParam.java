package com.lind.common.dto;

public interface PageParam {

	int getPageNumber();

	int getPageSize();

	String getSort();

	String getOrder();

	void setPageNumber(int pageNumber);

	void setPageSize(int pageSize);

	void setSort(String sort);

	void setOrder(String order);

}
