package com.lind.office.convert;

import com.lind.office.convert.utils.DownloadUtils;
import com.lind.office.convert.utils.FilesUtils;
import com.lind.office.convert.utils.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class UtilTest {

	@Test
	public void readFileText() {
		log.info(FilesUtils.readAll(PathUtils.getClassRootPath("/test.txt")));
	}

	@Test
	public void readFiles() {
		for (String fileName : FilesUtils.getAllFileNames("D://")) {
			log.info(fileName);
		}
	}

	@Test
	public void downUrl() {
		log.info(DownloadUtils.getContentFromUrl("https://home.firefoxchina.cn/"));
	}

}
