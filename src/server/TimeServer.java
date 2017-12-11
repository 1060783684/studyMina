package server;

import codec.StringCodecFactory;
import handler.TimeServiceHandler;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoAcceptor;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.LoggingFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by root on 17-12-11.
 */
public class TimeServer {
    private static int PORT = 9999;
    public static void main(String[] args) throws IOException {

        //对ByteBuffer进行配置:
        //是否使用直接内存: true:是   false:否
        //这里不使用直接内存
        ByteBuffer.setUseDirectBuffers(false);

        //当使用ByteBuffer.allocate方法创建buffer时由谁分配
        //SimpleByteBufferAllocator：每次创建一个新的缓存
        //CachedBufferAllocator：没用过......,据说是以前没用的buffer会缓存下来,供下一次使用
        ByteBuffer.setAllocator(new SimpleByteBufferAllocator());

        //1.1这个版本只有几个简单的Acceptor
        IoAcceptor acceptor = new SocketAcceptor();

        //生成一个Acceptor的配置器
        SocketAcceptorConfig config = new SocketAcceptorConfig();

        //获取过滤链并在过滤链末尾添加过滤器
        //这里添加了日志过滤器并将其命名为"logger"
        config.getFilterChain().addLast("logger",new LoggingFilter());

        //也可以这么实现,但是两种实现方式最好不要混着来,这里采用config方式
        //acceptor.getFilterChain().addLast("logger",new LoggingFilter());

        //添加一个mina自带的编解码器,可以对字符串进行编解码,
        // 进入server时将buffer解码成String,出去时将String编码成buffer
//        config.getFilterChain().addLast("codec",new ProtocolCodecFilter(
//                new TextLineCodecFactory(Charset.forName("UTF-8"))));

        //使用自己的编解码工厂
        config.getFilterChain().addLast("codec",new ProtocolCodecFilter(
                new StringCodecFactory("UTF-8")));

        //对session的设置也要通过config,这里设置了每个session的读缓冲区的大小
        config.getSessionConfig().setReceiveBufferSize(2048);
        //设置不使用nagle算法
        config.getSessionConfig().setTcpNoDelay(true);

        //绑定端口以及Handler,这个实现比较死板，监听端口和handler必须同时绑定
        //这里还使用SocketAcceptorConfig对Acceptor进行了配置
        acceptor.bind(
                new InetSocketAddress(PORT),new TimeServiceHandler(),config);
    }
}
