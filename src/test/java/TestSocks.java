import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest;
import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
import io.netty.handler.proxy.Socks5ProxyHandler;
import org.junit.Test;
import org.junit.Assert;

import java.net.InetSocketAddress;
import java.util.Collections;

public class TestSocks {
    @Test
    public void test1() {
        ByteBuf buf = Unpooled.buffer(9);

        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }

        EmbeddedChannel channel = new EmbeddedChannel(
                new Socks5ProxyHandler(new InetSocketAddress("127.0.0.1", 1098))
        );



        channel.writeInbound(buf);
        Object in = channel.readInbound();
        channel.readOutbound();
        channel.writeOutbound(in);
        Object out = channel.readOutbound();

    }

    @Test
    public void test2(){

    }
}
