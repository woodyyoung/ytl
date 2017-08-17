package com.lty.video.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpDecoder extends ByteToMessageDecoder{
	private static final Logger LOG = LoggerFactory.getLogger("bizLog");
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		try {
			if(!in.isReadable()){
				System.out.println("msg not read able!************");
				return;
			}
			in.markReaderIndex();
			//获取协议标识
			int uiMsgFlag = in.readInt();
			// 获取协议的类型
			int uiMsgType = in.readInt();
			// 获取消息长度
			int uiMsgLen = in.readInt()-1;
			//获取版本号
			int uiVersion = in.readInt();
			
			// 获取消息序列号uiSequance
			byte[] sequance =  in.readBytes(32).array();
			// 组装协议头
			TcpHeader header = new TcpHeader(uiMsgFlag,uiMsgType, uiMsgLen, uiVersion,new String(sequance));
			
			//可读字节小于该包的长度，出现粘包情况，等待下一次处理
			if(uiMsgLen > in.readableBytes()){
				in.resetReaderIndex();
				return;
			}
			//可读字节大于该包的长度，出现拆包情况，处理完成继续decode
			if(uiMsgLen < in.readableBytes()){
			    // 读取消息内容
			    byte[] content = in.readBytes(uiMsgLen).array();
			    TcpMessage message = new TcpMessage(header, new String(content));
			    out.add(message);
				
			    //继续decode
			    decode(ctx, in, out);
			}else{
			    // 读取消息内容
			    byte[] content = in.readBytes(uiMsgLen).array();
			    TcpMessage message = new TcpMessage(header, new String(content));
			    out.add(message);
			}
		} catch (Exception e) {
			LOG.error("TcpDecoder decode exception *****"+e.getMessage(),e);
		}
	}


}
