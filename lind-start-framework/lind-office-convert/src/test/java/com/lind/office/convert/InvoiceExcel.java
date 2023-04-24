package com.lind.office.convert;

/**
 * @author lind
 * @date 2022/10/26 9:34
 * @since 1.0.0
 */

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

import java.util.List;

public class InvoiceExcel {

	@ExcelRow
	private int rowIndex;

	@ExcelCellName("name")
	private String name;

	@ExcelCellName("CaseGrade")
	private List<String> CaseGrade;

}
