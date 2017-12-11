package codec;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;


/**
 * Created by root on 17-12-11.
 */
public class StringEncoder implements ProtocolEncoder {
    //当消息出去时调用此方法对消息进行编码
    @Override
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput out) throws Exception {
        String msg = o.toString();
        byte[] data = msg.getBytes();

        //调用ByteBuffer的静态allocate方法获取ByteBuffer实例
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        //设置buffer当大小不够时自增长
        buffer.setAutoExpand(true);
        //在写之前设置要写的开始位置
        buffer.position(0);
        //将数据写入buffer中
        buffer.put(data,0,data.length);
        //这里需要注意,写完后记得flip,
        // 因为当调用mina框架中的写出方法时mina框架会按正常方式读取buffer中的数据
        buffer.flip();
        //将解码后的消息写出
        out.write(buffer);
    }

    //销毁编码器时释放关联的资源,一般不关心
    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
