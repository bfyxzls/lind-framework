package com.lind.common.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;

/**
 * @author lind
 * @date 2024/1/25 17:02
 * @since 1.0.0
 */
@Slf4j
public class ReadTest {

	public static String getPath() {
		return ReadTest.class.getResource("/").getPath();
	}

	/**
	 * 最简单的读
	 * <p>
	 * 1. 创建excel对应的实体对象 参照{@link DemoData}
	 * <p>
	 * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
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

}
