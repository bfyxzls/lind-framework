package com.lind.common.core.util;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.notNull;

/**
 * 文件读取工具.
 */
public class FileUtils {

	public static final String OBJ_NO_NULL = "对象不能为空";

	private static final int DEFAULT_BUF_SIZE = 1024;
	static Function<String, String> resourceFun;

	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 禁止实例化.
	 */
	private FileUtils() {
	}

	/**
	 * 设置文件获取的路径为资源resource路径.
	 */
	public static void setResourcePath() {
		resourceFun = (path) -> {
			try {
				return ResourceUtils.getFile("classpath:" + path).getPath();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		};
	}

	/**
	 * IO方式读取文件到对象.
	 * @param name .
	 * @return .
	 */
	public static byte[] readResourceByteArray(String name) throws IOException {
		if (resourceFun != null) {
			name = resourceFun.apply(name);
		}
		File f = new File(name);
		notNull(f, OBJ_NO_NULL);

		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int bufSize = 1024;
			byte[] buffer = new byte[bufSize];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, bufSize))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		}
		catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			try {
				if (in != null) {
					in.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}

	/**
	 * NIO方式读取. Channel(通道)，Buffer(缓冲区),
	 * Selector。传统IO基于字节流和字符流进行操作，而NIO基于Channel和Buffer(缓冲区)进行操作，
	 * 数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。Selector(选择区)用于监听多个通道的事件（比如：连接打开，数据到达）。
	 * 因此，Selector单个线程可以监听多个数据通道
	 * @param name .
	 * @return .
	 * @throws IOException .
	 */
	public static byte[] readResourceByteArrayNIO(String name) throws IOException {
		if (resourceFun != null) {
			name = resourceFun.apply(name);
		}
		File f = new File(name);
		notNull(f, OBJ_NO_NULL);
		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			notNull(fs, OBJ_NO_NULL);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
				System.out.println("reading");
			}
			return byteBuffer.array();
		}
		catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			try {
				if (channel != null) {
					channel.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fs != null) {
					fs.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 大文件读取.
	 * @param name .
	 * @return .
	 * @throws IOException .
	 */
	public static byte[] readResourceByteArrayBigFileNIO(String name) throws IOException {
		FileChannel fc = null;
		try {
			if (resourceFun != null) {
				name = resourceFun.apply(name);
			}
			File f = new File(name);
			notNull(f, OBJ_NO_NULL);

			fc = new RandomAccessFile(f.getAbsoluteFile(), "r").getChannel();
			MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
			System.out.println(byteBuffer.isLoaded());
			byte[] result = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0) {
				byteBuffer.get(result, 0, byteBuffer.remaining());
			}
			return result;
		}
		catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			try {
				if (fc != null) {
					fc.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 将对象写到资源文件.
	 */
	public static void writeResourceFromByteArrayNIO(String path, byte[] obj) {
		FileOutputStream fos = null;
		try {
			File file = new File(path);
			notNull(file, OBJ_NO_NULL);
			if (!file.exists()) {
				if (!file.createNewFile()) {
					throw new IllegalArgumentException("文件建立不成功");
				}
			}
			fos = new FileOutputStream(file.getAbsoluteFile());
			notNull(fos, OBJ_NO_NULL);
			FileChannel channel = fos.getChannel();
			ByteBuffer src = ByteBuffer.wrap(obj);
			// 字节缓冲的容量和limit会随着数据长度变化，不是固定不变的
			System.out.println("初始化容量和limit：" + src.capacity() + "," + src.limit());
			int length = 0;
			while ((length = channel.write(src)) != 0) {
				/*
				 * 注意，这里不需要clear，将缓冲中的数据写入到通道中后 第二次接着上一次的顺序往下读
				 */
				System.out.println("写入长度:" + length);
			}

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (fos != null) {
				try {
					fos.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 读取文本文件.
	 * @param filePath
	 */
	public static String readTxtFile(String filePath) {
		try {
			String encoding = "utf-8";
			File file = new File(filePath);
			StringBuffer buffer = new StringBuffer();
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					buffer.append(lineTxt);
				}
				read.close();
				return buffer.toString();
			}
			else {
				throw new IllegalArgumentException("找不到指定的文件");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过流的方式读取，注意jar包中的文件只能使用这种方式.
	 * @param inputStream
	 * @return
	 */
	public static String readTxtFile(InputStream inputStream) {
		try {
			String encoding = "utf-8";
			StringBuffer buffer = new StringBuffer();
			if (inputStream != null) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(inputStream, encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					buffer.append(lineTxt);
				}
				read.close();
				inputStream.close();
				return buffer.toString();
			}
			else {
				throw new IllegalArgumentException("找不到指定的文件");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 写入文件
	 * @param output
	 * @param list
	 * @param count
	 * @throws IOException
	 */
	private static void extracted(String output, List<String> list, long count) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(output + count + ".txt", true));
		for (String s : list) {
			bw.append(s);
			if (!s.endsWith("\r")) {
				bw.newLine();
			}
			bw.flush();
		}
		bw.close();
	}

	/**
	 * 拆分大文件.
	 * @param input 从哪个文件读取
	 * @param output 指定分拆后文件所在位置
	 * @param batch 拆分批次
	 */
	@SneakyThrows
	public static void splitBigFile(String input, String output, Integer batch) {
		File file = new File(input);
		FileReader fileReader = new FileReader(input);
		LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
		lineNumberReader.skip(Long.MAX_VALUE);
		long lines = lineNumberReader.getLineNumber() + 1;
		fileReader.close();
		lineNumberReader.close();

		FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(10);
		// 使用temp字节数组用于存储不完整的行的内容
		byte[] temp = new byte[0];
		List<String> list = new ArrayList<>();
		long count = 0;
		while (fileChannel.read(byteBuffer) != -1) {
			byte[] bs = new byte[byteBuffer.position()];
			byteBuffer.flip();
			byteBuffer.get(bs);
			byteBuffer.clear();
			int startNum = 0;
			// 判断是否出现了换行符，注意这要区分LF-\n,CR-\r,CRLF-\r\n,这里判断\n
			boolean isNewLine = false;
			for (int i = 0; i < bs.length; i++) {
				if (bs[i] == 10) {
					isNewLine = true;
					startNum = i;
				}
			}

			if (isNewLine) {
				// 如果出现了换行符，将temp中的内容与换行符之前的内容拼接
				byte[] toTemp = new byte[temp.length + startNum];
				System.arraycopy(temp, 0, toTemp, 0, temp.length);
				System.arraycopy(bs, 0, toTemp, temp.length, startNum);
				System.out.println(new String(toTemp));
				list.add(new String(toTemp));
				// 将换行符之后的内容(去除换行符)存到temp中
				temp = new byte[bs.length - startNum - 1];
				System.arraycopy(bs, startNum + 1, temp, 0, bs.length - startNum - 1);
				count++;
				// 尾页
				if (count == lines - 1) {
					extracted(output, list, count);
				}
			}
			else {
				// 如果没出现换行符，则将内容保存到temp中
				byte[] toTemp = new byte[temp.length + bs.length];
				System.arraycopy(temp, 0, toTemp, 0, temp.length);
				System.arraycopy(bs, 0, toTemp, temp.length, bs.length);
				temp = toTemp;
				if (list.size() > 0 && count > 0 && count % batch == 0) {
					System.out.println("split lines：" + count);
					extracted(output, list, count);
					list.clear();

				}

			}
		}
	}

	/**
	 * 大文件拆分
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		RandomAccessFile raf = new RandomAccessFile("d:\\webshu_0_to_20221226.csv.4", "r");
		long numSplits = 1801; // from user input, extract it from args
		// 文件大小
		long sourceSize = raf.length();
		// 文件分块大小
		long bytesPerSplit = sourceSize / numSplits;
		// 分块后剩余
		long remainingBytes = sourceSize % numSplits;

		// 缓冲区
		int maxReadBufferSize = 8 * 1024; // 8KB
		for (int destIx = 1; destIx <= numSplits; destIx++) {
			BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("d:\\split\\" + destIx));
			if (bytesPerSplit > maxReadBufferSize) {
				// 一共要读的次数
				long numReads = bytesPerSplit / maxReadBufferSize;
				// 剩余未读的大小
				long numRemainingRead = bytesPerSplit % maxReadBufferSize;
				for (int i = 0; i < numReads; i++) {
					readWrite(raf, bw, maxReadBufferSize);
				}
				if (numRemainingRead > 0) {
					readWrite(raf, bw, numRemainingRead);
				}
			}
			else {
				readWrite(raf, bw, bytesPerSplit);
			}
			bw.close();
		}
		if (remainingBytes > 0) {
			BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("d:\\split\\" + (numSplits + 1)));
			readWrite(raf, bw, remainingBytes);
			bw.close();
		}
		raf.close();
	}

	static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
		byte[] buf = new byte[(int) numBytes];
		int val = raf.read(buf);
		if (val != -1) {
			bw.write(buf);
		}
	}

	/**
	 * 递归删除目录
	 * @param root
	 * @return
	 */
	public static boolean deleteRecursively(File root) {
		if (root != null && root.exists()) {
			if (root.isDirectory()) {
				File[] children = root.listFiles();
				if (children != null) {
					for (File child : children) {
						deleteRecursively(child);
					}
				}
			}
			return root.delete();
		}
		return false;
	}

	/**
	 * 覆盖文件内容.
	 * @param file
	 * @param data
	 */
	public static void writeFileContent(File file, byte[] data) {

		// file
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}

		// append file content
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		finally {
			if (fos != null) {
				try {
					fos.close();
				}
				catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

	}

	/**
	 * 读文件内容.
	 * @param file
	 * @return
	 */
	public static byte[] readFileContent(File file) {
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];

		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			in.read(filecontent);
			in.close();

			return filecontent;
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		finally {
			if (in != null) {
				try {
					in.close();
				}
				catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 读取流中的所有内容.
	 * @param stream
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String readToEnd(InputStream stream, Charset charset) throws IOException {
		requireNonNull(stream);
		requireNonNull(charset);

		final StringBuilder sb = new StringBuilder();
		final char[] buffer = new char[DEFAULT_BUF_SIZE];

		try (Reader in = new InputStreamReader(stream, charset)) {
			int charsRead = 0;
			while ((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
				sb.append(buffer, 0, charsRead);
			}
		}

		return sb.toString();
	}

}
