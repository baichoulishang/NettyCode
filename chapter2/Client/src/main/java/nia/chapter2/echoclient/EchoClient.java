package nia.chapter2.echoclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Listing 2.4 Main class for the client
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // 开启客户端的方法
    public void start() throws Exception {
        // 创建EventLoopGroup,同时也是NIO的
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            // 指定 EventLoopGroup 以处理客户端事件；需要适用于 NIO 的实现
            b.group(group)
                    .channel(NioSocketChannel.class)// 绑定NIO的Channel
                    .remoteAddress(new InetSocketAddress(host, port))// 使用主机和端口参数来连接远程地址，也就是这里的 Echo 服务器的地址，而不是绑定到一个一直被监听的端口。
                    .handler(new ChannelInitializer<SocketChannel>() {// 使用ChannelInitializer向 ChannelPipeline 中添加 ClientHandler 实例
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            // 连接到远程节点，阻寒等待直到连接完成
            ChannelFuture f = b.connect().sync();
            // 阻塞知道Channel关闭
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args)
            throws Exception {
        // if (args.length != 2) {
        //     System.err.println("Usage: " + EchoClient.class.getSimpleName() + " <host> <port>"
        //     );
        //     return;
        // }

        final String host = "localhost";
        final int port = Integer.parseInt("8888");
        new EchoClient(host, port).start();
    }
}

