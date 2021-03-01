package example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;

import java.net.InetSocketAddress;

public class SocksClientProxy {
    static final int PROXY_PORT = Integer.parseInt(System.getProperty("port", "9988"));

    static final int PORT = Integer.parseInt(System.getProperty("port", "1098"));
    static final String HOST = System.getProperty("host", "127.0.0.1");

    public static void main(String[] args) throws Exception{
            NioEventLoopGroup worker = new NioEventLoopGroup();
            NioEventLoopGroup boss = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();

            b.group(boss, worker)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new Socks5ProxyHandler(new InetSocketAddress(HOST,PORT)));
                        }
                    })
                    .channel(NioServerSocketChannel.class);

            b.bind(PROXY_PORT).sync().channel().closeFuture().sync();

        }finally {
            worker.shutdownGracefully();
        }
    }
}
