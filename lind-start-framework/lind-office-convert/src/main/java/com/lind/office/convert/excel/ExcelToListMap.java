package com.lind.office.convert.excel;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lind
 * @date 2022/10/28 11:10
 * @since 1.0.0
 */
public class ExcelToListMap {

	/**
	 * 递归解析单行对象.
	 * @param filed
	 * @param jsonNode
	 * @param map
	 */
	public static void join(String filed, JsonNode jsonNode, Map map) {
		if (jsonNode.isArray()) {
			Iterator<JsonNode> jsonNodeIterator = jsonNode.iterator();
			while (jsonNodeIterator.hasNext()) {
				JsonNode itemNode = jsonNodeIterator.next();
				join(filed, itemNode, map);
			}
		}
		else if (jsonNode.isObject()) {
			Iterator<String> iteratorNames = jsonNode.fieldNames();
			while (iteratorNames.hasNext()) {
				String name = iteratorNames.next();
				if (jsonNode.get(name).isArray() || jsonNode.get(name).isObject()) {
					join(name, jsonNode.get(name), map);
				}
				else {
					map.put(name, jsonNode.get(name));
				}

			}

		}
		else {
			map.put(filed, jsonNode.asText());
		}
	}

	/**
	 * 读excel返回Map
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, Object>> read(String filePath) throws IOException {
		File file = new File(filePath); // creating a new file instance
		FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
		Iterator<Row> itr = sheet.iterator(); // iterating over excel file
		Row head = sheet.getRow(0);
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> mapList = new ArrayList<>();
		itr.next();// 跳过第一行
		while (itr.hasNext()) {
			Row row = itr.next();

			Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each
																// column
			Map<String, Object> map = new HashMap<>();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				String columnName = head.getCell(cell.getColumnIndex()).toString();
				switch (cell.getCellType()) {
				case STRING: // field that represents string cell type

					try {
						JsonNode node = mapper.readTree(cell.getStringCellValue());
						if (node.isArray()) {
							ArrayNode arrayNode = (ArrayNode) node;
							try {
								List<Map> innerList = new ArrayList<>();
								for (JsonNode jsonNode : arrayNode) {
									innerList.add(mapper.convertValue(jsonNode, Map.class));
								}
								map.put(columnName, innerList);
							}
							catch (Exception ex) {
								// 简单类型的数组
								map.put(columnName,
										mapper.readValue(cell.getStringCellValue(), new TypeReference<List<String>>() {
										}));
							}

						}
						else {
							map.put(columnName, mapper.convertValue(node, Map.class));
						}
					}
					catch (JsonParseException ex) {
						map.put(columnName, cell.getStringCellValue());
					}
					break;
				case NUMERIC:
					map.put(columnName, cell.getStringCellValue());
					break;
				default:
				}
			}
			mapList.add(map);
		}

		return mapList;
	}

}
