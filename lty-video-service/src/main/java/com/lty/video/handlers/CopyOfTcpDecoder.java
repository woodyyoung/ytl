/*package com.lty.video.handlers;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class CopyOfTcpDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if(!in.isReadable()){
			System.out.println("msg not read able!************");
			return;
		}
		in.markReaderIndex();
		// 获取协议的版本
        int uiMsgType = in.readInt();
        // 获取消息长度
        int uiMsgLen = in.readInt();
        
        //可读字节小于该包的长度，出现粘包情况，等待下一次处理
        if(uiMsgLen+32 > in.readableBytes())
        {
        	in.resetReaderIndex();
        	return;
        }
        
        // 获取消息序列号uiSequance
        byte[] sequance =  in.readBytes(32).array();
        // 组装协议头
        TcpHeader header = new TcpHeader(uiMsgType, uiMsgLen, new String(sequance));
        
        
        
        // 读取消息内容
        byte[] content = in.readBytes(uiMsgLen).array();

        TcpMessage message = new TcpMessage(header, new String(content));
        out.add(message);
		
	}

}
*/