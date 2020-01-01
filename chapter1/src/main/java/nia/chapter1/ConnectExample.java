package nia.chapter1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by kerr.
 * <p>
 * Listing 1.3 Asynchronous connect
 * <p>
 * Listing 1.4 Callback in action
 */
public class ConnectExample {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * Listing 1.3 Asynchronous connect
     * <p>
     * Listing 1.4 Callback in action
     */
    public static void connect() {
        Channel channel = CHANNEL_FROM_SOMEWHERE; //reference form somewhere
        // Does not block
        // 连接到远程节点上
        ChannelFuture future = channel.connect(new InetSocketAddress("192.168.0.1", 25));
        // 注册监听器.此处用注册来形容比较贴切
        future.addListener(new ChannelFutureListener() {
            // 当操作已经完成时,该监听器会被执行.但是,指的是什么操作????
            @Override
            public void operationComplete(ChannelFuture future) {
                // 当该监听器被通知连接已经建立的时候，要检查对应的状态(如果该操作是成功的，那么将数据写到该 Channel。
                if (future.isSuccess()) {
                    ByteBuf buffer = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
                    ChannelFuture wf = future.channel().writeAndFlush(buffer);
                    // ...and so on
                } else {
                    // 否则，要从 ChannelFuture 中检索对应的 Throwable。
                    Throwable cause = future.cause();
                    cause.printStackTrace();
                }
            }
        });
    }
}