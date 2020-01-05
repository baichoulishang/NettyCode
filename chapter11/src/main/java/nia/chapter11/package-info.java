/**
 * Created by kerr.
 * <p>
 * 代码清单 11-1 添加 SSL/TLS 支持 {@link nia.chapter11.SslChannelInitializer}
 * <p>
 * 代码清单 11-2 添加 HTTP 支持 {@link nia.chapter11.HttpPipelineInitializer}
 * <p>
 * 代码清单 11-3 自动聚合 HTTP 的消息片段 {@link nia.chapter11.HttpAggregatorInitializer}
 * <p>
 * 代码清单 11-4 自动压缩 HTTP 消息 {@link nia.chapter11.HttpCompressionInitializer}
 * <p>
 * 代码清单 11-5 使用 HTTPS {@link nia.chapter11.HttpsCodecInitializer}
 * <p>
 * 代码清单 11-6 在服务器端支持 WebSocket {@link nia.chapter11.WebSocketServerInitializer}
 * <p>
 * 代码清单 11-7 发送心跳 {@link nia.chapter11.IdleStateHandlerInitializer}
 * <p>
 * 代码清单 11-8 处理由行尾符分隔的帧 {@link nia.chapter11.LineBasedHandlerInitializer}
 * <p>
 * 代码清单 11-9 使用 ChannelInitializer 安装解码器 {@link nia.chapter11.CmdHandlerInitializer}
 * <p>
 * 代码清单 11-10 使用 LengthFieldBasedFrameDecoder 解码器基于长度的协议 {@link nia.chapter11.LengthBasedInitializer}
 * <p>
 * 代码清单 11-11 使用 FileRegion 传输文件的内容 {@link nia.chapter11.FileRegionWriteHandler}
 * <p>
 * 代码清单 11-12 使用 ChunkedStream 传输文件内容 {@link nia.chapter11.ChunkedWriteHandlerInitializer}
 * <p>
 * 代码清单 11-13 使用 JBoss Marshalling {@link nia.chapter11.MarshallingInitializer}
 * <p>
 * 代码清单 11-14 使用 protobuf {@link nia.chapter11.ProtoBufInitializer}
 */
package nia.chapter11;