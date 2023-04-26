package com.lind.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lind.common.pattern.factorymethod.Userinfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import java.lang.reflect.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2022/8/9 17:06
 * @since 1.0.0
 */
public class ReflectTest {

	public void print(JSONObject a) {
		System.out.println(a);
	}

	public void go() {
		System.out.println("go");
	}

	/**
	 * 反射调用方法.
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@Test
	public void invoke()
			throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class clazz = Class.forName("com.lind.common.ReflectTest");
		Method method = clazz.getDeclaredMethod("print", JSONObject.class);
		String param = "{hello:\"ok\",sex:\"1\"}";
		JSONObject anchorParam = JSON.parseObject(param);
		method.invoke(new ReflectTest(), anchorParam);
	}

	@Test
	public void instance() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
			InstantiationException, IllegalAccessException {
		Class<?> clazz = Class.forName("com.lind.common.ReflectTest");
		Constructor<?> customStrategy = clazz.getConstructor();
		ReflectTest reflectTest = (ReflectTest) customStrategy.newInstance();
		reflectTest.go();

	}

	private String name;

	@Test
	public void doWith() {
		Userinfo userinfo = new Userinfo();
		userinfo.setName("lind");
		userinfo.setAge(1);
		ReflectionUtils.doWithFields(userinfo.getClass(), field -> {
			if (field.isAnnotationPresent(ApiModelProperty.class)) {

				field.setAccessible(true);// 访问修饰符权限
				System.out.println("field:" + field.getName() + ":" + field.get(userinfo));
			}
		});
	}

	public interface IVehical {

		void run();

	}

	/**
	 * 要扩展的类
	 */
	public class Car implements IVehical {

		public void run() {
			System.out.println("Car会跑");
		}

	}

	/**
	 * 调用处理类invocationhandler
	 */
	public class VehicalInvacationHandler implements InvocationHandler {

		private final IVehical vehical;

		public VehicalInvacationHandler(IVehical vehical) {
			this.vehical = vehical;
		}

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			System.out.println("---------before-------");
			Object invoke = method.invoke(vehical, args);
			System.out.println("---------after-------");

			return invoke;
		}

	}

	/**
	 * 利用Java的反射技术(Java Reflection)，在运行时创建一个实现某些给定接口的新类（也称“动态代理类”）及其实例（对象）,
	 * 代理的是接口(Interfaces)，不是类(Class)，也不是抽象类。在运行时才知道具体的实现，spring aop就是此原理。
	 */
	@Test
	public void newIns() {
		IVehical car = new Car();
		// IVehical vehical = (IVehical)
		// Proxy.newProxyInstance(car.getClass().getClassLoader(),
		// Car.class.getInterfaces(), new VehicalInvacationHandler(car));
		// vehical.run();

		IVehical vehical = (IVehical) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				Car.class.getInterfaces(), new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						System.out.println("---------before-------");
						Object invoke = method.invoke(car, args);
						System.out.println("---------after-------");
						return invoke;
					}
				});
		vehical.run();

	}

	@Test
	public void netty() throws InterruptedException {
		NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline().addLast(new IdleStateHandler(0, 0, 1, TimeUnit.SECONDS)) // beat
																									// N,
																									// close
																									// if
																									// fail
						;
					}
				}).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
		var channel = bootstrap.connect("localhost", 70).sync().channel();
	}

}
