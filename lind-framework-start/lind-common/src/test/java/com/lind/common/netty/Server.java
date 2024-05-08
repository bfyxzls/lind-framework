package com.lind.common.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lind
 * @date 2022/11/15 17:19
 * @since 1.0.0
 */

public class Server {

	static Logger logger = LoggerFactory.getLogger(Server.class);

	CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
		@Override
		public void run() {

			cn.hutool.http.HttpUtil.post("http://localhost:7070/kill", "name=lind&sex=1");

		}
	});

	/**
	 * 公开一个web服务器，只支持post方法，公开端口kill
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// start server
		int port = 7070;
		ServerBootstrap bootstrap = new ServerBootstrap();
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ThreadPoolExecutor bizThreadPool = new ThreadPoolExecutor(10, 20, 60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(2000), new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						// 为线程池里的线程初始化,第一次构建的数量为corePoolSize，之后最大为maximumPooSize,再大走rejectedExecution
						String name = "xxl-job, EmbedServer bizThreadPool-" + r.hashCode();
						System.err.println(name);
						return new Thread(r, name);
					}
				}, new RejectedExecutionHandler() {
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						throw new RuntimeException("xxl-job, EmbedServer bizThreadPool is EXHAUSTED!");
					}
				});

		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline().addLast(new IdleStateHandler(0, 0, 30 * 3, TimeUnit.SECONDS)) // beat
								// 3N,
								// close
								// if
								// idle
								.addLast(new HttpServerCodec()).addLast(new HttpObjectAggregator(5 * 1024 * 1024)) // merge
								// request
								// &
								// reponse
								// to
								// FULL
								.addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
									@Override
									protected void channelRead0(ChannelHandlerContext channelHandlerContext,
											FullHttpRequest fullHttpRequest) throws Exception {
										String requestData = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
										String uri = fullHttpRequest.uri();
										HttpMethod httpMethod = fullHttpRequest.method();
										boolean keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);
										logger.info("httpMethod:{},uri:{},requestData:{}", httpMethod, uri,
												requestData);
										bizThreadPool.execute(() -> {
											// do invoke
											Object responseObj = process(httpMethod, uri, requestData);

											// to json
											String responseJson = null;
											try {
												responseJson = new ObjectMapper().writeValueAsString(responseObj);
											}
											catch (JsonProcessingException e) {
												throw new RuntimeException(e);
											}

											// write response
											writeResponse(channelHandlerContext, keepAlive, responseJson);
										});
									}
								});
					}
				}).childOption(ChannelOption.SO_KEEPALIVE, true);

		// bind
		ChannelFuture future = bootstrap.bind(port).sync();

		logger.info(">>>>>>>>>>> xxl-job remoting server start success, nettype = {}, port = {}", Server.class, port);

		// wait util stop
		future.channel().closeFuture().sync();
	}

	@SneakyThrows
	private static Object process(HttpMethod httpMethod, String uri, String requestData) {
		// valid
		if (HttpMethod.POST != httpMethod) {
			return "invalid request, HttpMethod not support.";
		}
		// services mapping
		try {
			switch (uri) {
				case "/beat":
					return "beat:" + requestData;

				case "/kill":
					return "kill:" + requestData;

				default:
					return "default";
			}
		}
		catch (Exception e) {
			throw e;
		}
	}

	/**
	 * write response
	 */
	private static void writeResponse(ChannelHandlerContext ctx, boolean keepAlive, String responseJson) {
		// write response
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer(responseJson, CharsetUtil.UTF_8)); // Unpooled.wrappedBuffer(responseJson)
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8"); // HttpHeaderValues.TEXT_PLAIN.toString()
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
		if (keepAlive) {
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}
		ctx.writeAndFlush(response);
	}

	@Test
	public void for100() throws ExecutionException, InterruptedException {
		List<Integer> urls = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			urls.add(i);
		}
		urls.parallelStream().forEachOrdered(o -> {
			cn.hutool.http.HttpUtil.post("http://localhost:7070/kill", "name=lind&index=" + o);
		});

	}

	@Test
	public void fork() {
		/**
		 * 可以看得出来，结果是乱序输出的，且参与并行处理的线程有主线程以及ForkJoinPool中的worker线程 main num:3
		 * ForkJoinPool.commonPool-worker-2 num:2 ForkJoinPool.commonPool-worker-2 num:1
		 * ForkJoinPool.commonPool-worker-3 num:5 ForkJoinPool.commonPool-worker-3 num:4
		 */
		Integer[] array = new Integer[] { 1, 2, 3, 4, 5 };
		Arrays.asList(array).parallelStream().forEach(i -> {
			System.out.println(Thread.currentThread().getName() + " num:" + i);
		});
	}

}
