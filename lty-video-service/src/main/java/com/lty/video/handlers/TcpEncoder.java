package com.lty.video.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToByteEncoder;

@Sharable
public class TcpEncoder  extends MessageToByteEncoder<TcpMessage>{

	@Override
	protected void encode(ChannelHandlerContext ctx, TcpMessage msg,
			ByteBuf out) throws Exception {
		  // 将Message转换成二进制数据
        TcpHeader header = msg.getTcpHeader();

        // 这里写入的顺序就是协议的顺序.

        // 写入Header信息
        out.writeInt(header.getUiMsgFlag());
        out.writeInt(header.getUiMsgType());
        out.writeInt(msg.getContent().length());
        out.writeInt(header.getUiVersion());
        out.writeBytes(header.getUiSequence().getBytes());
        //out.writeLong(header.getUiSequence());

        // 写入消息主体信息
        out.writeBytes(msg.getContent().getBytes());
		
	}

}
