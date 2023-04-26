package com.lind.office.convert.word;

import com.lind.office.convert.utils.CustomXWPFDocument;
import com.lind.office.convert.utils.OfficeUtil;
import lombok.SneakyThrows;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.ResourceUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HtmlToWord {

	// 获取html中的图片元素信息
	static List<HashMap<String, String>> getImgStr(String htmlStr) {
		List<HashMap<String, String>> pics = new ArrayList<HashMap<String, String>>();

		Document doc = Jsoup.parse(htmlStr);
		Elements imgs = doc.select("img");
		for (Element img : imgs) {
			HashMap<String, String> map = new HashMap<String, String>();
			if (!"".equals(img.attr("width"))) {
				map.put("width", img.attr("width").substring(0, img.attr("width").length() - 2));
			}
			if (!"".equals(img.attr("height"))) {
				map.put("height", img.attr("height").substring(0, img.attr("height").length() - 2));
			}
			map.put("img", img.toString().substring(0, img.toString().length() - 1) + "/>");
			map.put("img1", img.toString());
			map.put("src", img.attr("src"));
			pics.add(map);
		}
		return pics;
	}

	/**
	 * 读文件内容
	 * @param file
	 * @return
	 */
	static String readfile(File file) {
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuffer buffer = new StringBuffer();
		byte[] bytes = new byte[1024];
		try {
			for (int n; (n = input.read(bytes)) != -1;) {
				buffer.append(new String(bytes, 0, n, "utf8"));
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * 写成word文件.
	 */
	@SneakyThrows
	public static String writeWordFile(String content, String outDir) {
		String path = outDir;
		Map<String, Object> param = new HashMap<String, Object>();

		if (!"".equals(path)) {
			File fileDir = new File(path);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			content = HtmlUtils.htmlUnescape(content);
			List<HashMap<String, String>> imgs = getImgStr(content);
			int count = 0;
			for (HashMap<String, String> img : imgs) {
				count++;
				// 处理替换以“/>”结尾的img标签
				content = content.replace(img.get("img"), "${imgReplace" + count + "}");
				// 处理替换以“>”结尾的img标签
				content = content.replace(img.get("img1"), "${imgReplace" + count + "}");
				Map<String, Object> header = new HashMap<String, Object>();

				try {
					File filePath = new File(ResourceUtils.getURL("classpath:").getPath());
					String imagePath = filePath.getAbsolutePath() + "\\static\\";
					imagePath += img.get("src").replaceAll("/", "\\\\");
					// 如果没有宽高属性，默认设置为400*300
					if (img.get("width") == null || img.get("height") == null) {
						header.put("width", 400);
						header.put("height", 300);
					}
					else {
						header.put("width", (int) (Double.parseDouble(img.get("width"))));
						header.put("height", (int) (Double.parseDouble(img.get("height"))));
					}
					header.put("type", "jpg");
					header.put("content", OfficeUtil.inputStream2ByteArray(new FileInputStream(imagePath), true));
				}
				catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				param.put("${imgReplace" + count + "}", header);
			}
			try {
				// 生成doc格式的word文档，需要手动改为docx
				String fileName = path.concat("\\").concat(UUID.randomUUID().toString() + ".doc");
				byte by[] = content.getBytes("UTF-8");
				ByteArrayInputStream bais = new ByteArrayInputStream(by);

				POIFSFileSystem poifs = new POIFSFileSystem();
				DirectoryEntry directory = poifs.getRoot();
				DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
				OutputStream ostream = new FileOutputStream(fileName);
				poifs.writeFilesystem(ostream);
				bais.close();
				ostream.close();

				if (imgs.size() > 0) {
					// 临时文件（手动改好的docx文件）,需要使用office另存为office2007格式的文件
					CustomXWPFDocument doc = OfficeUtil.generateWord(param, "D:\\wordFile\\temp.docx");
					// 最终生成的带图片的word文件
					FileOutputStream fopts = new FileOutputStream("D:\\wordFile\\final.docx");
					doc.write(fopts);

					fopts.close();
				}
				return fileName;
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	/**
	 * 从html文件写到word文件.
	 * @param file
	 * @param outDir
	 * @return
	 */
	public static String writeWordFile(File file, String outDir) {
		return writeWordFile(readfile(file), outDir);
	}

}
