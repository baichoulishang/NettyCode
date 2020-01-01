package nia.chapter2.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Listing 2.2 EchoServer class
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        // if (args.length != 1) {
        //     System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
        //     return;
        // }

        // 设置端口值（如果端口参数的格式不正确，则抛出一个NumberFormatException）
        int port = Integer.parseInt("8888");
        // 启动服务器
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        // 配置入站处理器,即ChannelInboundHandler的实例
        final EchoServerHandler serverHandler = new EchoServerHandler();
        // 创建EventLoopGroup
        // 因为我们使用的是NIO,所以这里使用的是NioEventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)// 在ServerBootstrap上绑定EventLoopGroup
                    .channel(NioServerSocketChannel.class)// 绑定Channel
                    .localAddress(new InetSocketAddress(port))// 使用指定的端口设置套接字地址.服务器将绑定到这个端口并监听新的连接请求
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 添加一个 EchoServerHandler 到子 Channel 的 ChannelPipeline
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加EchoServerHandler实例到ChannelPipeline,它是ChannelInboundHandler的实例
                            // 这个ChannelPipeline的作用就是持有一个 ChannelHandler 的实例链，这玩意估计就是用来传递事件用的
                            pipeline.addLast(serverHandler);// EchoServerHandler被标注为@Shareable, 所以我们可以总是使用同样的实例
                        }
                    });
            // 异步地绑定服务器；调用 sync()方法阻塞等待直到绑定完成
            // ChannelFuture是用来查看结果的
            ChannelFuture f = bootstrap.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listening for connections on " + f.channel().localAddress());
            // 获取 Channel 的CloseFuture且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup的资源.这个sync估计也是用来阻塞的
            group.shutdownGracefully().sync();
        }
    }
}
