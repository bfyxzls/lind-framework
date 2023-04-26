package com.lind.office.convert.excel;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.io.Serializable;

/**
 * 导入导出excel.
 */
public class ExcelBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String headTextName;

	private String propertyName;

	private Integer cols = 0;

	private transient XSSFCellStyle cellStyle;

	/**
	 * init.
	 * @param headTextName .
	 * @param propertyName .
	 */
	public ExcelBean(String headTextName, String propertyName) {
		this.headTextName = headTextName;
		this.propertyName = propertyName;
	}

	/**
	 * init.
	 * @param headTextName .
	 * @param propertyName .
	 * @param cols .
	 */
	public ExcelBean(String headTextName, String propertyName, Integer cols) {
		super();
		this.headTextName = headTextName;
		this.propertyName = propertyName;
		this.cols = cols;
	}

	public String getHeadTextName() {
		return headTextName;
	}

	public void setHeadTextName(String headTextName) {
		this.headTextName = headTextName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Integer getCols() {
		return cols;
	}

	public void setCols(Integer cols) {
		this.cols = cols;
	}

	public XSSFCellStyle getCellStyle() {
		return cellStyle;
	}

	public void setCellStyle(XSSFCellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}

}
