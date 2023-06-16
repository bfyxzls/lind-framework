package com.lind.common.util;

import cn.hutool.core.io.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件夹读文件测试.
 *
 * @author lind
 * @date 2023/6/2 17:25
 * @since 1.0.0
 */
public class FileTreeTest {

	public static void main(String[] args) {
		String path = "d:\\test";
		String passedPath = "d:\\test\\finished";

		FileUtil.loopFiles(path).forEach(file -> {
			try {
				if (Files.notExists(Paths.get(passedPath))) {
					Files.createDirectory(Paths.get(passedPath));
				}
				FileUtil.readLines(file, "utf-8").forEach(line -> {
					// 文件内容处理
					System.out.println(line);
				});
				// 文件移动
				Files.move(file.getAbsoluteFile().toPath(), Paths.get(passedPath).resolve(file.getName()));
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

}
