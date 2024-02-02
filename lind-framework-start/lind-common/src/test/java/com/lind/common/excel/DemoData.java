package com.lind.common.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 基础数据类.这里的排序和excel里面的排序一致
 *
 * @author Jiaju Zhuang
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemoData {

	private String string;

	private Date date;

	private Double doubleData;

}
