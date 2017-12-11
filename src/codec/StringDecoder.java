package codec;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * Created by root on 17-12-11.
 */
public class StringDecoder implements ProtocolDecoder {
    private String charset;

    public StringDecoder(String charset){
        this.charset = charset;
    }

    //当client消息到达时调用此方法解码
    @Override
    public void decode(IoSession ioSession, ByteBuffer byteBuffer, ProtocolDecoderOutput out) throws Exception {
        //limit代表可写或可读的极限,这里代表可读的极限
        byte[] data = new byte[byteBuffer.limit()];
        //将buffer中的所有数据写入byte数组中
        byteBuffer.get(data);
        //使用charset属性中的编码类型编码
        String msg = new String(data,charset);
        //将消息透传下去
        out.write(msg);

//        throw new Exception("Decoder Exception");
    }

    //当session关闭时处理剩余数据
    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    //销毁解码器时释放关联的资源,一般不关心
    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
