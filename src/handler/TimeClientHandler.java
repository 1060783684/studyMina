package handler;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

/**
 * Created by root on 17-12-11.
 */
public class TimeClientHandler extends IoHandlerAdapter {

    //当和server连接上时发送消息
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        String data = "Hello!";
        session.write(data);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("Client close!");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String msg = message.toString();
        System.out.println("Server message : " + msg);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("client sent message");
    }
}
