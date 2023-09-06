package com.lind.common.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lind
 * @date 2023/9/6 9:27
 * @since 1.0.0
 */
// 当使用 Netty 创建服务器时，你可以自定义一个 ChannelHandler 来处理客户端与服务器之间的通信。这个自定义的 ChannelHandler
// 类将包含你自己的业务逻辑。下面是一个示例的 MyServerHandler 类，演示了如何创建一个简单的服务器端处理器：
public class MyServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 读取客户端发送的数据
		ByteBuf buffer = (ByteBuf) msg;
		try {
			// 将数据从 ByteBuf 转换为字符串
			String request = buffer.toString(io.netty.util.CharsetUtil.UTF_8);

			// 在控制台上打印收到的数据
			System.out.println("Received from client: " + request);

			// 在这里可以处理请求并向客户端发送响应
			// 例如，可以执行业务逻辑并将结果写回客户端
		}
		finally {
			// 释放资源
			buffer.release();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 发生异常时的处理逻辑
		cause.printStackTrace();
		ctx.close();
	}

}
