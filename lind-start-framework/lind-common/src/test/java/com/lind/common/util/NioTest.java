package com.lind.common.util;

import lombok.SneakyThrows;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NioTest {

	@SneakyThrows
	@Test
	public void readWrite() {
		StringBuilder text = new StringBuilder();
		// 文件通道通过 FileChannel 的静态方法 open() 来获取，获取时需要指定文件路径和文件打开方式。
		// 获取文件通道
		FileChannel channel = FileChannel.open(Paths.get("c:\\windows\\system32\\drivers\\etc\\hosts"),
				StandardOpenOption.READ);
		// 文件相关的字节缓冲区有两种，一种是基于堆的 HeapByteBuffer，另一种是基于文件映射，放在堆外内存中的 MappedByteBuffer。
		// 分配字节缓存
		ByteBuffer buf = ByteBuffer.allocate(10);
		// 读取数据 一般需要一个循环结构来读取数据，读取数据时需要注意切换 ByteBuffer 的读写模式。
		while (channel.read(buf) != -1) { // 读取通道中的数据，并写入到 buf 中
			buf.flip(); // 缓存区切换到读模式
			while (buf.position() < buf.limit()) { // 读取 buf 中的数据
				text.append((char) buf.get());
			}
			buf.clear(); // 清空 buffer，缓存区切换到写模式
		}
		System.out.println(text.toString());

		// 与入数据
		for (int i = 0; i < text.length(); i++) {
			buf.put((byte) text.charAt(i)); // 填充缓冲区，需要将 2 字节的 char 强转为 1 自己的 byte
			if (buf.position() == buf.limit() || i == text.length() - 1) { // 缓存区已满或者已经遍历到最后一个字符
				buf.flip(); // 将缓冲区由写模式置为读模式
				channel.write(buf); // 将缓冲区的数据写到通道
				buf.clear(); // 清空缓存区，将缓冲区置为写模式，下次才能使用
			}
		}
	}

}
