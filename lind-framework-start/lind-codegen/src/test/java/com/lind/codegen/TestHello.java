package com.lind.codegen;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.SneakyThrows;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.MathTool;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author lind
 * @date 2023/12/1 15:15
 * @since 1.0.0
 */
public class TestHello {

	@SneakyThrows
	@Test
	public void generatePage() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		Properties prop = new Properties();
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(prop);
		Map<String, Object> map = new HashMap<>();
		map.put("comments", "Velocity");
		map.put("author", "lind");
		map.put("datetime", "2023/12/1 15:15");
		VelocityContext context = new VelocityContext(map);
		// 函数库
		context.put("math", new MathTool());
		context.put("dateTool", new DateTool());
		StringWriter sw = new StringWriter();
		Template tpl = Velocity.getTemplate("hello.vm", CharsetUtil.UTF_8);
		tpl.merge(context, sw);

		String fileName = "hello.txt";
		// ZipEntry来表示压缩包中的每个文件或目录，并使用putNextEntry方法将其添加到压缩流中
		zip.putNextEntry(new ZipEntry(Objects.requireNonNull(fileName)));
		IoUtil.write(zip, StandardCharsets.UTF_8, false, sw.toString());
		IoUtil.close(sw);
		zip.closeEntry();

		IoUtil.close(zip);// 这个需要在IoUtil.write的上面，即先关闭zip流，再写文件，否则会报错

		IoUtil.write(FileUtil.getOutputStream("d:\\codegen\\hello.zip"), Boolean.TRUE, outputStream.toByteArray());
	}

	@Test
	public void zip() {
		String entryName = "d:\\codegen\\1";
		String zipFilePath = "d:\\codegen\\example.zip";

		// 将entryName这个文件或者目录，压缩成zipFilePath这个zip文件
		ZipUtil.zip(entryName, zipFilePath, true);
	}

	@Test
	public void zipStream() throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		zip.putNextEntry(new ZipEntry(Objects.requireNonNull("1.txt")));
		IoUtil.write(zip, StandardCharsets.UTF_8, false, "hello world.");
		zip.closeEntry();

		IoUtil.close(zip);
		IoUtil.write(FileUtil.getOutputStream("d:\\codegen\\hello.zip"), Boolean.TRUE, outputStream.toByteArray());
	}

}
