package example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;

import java.net.InetSocketAddress;

public class SocksClient {

    static final int PORT = Integer.parseInt(System.getProperty("port", "1098"));
    static final String HOST = System.getProperty("host", "127.0.0.1");

    public static void main(String[] args) throws Exception{
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addFirst(new Socks5ProxyHandler(new InetSocketAddress(HOST,PORT)));
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        }
                    });
            b.connect(HOST,PORT).sync().channel().closeFuture().sync();
        }finally {
            worker.shutdownGracefully();
        }
    }
}
