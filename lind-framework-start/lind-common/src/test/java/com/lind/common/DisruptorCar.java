package com.lind.common;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 停车场问题. 1） 事件对象Event 2）三个消费者Handler 3）一个生产者Processer 4）执行Main方法
 */
public class DisruptorCar {

	private static final Integer NUM = 1; // 1,10,100,1000

	/**
	 * 消息生产.
	 */
	void disruptorPublish() {
		long beginTime = System.currentTimeMillis();
		int bufferSize = 2048; // 2的N次方
		try {
			// 创建线程池，负责处理Disruptor的四个消费者
			ExecutorService executor = Executors.newFixedThreadPool(4);

			// 初始化一个 Disruptor
			Disruptor<MyInParkingDataEvent> disruptor = new Disruptor<>(() -> {
				return new MyInParkingDataEvent(); // Event 初始化工厂
			}, bufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());

			// 使用disruptor创建消费者组 MyParkingDataInDbHandler 和 MyParkingDataToKafkaHandler
			EventHandlerGroup<MyInParkingDataEvent> handlerGroup = disruptor
					.handleEventsWith(new MyParkingDataInDbHandler(), new MyParkingDataToKafkaHandler());

			// 当上面两个消费者处理结束后在消耗 smsHandler
			MyParkingDataSmsHandler myParkingDataSmsHandler = new MyParkingDataSmsHandler();
			handlerGroup.then(myParkingDataSmsHandler);

			// 启动Disruptor
			disruptor.start();

			CountDownLatch countDownLatch = new CountDownLatch(1); // 一个生产者线程准备好了就可以通知主线程继续工作了
			// 生产者生成数据
			executor.submit(new MyInParkingDataEventPublisher(countDownLatch, disruptor));
			countDownLatch.await(); // 等待生产者结束

			disruptor.shutdown();
			executor.shutdown();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 测试入口 ， 一个生产者（汽车进入停车场）； 三个消费者（一个记录汽车信息，一个发送消息给系统，一个发送消息告知司机）
	 * 前两个消费者同步执行，都有结果了再执行第三个消费者
	 */
	@Test
	public void main() throws InterruptedException {

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		for (int i = 0; i < 10; i++) {
			disruptorPublish();
		}
		stopWatch.stop();
		System.out.println("总耗时:" + stopWatch.getTotalTimeMillis());
	}

	/**
	 * 事件源对象.
	 */
	public class MyInParkingDataEvent {

		private String carLicense; // 车牌号

		public String getCarLicense() {
			return carLicense;
		}

		public void setCarLicense(String carLicense) {
			this.carLicense = carLicense;
		}

	}

	/**
	 * Handler 第一个消费者，负责保存进场汽车的信息
	 */
	public class MyParkingDataInDbHandler
			implements EventHandler<MyInParkingDataEvent>, WorkHandler<MyInParkingDataEvent> {

		@Override
		public void onEvent(MyInParkingDataEvent myInParkingDataEvent) throws Exception {
			long threadId = Thread.currentThread().getId(); // 获取当前线程id
			String carLicense = myInParkingDataEvent.getCarLicense(); // 获取车牌号
			System.out.println(String.format("Thread Id %s 保存 %s 到数据库中 ....", threadId, carLicense));
		}

		@Override
		public void onEvent(MyInParkingDataEvent myInParkingDataEvent, long sequence, boolean endOfBatch)
				throws Exception {
			this.onEvent(myInParkingDataEvent);
		}

	}

	/**
	 * 第二个消费者，负责发送通知告知工作人员(Kafka是一种高吞吐量的分布式发布订阅消息系统)
	 */
	public class MyParkingDataToKafkaHandler implements EventHandler<MyInParkingDataEvent> {

		@Override
		public void onEvent(MyInParkingDataEvent myInParkingDataEvent, long sequence, boolean endOfBatch)
				throws Exception {
			long threadId = Thread.currentThread().getId(); // 获取当前线程id
			String carLicense = myInParkingDataEvent.getCarLicense(); // 获取车牌号
			System.out.println(String.format("Thread Id %s 发送 %s 进入停车场信息给 kafka系统...", threadId, carLicense));
		}

	}

	/**
	 * 第三个消费者，sms短信服务，告知司机你已经进入停车场，计费开始。
	 */
	public class MyParkingDataSmsHandler implements EventHandler<MyInParkingDataEvent> {

		@Override
		public void onEvent(MyInParkingDataEvent myInParkingDataEvent, long sequence, boolean endOfBatch)
				throws Exception {
			long threadId = Thread.currentThread().getId(); // 获取当前线程id
			String carLicense = myInParkingDataEvent.getCarLicense(); // 获取车牌号
			System.out.println(String.format("Thread Id %s 给  %s 的车主发送一条短信，并告知他计费开始了 ....", threadId, carLicense));
		}

	}

	/**
	 * 生产者，进入停车场的车辆
	 */
	public class MyInParkingDataEventPublisher implements Runnable {

		private CountDownLatch countDownLatch; // 用于监听初始化操作，等初始化执行完毕后，通知主线程继续工作

		private Disruptor<MyInParkingDataEvent> disruptor;

		public MyInParkingDataEventPublisher(CountDownLatch countDownLatch, Disruptor<MyInParkingDataEvent> disruptor) {
			this.countDownLatch = countDownLatch;
			this.disruptor = disruptor;
		}

		@Override
		public void run() {
			MyInParkingDataEventTranslator eventTranslator = new MyInParkingDataEventTranslator();
			try {
				for (int i = 0; i < NUM; i++) {
					disruptor.publishEvent(eventTranslator);
					Thread.sleep(1000); // 假设一秒钟进一辆车
				}
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				countDownLatch.countDown(); // 执行完毕后通知 await()方法
				System.out.println(NUM + "辆车已经全部进入进入停车场！");
			}
		}

	}

	class MyInParkingDataEventTranslator implements EventTranslator<MyInParkingDataEvent> {

		@Override
		public void translateTo(MyInParkingDataEvent myInParkingDataEvent, long sequence) {
			this.generateData(myInParkingDataEvent);
		}

		private MyInParkingDataEvent generateData(MyInParkingDataEvent myInParkingDataEvent) {
			myInParkingDataEvent.setCarLicense("车牌号： 鄂A-" + (int) (Math.random() * 100000)); // 随机生成一个车牌号
			System.out.println("Thread Id " + Thread.currentThread().getId() + " 写完一个event");
			return myInParkingDataEvent;
		}

	}

}
