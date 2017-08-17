package com.lty.video.handlers;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lty.video.client.TcpClient;

public class ConnectionListener implements ChannelFutureListener {
	private static final Logger LOG = LoggerFactory.getLogger("bizLog");
	
	private TcpClient client;  
	
	private int reconnectionTime;
	  
	  
    public ConnectionListener(TcpClient client,int reconnectionTime) {  
        this.client = client;  
        this.reconnectionTime = reconnectionTime; 
    }  
  

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if (!future.isSuccess()&&!future.channel().isActive()) {  
			Thread.sleep(reconnectionTime);
            System.out.println("Reconnection..........."); 
            LOG.info("*******Reconnectio!*******");
            client.getB().group().shutdownGracefully();
            client.reStart();
        } else{
        	LOG.info("*******starting scuess!*******");
        	System.out.println("starting scuess!!!!!!!!!!!!!!!!!");  
        }
		
	}

}
