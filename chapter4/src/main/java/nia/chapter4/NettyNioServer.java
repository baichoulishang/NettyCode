package nia.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Listing 4.4 Asynchronous networking with Netty
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class NettyNioServer {
    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        // 为非阻塞模式使用NioEventLoopGroup
        // 这次使用的是NIO的NioEventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    // 使用NIO的Channel
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    // 指定 ChannelInitializer，对于每个已接受的连接都调用它
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                                      @Override
                                      public void initChannel(SocketChannel ch) throws Exception {
                                          // 添加 ChannelInboundHandlerAdapter以接收和处理事件
                                          ChannelInboundHandlerAdapter adapter = new ChannelInboundHandlerAdapter() {
                                              @Override
                                              public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                                  // 将消息写到客户端，并添加ChannelFutureListener，
                                                  ctx.writeAndFlush(buf.duplicate())
                                                          .addListener(ChannelFutureListener.CLOSE);// 以便消息一被写完就关闭连接
                                              }
                                          };
                                          ch.pipeline().addLast(adapter);
                                      }
                                  }
                    );
            // 绑定服务器以接受连接
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            // 释放所有的资源
            group.shutdownGracefully().sync();
        }
    }

}

