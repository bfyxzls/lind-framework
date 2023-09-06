package com.lind.common.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author lind
 * @date 2023/9/6 9:27
 * @since 1.0.0
 */
public class SimpleServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// 添加自定义的处理器到管道
		pipeline.addLast(new MyServerHandler());
	}

}
