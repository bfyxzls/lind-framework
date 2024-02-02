package com.lind.common.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author lind
 * @date 2024/1/25 17:02
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest
@RestController
@AutoConfigureMockMvc
public class ReadAndExportTest {

	private MockMvc mockMvc;

	public static String getPath() {
		return ReadAndExportTest.class.getResource("/").getPath();
	}

	public static byte[] convertFileToByteArray(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = fis.read(buffer)) != -1) {
			bos.write(buffer, 0, length);
		}
		fis.close();
		return bos.toByteArray();
	}

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(new ReadAndExportTest()).build();
	}

	@GetMapping("/export-excel")
	public void exportExcel(HttpServletResponse response) throws IOException {
		List<DemoData> data = new ArrayList<>();
		data.add(new DemoData("张三", new Date(), 140d));
		data.add(new DemoData("李四", new Date(), 150d));
		data.add(new DemoData("王五", new Date(), 180d));

		// 设置响应头
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=demo.xlsx");

		// 写出数据到响应流
		EasyExcel.write(response.getOutputStream(), DemoData.class).sheet("Sheet1").doWrite(data);
	}

	/**
	 * 最简单的读
	 * <p>
	 * 1. 创建excel对应的实体对象 参照{@link DemoData}
	 * <p>
	 * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器
	 * <p>
	 * 3. 直接读即可
	 */
	@Test
	public void simpleRead() {
		// 写法1：JDK8+ ,不用额外写一个DemoDataListener
		// since: 3.0.0-beta1
		String fileName = getPath() + "demo" + File.separator + "demo.xlsx";
		// 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
		// 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
		// 具体需要返回多少行可以在`PageReadListener`的构造函数设置
		EasyExcel.read(fileName, DemoData.class, new PageReadListener<DemoData>(dataList -> {
			for (DemoData demoData : dataList) {
				log.info("读取到一条数据{}", JSON.toJSONString(demoData));
			}
		})).sheet().doRead();
	}

	@Test
	public void simpleExport() {
		// 准备数据
		List<DemoData> data = new ArrayList<>();
		data.add(new DemoData("张三", new Date(), 140d));
		data.add(new DemoData("李四", new Date(), 150d));
		data.add(new DemoData("王五", new Date(), 180d));

		// 指定输出的 Excel 文件路径
		String fileName = "d:\\your_file_path.xlsx";

		// 写出数据到 Excel
		EasyExcel.write(fileName, DemoData.class).sheet("人员表").doWrite(data);
	}

	@Test
	public void webExcelExport() throws Exception {
		ResultActions resultActions = mockMvc.perform(get("/export-excel")).andExpect(status().isOk());
		// 从 ResultActions 获取响应
		MockHttpServletResponse response = resultActions.andReturn().getResponse();
		// 将响应内容写入临时文件
		File tempFile = File.createTempFile("temp", ".xlsx");
		try (FileOutputStream fos = new FileOutputStream(tempFile)) {
			fos.write(response.getContentAsByteArray());
			fos.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		// 使用 EasyExcel 读取临时文件中的数据
		List<DemoData> data = EasyExcel.read(tempFile).sheet().head(DemoData.class).doReadSync();

		// 打印数据内容
		for (DemoData item : data) {
			System.out.println(item);
		}

		// 进行断言验证
		// ...

		// 删除临时文件
		tempFile.delete();
	}

}
