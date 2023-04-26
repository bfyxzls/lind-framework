package com.lind.office.convert.excel;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

	private static final String excel2003L = ".xls"; // 2003- 版本的excel

	private static final String excel2007U = ".xlsx"; // 2007+ 版本的excel

	private static XSSFCellStyle fontStyle;

	private static XSSFCellStyle fontStyle2;

	/**
	 * getBankListByExcel.
	 * @param in .
	 * @param fileName .
	 * @return
	 */
	static List<List<Object>> getBankListByExcel(InputStream in, String fileName) throws Exception {
		List<List<Object>> list = null;

		// 创建Excel工作薄
		Workbook work = getWorkbook(in, fileName);
		if (null == work) {
			throw new Exception("创建Excel工作薄为空！");
		}
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;

		list = new ArrayList<List<Object>>();
		// 遍历Excel中所有的sheet
		for (int i = 0; i < work.getNumberOfSheets(); i++) {
			sheet = work.getSheetAt(i);
			if (sheet == null) {
				continue;
			}

			// 遍历当前sheet中的所有行
			for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum(); j++) {
				row = sheet.getRow(j);
				if (row == null || row.getFirstCellNum() == j) {
					continue;
				}

				// 遍历所有的列
				List<Object> li = new ArrayList<Object>();
				for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
					cell = row.getCell(y);
					li.add(getCellValue(cell));
				}
				list.add(li);
			}
		}
		return list;
	}

	/**
	 * 描述：根据文件后缀，自适应上传文件的版本.
	 * @param inStr fileName
	 * @return
	 */
	public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
		Workbook wb = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (excel2003L.equals(fileType)) {
			wb = new HSSFWorkbook(inStr); // 2003-
		}
		else if (excel2007U.equals(fileType)) {
			wb = new XSSFWorkbook(inStr); // 2007+
		}
		else {
			throw new Exception("解析的文件格式有误！");
		}
		return wb;
	}

	/**
	 * 描述：对表格中数值进行格式化.
	 * @param cell .
	 * @return
	 */
	static Object getCellValue(Cell cell) {
		Object value = null;
		DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd"); // 日期格式化
		DecimalFormat df2 = new DecimalFormat("0.00"); // 格式化数字

		switch (cell.getCellType()) {
		case STRING:
			JSONObject jsonObject2 = JSONUtil.parseObj(cell.getRichStringCellValue().getString());
			value = jsonObject2;
			break;
		case NUMERIC:
			if ("General".equals(cell.getCellStyle().getDataFormatString())) {
				value = df.format(cell.getNumericCellValue());
			}
			else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
				value = sdf.format(cell.getDateCellValue());
			}
			else {
				value = df2.format(cell.getNumericCellValue());
			}
			break;
		case BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case BLANK:
			value = "";
			break;
		default:
			break;
		}
		return value;
	}

	/**
	 * 多列头创建EXCEL.
	 */
	static XSSFWorkbook createExcelFile(Class clazz, List objs, Map<Integer, List<ExcelBean>> map, String sheetName)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException,
			IntrospectionException, ParseException {
		// 创建新的Excel 工作簿
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称
		XSSFSheet sheet = workbook.createSheet(sheetName);
		// 以下为excel的字体样式以及excel的标题与内容的创建，下面会具体分析;
		createFont(workbook);// 字体样式
		createTableHeader(sheet, map);// 创建标题（头）
		createTableRows(sheet, map, objs, clazz);// 创建内容
		return workbook;
	}

	/**
	 * createFont.
	 * @param workbook .
	 */
	static void createFont(XSSFWorkbook workbook) {
		// 表头
		fontStyle = workbook.createCellStyle();
		XSSFFont font1 = workbook.createFont();
		font1.setBold(true);
		font1.setFontName("黑体");
		font1.setFontHeightInPoints((short) 14);// 设置字体大小
		fontStyle.setFont(font1);
		fontStyle.setBorderBottom(BorderStyle.THIN); // 下边框
		fontStyle.setBorderLeft(BorderStyle.THIN);// 左边框
		fontStyle.setBorderTop(BorderStyle.THIN);// 上边框
		fontStyle.setBorderRight(BorderStyle.THIN);// 右边框
		fontStyle.setAlignment(HorizontalAlignment.CENTER); // 居中

		// 内容
		fontStyle2 = workbook.createCellStyle();
		XSSFFont font2 = workbook.createFont();
		font2.setFontName("宋体");
		font2.setFontHeightInPoints((short) 10);// 设置字体大小
		fontStyle2.setFont(font2);
		fontStyle2.setBorderBottom(BorderStyle.THIN); // 下边框
		fontStyle2.setBorderLeft(BorderStyle.THIN);// 左边框
		fontStyle2.setBorderTop(BorderStyle.THIN);// 上边框
		fontStyle2.setBorderRight(BorderStyle.THIN);// 右边框
		fontStyle2.setAlignment(HorizontalAlignment.CENTER); // 居中
	}

	/**
	 * 根据ExcelMapping 生成列头（多行列头）.
	 * @param sheet 工作簿
	 * @param map 每行每个单元格对应的列头信息
	 */
	static final void createTableHeader(XSSFSheet sheet, Map<Integer, List<ExcelBean>> map) {
		int startIndex = 0;// cell起始位置
		int endIndex = 0;// cell终止位置

		for (Map.Entry<Integer, List<ExcelBean>> entry : map.entrySet()) {
			XSSFRow row = sheet.createRow(entry.getKey());
			List<ExcelBean> excels = entry.getValue();
			for (int x = 0; x < excels.size(); x++) {
				// 合并单元格
				if (excels.get(x).getCols() > 1) {
					if (x == 0) {
						endIndex += excels.get(x).getCols() - 1;
						CellRangeAddress range = new CellRangeAddress(0, 0, startIndex, endIndex);
						sheet.addMergedRegion(range);
						startIndex += excels.get(x).getCols();
					}
					else {
						endIndex += excels.get(x).getCols();
						CellRangeAddress range = new CellRangeAddress(0, 0, startIndex, endIndex);
						sheet.addMergedRegion(range);
						startIndex += excels.get(x).getCols();
					}
					XSSFCell cell = row.createCell(startIndex - excels.get(x).getCols());
					cell.setCellValue(excels.get(x).getHeadTextName());// 设置内容
					if (excels.get(x).getCellStyle() != null) {
						cell.setCellStyle(excels.get(x).getCellStyle());// 设置格式
					}
					cell.setCellStyle(fontStyle);
				}
				else {

					XSSFCell cell = row.createCell(x);
					cell.setCellValue(excels.get(x).getHeadTextName());// 设置内容
					if (excels.get(x).getCellStyle() != null) {
						cell.setCellStyle(excels.get(x).getCellStyle());// 设置格式
					}
					cell.setCellStyle(fontStyle);
				}
			}
			setSizeColumn(sheet, excels.size()); // 中文列宽
		}
	}

	/**
	 * 自适应宽度(中文支持).
	 * @param sheet .
	 * @param size .
	 */
	static void setSizeColumn(XSSFSheet sheet, int size) {
		for (int columnNum = 0; columnNum < size; columnNum++) {
			int columnWidth = sheet.getColumnWidth(columnNum) / 256;
			for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
				XSSFRow currentRow;
				// 当前行未被使用过
				if (sheet.getRow(rowNum) == null) {
					currentRow = sheet.createRow(rowNum);
				}
				else {
					currentRow = sheet.getRow(rowNum);
				}

				if (currentRow.getCell(columnNum) != null) {
					XSSFCell currentCell = currentRow.getCell(columnNum);
					if (currentCell.getCellType() == CellType.STRING) {
						int length = currentCell.getStringCellValue().getBytes(Charset.defaultCharset()).length;
						if (columnWidth < length) {
							columnWidth = length;
						}
					}
				}
			}
			sheet.setColumnWidth(columnNum, columnWidth * 256);
		}

	}

	/**
	 * createTableRows.
	 */
	@SuppressWarnings("rawtypes")
	static void createTableRows(XSSFSheet sheet, Map<Integer, List<ExcelBean>> map, List objs, Class clazz)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IntrospectionException,
			ClassNotFoundException, ParseException {

		int rowindex = map.size();
		int maxKey = 0;
		for (Map.Entry<Integer, List<ExcelBean>> entry : map.entrySet()) {
			if (entry.getKey() > maxKey) {
				maxKey = entry.getKey();
			}
		}
		List<ExcelBean> ems = map.get(maxKey);

		List<Integer> widths = new ArrayList<Integer>(ems.size());
		for (Object obj : objs) {
			XSSFRow row = sheet.createRow(rowindex);
			for (int i = 0; i < ems.size(); i++) {
				ExcelBean em = (ExcelBean) ems.get(i);
				// 获得get方法
				PropertyDescriptor pd = new PropertyDescriptor(em.getPropertyName(), clazz);
				Method getMethod = pd.getReadMethod();
				Object rtn = getMethod.invoke(obj);
				XSSFCell cell = row.createCell(i);
				cell.setCellType(CellType.STRING);
				// 如果是日期类型 进行 转换
				String value = "";
				// 如果是日期类型 进行 转换
				if (rtn != null) {
					value = rtn.toString();
					if (rtn instanceof Date) {
						value = rtn.toString();
						cell.setCellValue(value);
					}
					else if (rtn instanceof Integer) {
						cell.setCellValue((Integer) rtn);
						cell.setCellType(CellType.NUMERIC);
					}
					else if (rtn instanceof Double || rtn instanceof BigDecimal) {
						cell.setCellValue((Double) rtn);
						cell.setCellType(CellType.NUMERIC);
					}
					else {
						cell.setCellValue(value);
					}
				}
				else {
					cell.setCellValue(value);
				}
				cell.setCellStyle(fontStyle2);
				// 获得最大列宽
				int width = value.getBytes(Charset.defaultCharset()).length * 300;
				// 还未设置，设置当前
				if (widths.size() <= i) {
					widths.add(width);
					continue;
				}
				// 比原来大，更新数据
				if (width > widths.get(i)) {
					widths.set(i, width);
				}
			}
			rowindex++;
		}
		// 设置列宽
		for (int index = 0; index < widths.size(); index++) {
			Integer width = widths.get(index);
			width = width < 2500 ? 2500 : width + 300;
			width = width > 10000 ? 10000 + 300 : width + 300;
			sheet.setColumnWidth(index, width);
		}
	}

	/**
	 * 导出excel.
	 */
	public static void export(HttpServletRequest request, HttpServletResponse response, String fileName,
			List<ExcelBean> ems, Class clazz, List objs) throws Exception {
		fileName = getUtf8FileName(fileName);
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		// 导出Excel对象
		Map<Integer, List<ExcelBean>> map = new LinkedHashMap<>();
		map.put(0, ems);
		XSSFWorkbook workbook = ExcelUtils.createExcelFile(clazz, objs, map, "sheet1");
		OutputStream output = response.getOutputStream();
		BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
		bufferedOutPut.flush();
		workbook.write(bufferedOutPut);
		bufferedOutPut.close();

	}

	/**
	 * 中文名格式化.
	 * @param fileName .
	 * @return
	 */
	static String getUtf8FileName(String fileName) {
		try {
			return java.net.URLEncoder.encode(fileName, "UTF-8");

		}
		catch (Exception e) {
			return fileName;
		}
	}

}
