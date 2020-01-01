package nia.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Listing 4.3 Blocking networking with Netty
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class NettyOioServer {
    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        // 使用的是阻塞EventLoopGroup.这个已经被废弃了
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            // 创建 ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    // 使用 OioServerSocketChannel以允许阻塞模式（旧的I/O）
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    // 指定 ChannelInitializer，对于每个已接受的连接都调用它
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 添加一个 ChannelInboundHandlerAdapter以拦截和处理事件
                            // 不建议使用匿名内部类
                            ChannelInboundHandlerAdapter adapter = new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // 将消息写到客户端，并添加 ChannelFutureListener，
                                    ChannelFuture future = ctx.writeAndFlush(buf.duplicate())
                                            .addListener(
                                                    // 以便消息一被写完就关闭连接
                                                    ChannelFutureListener.CLOSE);
                                }
                            };
                            ch.pipeline().addLast(adapter);
                        }
                    });
            // 绑定服务器以接受连接
            ChannelFuture f = bootstrap.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            // 释放所有的资源
            group.shutdownGracefully().sync();
        }
    }

}

