package com.lind.common.othertest;

import com.lind.common.disruptor.LongEvent;
import com.lind.common.disruptor.LongEventFactory;
import com.lind.common.disruptor.LongEventHandler;
import com.lind.common.disruptor.LongEventProducer;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DisruptorTest {

	@Test
	public void test() throws Exception {
		// The factory for the com.lind.common.event
		LongEventFactory factory = new LongEventFactory();

		// Specify the size of the ring buffer, must be power of 2.
		int bufferSize = 4;

		// Construct the Disruptor
		Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, DaemonThreadFactory.INSTANCE);

		// Connect the handler
		disruptor.handleEventsWith(new LongEventHandler());

		// Start the Disruptor, starts all threads running
		disruptor.start();

		// Get the ring buffer from the Disruptor to be used for publishing.
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

		LongEventProducer producer = new LongEventProducer(ringBuffer);

		ByteBuffer bb = ByteBuffer.allocate(8);
		for (long l = 0; l < 10; l++) {
			bb.putLong(0, l);
			producer.onData(bb);
		}
		Thread.sleep(10000);
	}

	@Test
	public void test2() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		// 参数准备工作
		OrderEventFactory orderEventFactory = new OrderEventFactory();
		// int ringBufferSize = 4;// 1146
		int ringBufferSize = 4;// 100064
		int core = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(ringBufferSize);

		/**
		 * 1 eventFactory: 消息(com.lind.common.event)工厂对象 2 ringBufferSize: 容器的长度 3
		 * executor: 线程池(建议使用自定义线程池) RejectedExecutionHandler 4 ProducerType: 单生产者 还是 多生产者
		 * 5 waitStrategy: 等待策略,BlockingWaitStrategy是最低效的策略；YieldingWaitStrategy性能是最好的
		 */
		// 1. 实例化disruptor对象
		Disruptor<OrderEvent> disruptor = new Disruptor<>(orderEventFactory, ringBufferSize, executor,
				ProducerType.SINGLE, new YieldingWaitStrategy());

		// 2. 添加消费者的监听 (构建disruptor 与 消费者的一个关联关系)
		disruptor.handleEventsWith(new OrderEventHandler());

		// 3. 启动disruptor
		disruptor.start();

		// 4. 获取实际存储数据的容器: RingBuffer
		RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();

		OrderEventProducer producer = new OrderEventProducer(ringBuffer);

		ByteBuffer bb = ByteBuffer.allocate(8);

		for (long i = 0; i < 100; i++) {
			bb.putLong(0, i);
			// 数据生产速度快，消费慢
			producer.sendData(bb);
		}

		disruptor.shutdown();
		executor.shutdown();
		stopWatch.stop();
		System.out.println(stopWatch.getTotalTimeMillis());
	}

	/**
	 * 每10ms向disruptor中插入一个元素，消费者读取数据，并打印到终端。详细逻辑请细读代码.
	 * @throws Exception
	 */
	@Test
	public void disruptorEnter() throws Exception {
		// 队列中的元素
		class Element {

			private int value;

			public int get() {
				return value;
			}

			public void set(int value) {
				this.value = value;
			}

		}

		// 生产者的线程工厂
		ThreadFactory threadFactory = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "simpleThread");
			}
		};

		// RingBuffer生产工厂,初始化RingBuffer的时候使用
		EventFactory<Element> factory = new EventFactory<Element>() {
			@Override
			public Element newInstance() {
				return new Element();
			}
		};

		// 处理Event的handler
		EventHandler<Element> handler = new EventHandler<Element>() {
			@SneakyThrows
			@Override
			public void onEvent(Element element, long sequence, boolean endOfBatch) {
				System.out.println("Element: " + element.get());
				Thread.sleep(10);
			}
		};

		// 阻塞策略
		BlockingWaitStrategy strategy = new BlockingWaitStrategy();

		// 指定RingBuffer的大小
		int bufferSize = 4;

		// 创建disruptor，采用单生产者模式
		Disruptor<Element> disruptor = new Disruptor(factory, bufferSize, threadFactory, ProducerType.SINGLE, strategy);

		// 设置EventHandler
		disruptor.handleEventsWith(handler);

		// 启动disruptor的线程
		disruptor.start();

		RingBuffer<Element> ringBuffer = disruptor.getRingBuffer();

		for (int l = 0; true; l++) {
			// 获取下一个可用位置的下标
			long sequence = ringBuffer.next();
			try {
				// 返回可用位置的元素
				Element event = ringBuffer.get(sequence);
				// 设置该位置元素的值
				event.set(l);
			}
			finally {
				ringBuffer.publish(sequence);
			}
		}

	}

	public class OrderEvent {

		private long value; // 订单的价格

		public long getValue() {
			return value;
		}

		public void setValue(long value) {
			this.value = value;
		}

	}

	public class OrderEventFactory implements EventFactory<OrderEvent> {

		public OrderEvent newInstance() {
			return new OrderEvent(); // 这个方法就是为了返回空的数据对象（Event）
		}

	}

	public class OrderEventHandler implements EventHandler<OrderEvent> {

		public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
			System.err.println("消费者: " + event.getValue());
			Thread.sleep(10000);
		}

	}

	public class OrderEventProducer {

		private final RingBuffer<OrderEvent> ringBuffer;

		public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
			this.ringBuffer = ringBuffer;
		}

		public void sendData(ByteBuffer data) {
			// 1 在生产者发送消息的时候, 首先 需要从我们的ringBuffer里面 获取一个可用的序号
			long sequence = ringBuffer.next(); // 0
			try {
				// 2 根据这个序号, 找到具体的 "OrderEvent" 元素 注意:此时获取的OrderEvent对象是一个没有被赋值的"空对象"
				OrderEvent event = ringBuffer.get(sequence);
				// 3 进行实际的赋值处理
				event.setValue(data.getLong(0));
			}
			finally {
				// 4 提交发布操作
				ringBuffer.publish(sequence);
			}
		}

	}

}
