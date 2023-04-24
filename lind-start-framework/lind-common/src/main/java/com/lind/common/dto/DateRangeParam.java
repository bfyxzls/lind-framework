package com.lind.common.dto;

public interface DateRangeParam {

	java.time.LocalDate getFromDate();

	java.time.LocalDate getToDate();

	void setFromDate(java.time.LocalDate fromDate);

	void setToDate(java.time.LocalDate toDate);

}
