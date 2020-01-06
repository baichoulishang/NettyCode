package nia.chapter8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 代码清单 8-4 引导服务器
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 * @author <a href="mailto:mawolfthal@gmail.com">Marvin Wolfthal</a>
 */
public class BootstrapServer {

    /**
     * 代码清单 8-4 引导服务器
     */
    public void bootstrap() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        // 创建 ServerBootstrap.作为对比,客户端使用的是Bootstrap
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 设置 EventLoopGroup，其提供了用于处理 Channel 事件的EventLoop
        bootstrap.group(group)
                // 指定要使用的 Channel 实现
                // ServerChannel一般是用来管理子Channel的
                .channel(NioServerSocketChannel.class)
                // 设置用于处理已被接受的子 Channel 的I/O及数据的 ChannelInboundHandler
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data");
                    }
                });
        // 通过配置好的 ServerBootstrap 的实例绑定该 Channel
        // 监听端口8080
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        // 添加监听器,话说这个监听器是干嘛用的?
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound");
                } else {
                    System.err.println("Bind attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
