package com.lind.common.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DateRangeDTO {

	private LocalDate fromDate;// 如果没有指定Json格式，如果你转2022-01-01这种日期，那它会报错.

	private LocalDate toDate;

}
