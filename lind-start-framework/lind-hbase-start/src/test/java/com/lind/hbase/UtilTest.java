package com.lind.hbase;

import cn.hutool.core.lang.Assert;
import com.lind.hbase.config.HBaseAutoConfiguration;
import com.lind.hbase.entity.DataRecord;
import com.lind.hbase.service.HBaseService;
import com.lind.hbase.util.HbaseConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author lind
 * @date 2022/8/10 9:18
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@Slf4j
@PropertySource(value = "classpath:application.yml")
@SpringBootTest(classes = { HBaseAutoConfiguration.class })

public class UtilTest {

	@Autowired
	HBaseService hBaseService;

	@Test
	public void getTable() throws IOException {
		Table table = HbaseConnectionUtil.getConnection373839().getTable(TableName.valueOf("nezha:nvwa_pfnl_dev"));
		String rowKey = "123";
		Get get = new Get(Bytes.toBytes(rowKey));
		Assert.isFalse(table.exists(get));
	}

	@Test
	public void createTable() throws IOException {
		hBaseService.createTable("test");
	}

	@Test
	public void saveData() {
		hBaseService.save("test", new DataRecord("1").append("author", "zhangzhanling"));
	}

	@Test
	public void readData() {
		log.info("hbase:{}", hBaseService.getByKey("test", "1"));
	}

}
