package com.lind.common.thread.nio;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class TestNio {

	public static final String TEST_FILE = "D:\\个人技术资料\\爬虫补470万数据到雷达表source_filter_data\\test.txt";

	/**
	 * 又称信号量，用来控制同时访问访问某个特定资源的操作数量，可以理解为是用于控制线程访问数量的工具类
	 */
	private Semaphore consumerThreads = new Semaphore(1);

	@Test
	public void main() {
		DemoFileReader fileReader = new DemoFileReader(TEST_FILE, 100, 3);
		fileReader.registerHanlder(new FileLineDataHandler());
		fileReader.startRead();
	}

	@Test
	public void nioReadFile() throws IOException {
		Path path = Paths.get(TEST_FILE);
		Files.lines(path).forEach((i) -> {
			System.out.println("thread:" + Thread.currentThread().getId() + "index:" + i);
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		});// print each line

	}

	@SneakyThrows
	@Test
	public void bufferReader() {
		File file = new File(TEST_FILE);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		while ((st = br.readLine()) != null) {
			System.out.println(st);
		}
	}

	@Test
	public void scanner() throws FileNotFoundException {
		File file = new File(TEST_FILE);
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			System.out.println(sc.nextLine());
		}
	}

	@Test
	public void nioFilesParse() throws IOException, ParseException {
		Path ph = Paths.get(TEST_FILE);
		Consumer<String> action = new LineConsumer();
		Stream<String> lines = Files.lines(ph);
		lines.forEach(action);
		lines.close();
	}

	@SneakyThrows
	@Test
	public void nioAsyncParseTest() {
		nioAsyncParse(1, 5, 1024);
	}

	public void nioAsyncParse(final int numberOfFiles, final int numberOfThreads, final int bufferSize)
			throws IOException, ParseException, InterruptedException {
		ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(numberOfThreads);
		ConcurrentLinkedQueue<ByteBuffer> byteBuffers = new ConcurrentLinkedQueue<ByteBuffer>();

		for (int b = 0; b < numberOfThreads; b++) {
			byteBuffers.add(ByteBuffer.allocate(bufferSize));
		}

		for (int f = 0; f < numberOfFiles; f++) {
			consumerThreads.acquire();
			AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get(TEST_FILE),
					EnumSet.of(StandardOpenOption.READ), pool);
			BufferConsumer consumer = new BufferConsumer(byteBuffers, TEST_FILE, bufferSize);
			channel.read(consumer.buffer(), 0l, channel, consumer);
		}
		consumerThreads.acquire(numberOfThreads);
	}

	class LineConsumer implements Consumer<String> {

		@Override
		public void accept(String line) {
			// What to do for each line
			System.out.println(line);
		}

	}

	class BufferConsumer implements CompletionHandler<Integer, AsynchronousFileChannel> {

		private ConcurrentLinkedQueue<ByteBuffer> buffers;

		private ByteBuffer bytes;

		private String file;

		private StringBuffer chars;

		private int limit;

		private long position;

		private DateFormat frmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public BufferConsumer(ConcurrentLinkedQueue<ByteBuffer> byteBuffers, String fileName, int bufferSize) {
			buffers = byteBuffers;
			bytes = buffers.poll();
			if (bytes == null)
				bytes = ByteBuffer.allocate(bufferSize);

			file = fileName;
			chars = new StringBuffer(bufferSize);
			frmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			limit = bufferSize;
			position = 0l;
		}

		public ByteBuffer buffer() {
			return bytes;
		}

		@Override
		public synchronized void completed(Integer result, AsynchronousFileChannel channel) {

			if (result != -1) {
				bytes.flip();
				final int len = bytes.limit();
				int i = 0;
				try {
					for (i = 0; i < len; i++) {
						byte by = bytes.get();
						if (by == '\n') {
							// ***
							// The code used to process the line goes here
							chars.setLength(0);
						}
						else {
							chars.append((char) by);
						}
					}
				}
				catch (Exception x) {
					System.out.println("Caught exception " + x.getClass().getName() + " " + x.getMessage() + " i="
							+ String.valueOf(i) + ", limit=" + String.valueOf(len) + ", position="
							+ String.valueOf(position));
				}

				if (len == limit) {
					bytes.clear();
					position += len;
					channel.read(bytes, position, channel, this);
				}
				else {
					try {
						channel.close();
					}
					catch (IOException e) {
					}
					consumerThreads.release();
					bytes.clear();
					buffers.add(bytes);
				}
			}
			else {
				try {
					channel.close();
				}
				catch (IOException e) {
				}
				consumerThreads.release();
				bytes.clear();
				buffers.add(bytes);
			}
		}

		@Override
		public void failed(Throwable e, AsynchronousFileChannel channel) {
		}

	}

	;

}
