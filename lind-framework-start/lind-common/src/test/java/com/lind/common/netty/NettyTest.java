package com.lind.common.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author lind
 * @date 2023/9/6 8:49
 * @since 1.0.0
 */
public class NettyTest {

	// 通过telnet localhost 70 ，可以向服务端发消息，服务端在控制台消息
	public static void main(String[] args) throws InterruptedException {
		// 创建事件循环组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			// 创建 ServerBootstrap
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new SimpleServerInitializer()); // 自定义通道处理器

			// 绑定端口
			serverBootstrap.bind(70).sync().channel().closeFuture().sync();
		}
		finally {
			// 关闭事件循环组
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
