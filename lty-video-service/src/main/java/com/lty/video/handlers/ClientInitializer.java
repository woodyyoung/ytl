package com.lty.video.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.timeout.IdleStateHandler;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
	private static final TcpEncoder ENCODER = new TcpEncoder();
	
	private static final int MAX_FRAME_LENGTH = 1024 * 10;  
	private static final int LENGTH_FIELD_OFFSET = 4; 
    private static final int LENGTH_FIELD_LENGTH = 4;  
    private static final int LENGTH_ADJUSTMENT = 32;  
    private static final int INITIAL_BYTES_TO_STRIP = 0;  
	
	private int readerIdleTimeSeconds;
	private int writerIdleTimeSeconds;
	
	public ClientInitializer(){
		
	}
	
	public ClientInitializer( int readerIdleTimeSeconds,
            int writerIdleTimeSeconds){
		this.readerIdleTimeSeconds = readerIdleTimeSeconds; 
		this.writerIdleTimeSeconds = writerIdleTimeSeconds; 
	}

	

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {

        ChannelPipeline pipeline = channel.pipeline();

        // 添加编解码器, 由于ByteToMessageDecoder的子类无法使用@Sharable注解,
        // 这里必须给每个Handler都添加一个独立的Decoder.
        pipeline.addLast("idleStateHandler", new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds,0));  
        
        //添加行分割符
        pipeline.addLast("handler1", new DelimiterBasedFrameDecoder(1024*1000,  Delimiters.lineDelimiter()));  
        
        pipeline.addLast(ENCODER);
        
        
        pipeline.addLast(new TcpDecoder());
        
        
		/*pipeline.addLast(new TcpDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET,
				LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP,
				true));
*/
        // and then business logic.
        pipeline.addLast(new ClientHandler());

    }
}
