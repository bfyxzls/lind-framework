package com.lind.common.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author lind
 * @date 2023/9/6 9:42
 * @since 1.0.0
 */
public class NettyClientTest {

	/**
	 * 客户端向上面的服务端发送Hello, Server!的消息.
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		String host = "localhost";
		int port = 70;
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap clientBootstrap = new Bootstrap();
			clientBootstrap.group(workerGroup).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// 添加客户端处理器
							ch.pipeline().addLast(new MyClientHandler());
						}
					});

			// 连接到服务器
			ChannelFuture connectFuture = clientBootstrap.connect(host, port).sync();

			// 获取连接的通道
			Channel channel = connectFuture.channel();

			// 发送消息到服务器
			String message = "Hello, Server!";
			channel.writeAndFlush(message);

			// 等待连接关闭
			channel.closeFuture().sync();
		}
		finally {
			workerGroup.shutdownGracefully();
		}
	}

}
