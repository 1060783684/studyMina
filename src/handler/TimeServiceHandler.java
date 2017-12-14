package handler;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by root on 17-12-11.
 */
//在mina框架中这个handler是一个单例一个框架acceptor只有一个
public class TimeServiceHandler extends IoHandlerAdapter {
    private final AtomicInteger clientCount = new AtomicInteger(0);

    //当session被创建时调用
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("Session create succeed !");
        //测试exceptionCaught方法,会捕获
        //throw new Exception("Create Excepion!");
    }

    //当session被打开时调用,一般在sessionCreated后被调用,一般这两个方法基本是同时调用的
    //建议是能在sessionOpened中处理的不要再sessionCreated中处理
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        int count = clientCount.incrementAndGet();
        System.out.println("count : " + count);
    }

    //当server收到client消息被时调用
    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String msg = message.toString();

        System.out.println(session.getRemoteAddress() + " message : " + msg);

        if("quit".equalsIgnoreCase(msg) ||
                "quit\n".equalsIgnoreCase(msg) ||
                "quit\r".equalsIgnoreCase(msg) ||
                "quit\r\n".equalsIgnoreCase(msg) ||
                "quit\n\r".equalsIgnoreCase(msg)){
            session.write("bye bye!\n");
            session.close();
        }

        //获取当前时间
        Date date = new Date();
        //EE:星期 MM:月份 dd:日 yyyy:年 HH:时 mm:分 ss:秒
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String formatDate = format.format(date);

        session.write("now : " + formatDate + "\n");
    }

    //mina框架中用户程序抛出的异常都会由其捕获并处理,包括Filter中的异常
    @Override
    public void exceptionCaught(IoSession session,Throwable cause) throws Exception{
        System.out.println("mina caught exception");
        cause.printStackTrace();
    }

    //当session关闭时调用
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("Session is close!");
    }

}
