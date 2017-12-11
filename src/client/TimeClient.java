package client;

import codec.StringCodecFactory;
import handler.TimeClientHandler;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * Created by root on 17-12-11.
 */
/*
*  mina中中的client实现比较神奇,与netty有一定的区别,
*  主要区别在于handler的事件处理
**/
public class TimeClient {

    private static int PORT = 9999;
    private static String HOST = "127.0.0.1";

    public static void main(String[] args){
        //创建连接器,即mina客户端
        SocketConnector connector = new SocketConnector();
        SocketConnectorConfig config = new SocketConnectorConfig();
        //设置连接超时时间
        config.setConnectTimeout(60);
        config.getFilterChain().addLast("codec",new ProtocolCodecFilter(
                new StringCodecFactory("UTF-8")));

        ConnectFuture future  = connector.connect(
                new InetSocketAddress(HOST,PORT),new TimeClientHandler(),config);
        //异步等待connector线程关闭,
        //这个方法现在已经被awaitUninterruptibly取代
        future.join();
        //若未连接则直接返回
        if(!future.isConnected()){
            return;
        }

        //这样获取了session就可以在mina框架以外的地方使用session了
        IoSession session = future.getSession();

        Scanner in = new Scanner(System.in);
        while (true){
            if(session.isClosing()){
                return;
            }
            String msg = in.nextLine();
            session.write(msg);
        }
    }
}
