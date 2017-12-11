package codec;

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Created by root on 17-12-11.
 */

/*
*注意：这里的获取编解码的方法导致实现不同可以使得编码或解码器在程序中只是单例
*     尤其解码器,一般实现的逻辑都是一样的,没必要多实例
**/

//自定义编解码器工厂,需要实现ProtocolCodecFactory接口,并实现getEncoder()和getDecoder()方法
public class StringCodecFactory implements ProtocolCodecFactory {
    String charset;

    //编码器全局单例
    private StringEncoder ENCODER = new StringEncoder();

    public StringCodecFactory(String charset){
        this.charset = charset;
    }

    //当client的第一个消息出去时会调用此方法获取编码器,且只调用一次,之后同一个session的消息出去都不会调用此方法了
    @Override
    public ProtocolEncoder getEncoder() throws Exception {
        System.out.println("Get encoder!");
        return ENCODER;
    }

    //当client的第一个消息进来时会调用此方法获取解码器,且只调用一次,之后同一个session的消息出去都不会调用此方法了
    //解码器一个session对应一个实例
    @Override
    public ProtocolDecoder getDecoder() throws Exception {
        System.out.println("Get decoder!");
//        throw new Exception("Get decoder Exception");
        return new StringDecoder(charset);
    }
}
