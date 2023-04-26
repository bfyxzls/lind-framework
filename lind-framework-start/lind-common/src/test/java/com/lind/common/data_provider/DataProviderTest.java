package com.lind.common.data_provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author lind
 * @date 2022/10/18 16:41
 * @since 1.0.0
 */
public class DataProviderTest {

	@Test(dataProvider = "wxAddConditionalMenu")
	public void testAddConditionalToJson(String json) throws JsonProcessingException {
		Menu menu = new Menu();
		menu.setLevel("click");
		menu.setTitle("今日歌曲");
		Assert.assertEquals(new ObjectMapper().writeValueAsString(menu), json);
	}

	@DataProvider(name = "wxAddConditionalMenu")
	public Object[][] addConditionalMenuJson() {
		String json = "{" + "\"title\":\"今日歌曲\"," + "\"level\":\"click\"" + "}";
		return new Object[][] { new Object[] { json } };
	}

}
