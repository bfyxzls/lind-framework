package com.lind.common.othertest;

import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.io.*;

/**
 * 字节流和字符流测试. 字节输入流：（InputStream） 从文件读取内容到某个变量 字符输入流：（Reader） 从文件读取内容到某个变量
 * 字节输出流：（OutputStream） 将字符输出到文件 字符输出流：（Writer） 将字符输出到文件 字符输入：字节（磁盘）–> 自动转换为 –>字符（内存）；
 * 字符输出：字符（内存）–> 自动转换为–>字节（磁盘）；
 */
public class StreamAndReaderWriteTest {

	static final String FILE_PATH = "d:\\miniBase\\hello.txt";
	static final String MSG = "富则达济天下，穷则独善其身";

	@Test
	public void rename() {
		// rename相当于一个移动操作
		Assert.assertTrue(new File(FILE_PATH).renameTo(new File(FILE_PATH + ".tmp")));
	}

	@Test
	public void Reader() throws IOException {
		// 第一步：定义要输出的文件的File类对象
		File file = new File(FILE_PATH);// 你的路径
		if (file.exists()) {
			Reader in = new FileReader(file);
			char[] data = new char[1024];
			int len = in.read(data);// 向字符数组保存数据，返回长度。
			System.out.println(new String(data, 0, len));
			in.close();
		}
	}

	@Test
	public void outputStream() throws IOException {
		// 第一步：定义要输出的文件的File类对象
		File file = new File(FILE_PATH);
		// 输出信息的时候文件可以不存在，但是目录必须存在
		if (!file.getParentFile().exists()) {// 父路径不存在
			file.getParentFile().mkdirs();// 创建父路径
		}
		// 第二步：利用OutputStream的子类为父类进行实例化
		OutputStream output = new FileOutputStream(file);
		// 第三步：输出文字信息
		String msg = MSG;// 字符串
		// 为了方便输出需要将字符串变为字节数组
		byte[] data = msg.getBytes();// 变为字节数组
		output.write(data);// 输出数据
		output.close();// 关闭流
	}

	@Test
	public void inputStream() throws IOException {
		// 第一步：定义要输出的文件的File类对象
		File file = new File(FILE_PATH);
		// 第二步：实例化InputStream
		InputStream input = new FileInputStream(file);
		// 实现数据的读取操作
		byte[] data = new byte[1024];
		int len = input.read(data);// 将数据读取到数组之中
		System.out.println("读取的内容\n" + new String(data, 0, len));
		assert new String(data, 0, len).equals(MSG);
		// 第四步关闭输入流
		input.close();
	}

	@Test
	public void write() throws IOException {
		// 第一步：定义要输出的文件的File类对象
		File file = new File(FILE_PATH);// 你的路径
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		Writer out = new FileWriter(file);
		String str = MSG;
		out.write(str);
		out.close();
	}

	@Test
	public void lindFileWriteTest() throws IOException {
		LindFileWrite lindFileWrite = new LindFileWrite("d:\\miniBase\\zzl.txt");
		lindFileWrite.append("hello\n");
		lindFileWrite.append("good\n");
		lindFileWrite.append("you are good!");
	}

	public static class LindFileWrite implements Closeable {

		private final FileOutputStream fileOutputStreaml;

		public LindFileWrite(String name) throws IOException {
			File f = new File(name);
			f.createNewFile();
			this.fileOutputStreaml = new FileOutputStream(f, true);
		}

		public void append(String input) throws IOException {
			if (input == null)
				return;

			fileOutputStreaml.write(input.getBytes());
		}

		@Override
		public void close() throws IOException {
			if (fileOutputStreaml != null) {
				try {
					fileOutputStreaml.flush();
					FileDescriptor fd = fileOutputStreaml.getFD();
					fd.sync();
				}
				finally {
					fileOutputStreaml.close();
				}
			}
		}

	}

}
