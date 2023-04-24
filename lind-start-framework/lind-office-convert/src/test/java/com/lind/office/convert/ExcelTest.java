package com.lind.office.convert;

import com.lind.office.convert.excel.ExcelBean;
import com.lind.office.convert.excel.ExcelToListMap;
import com.lind.office.convert.excel.ExcelUtils;
import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelTest {

	public static void main(String args[]) throws IOException {
		ExcelToListMap.read("D:\\个人技术资料\\绩效总结\\db2.xlsx");
	}

	@SneakyThrows
	@Test
	public void listExportExcel() {
		List<People> mapList = new ArrayList<>();

		mapList.add(new People("zhangsan", 100));
		mapList.add(new People("lisi", 200));
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		List<ExcelBean> excelBeans = new ArrayList<>();
		excelBeans.add(new ExcelBean("名称", "name"));
		excelBeans.add(new ExcelBean("数量", "totalData"));
		ExcelUtils.export(request, response, "test.xls", excelBeans, People.class, mapList);
	}

	@Test
	public void poiji() throws Exception {
		File file = new File("D:\\个人技术资料\\绩效总结\\db2.xlsx");
		InputStream stream = new FileInputStream(file);
		Workbook result = ExcelUtils.getWorkbook(stream, "db2.xlsx");
		PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings().addListDelimiter(",").build();

		List<InvoiceExcel> list = Poiji.fromExcel(file, InvoiceExcel.class, options);
		// System.out.println("Printing List Data: " +invoices);

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	public class People {

		private String name;

		private Integer totalData;

	}

}
