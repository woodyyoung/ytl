package com.lty.video.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.lty.video.handlers.ClientInitializer;
import com.lty.video.handlers.ConnectionListener;
import com.lty.video.handlers.TcpMessage;
@Service("tcpClient")
@Sharable
public class TcpClient {
	private static final Logger LOG = LoggerFactory.getLogger("bizLog");
	@Autowired
	@Qualifier("bootstrap")
	private Bootstrap b;
	@Autowired
	@Qualifier("tcpSocketAddress")
	private InetSocketAddress tcpPort;

	private ChannelFuture channelFuture;
	
	@Value("${netty.so.keepalive}")
	private boolean keepAlive;

	@Value("${netty.so.backlog}")
	private int backlog;
	
	@Value("${netty.reconnectionTime}")
	private int reconnectionTime;
	
	@Value("${netty.readerIdleTimeSeconds}")
	private int readerIdleTimeSeconds;//心跳时间  多久没有读取到服务器的消息
	@Value("${netty.writerIdleTimeSeconds}")//心跳时间  多久往服务器写如消息
	private int writerIdleTimeSeconds;
	
	private boolean reStarting  = false;

	@PostConstruct
	public void start(){
		LOG.info("************Starting client at " + tcpPort+ "***********");
		try {
			channelFuture = b.connect(tcpPort).addListener(new ConnectionListener(this,reconnectionTime)).sync();
		} catch (Exception e) {
			LOG.error("************start error！" + tcpPort+" ************");
			LOG.error(e.getMessage(),e);
		}
	}
	
	public void reStart()  {
		reStarting = true;
		LOG.info("************reStarting client!***********");
		if(channelFuture!=null){
			channelFuture.channel().close();
			channelFuture.channel().closeFuture();
			channelFuture.channel().eventLoop().shutdownGracefully();
		}
		EventLoopGroup group = new NioEventLoopGroup();
		b = new Bootstrap();
		b.group(group)
              .channel(NioSocketChannel.class)
              .handler(new ClientInitializer(readerIdleTimeSeconds,writerIdleTimeSeconds));
		b.option(ChannelOption.SO_KEEPALIVE, keepAlive);
		b.option(ChannelOption.SO_BACKLOG, backlog);
		try {
			channelFuture = b.connect(tcpPort).addListener(new ConnectionListener(this,reconnectionTime)).sync();
			requestAllDevice();
		} catch (Exception e) {
			LOG.error("************reStart error！" + tcpPort+" ************");
			LOG.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}


	@PreDestroy
	public void stop() {
		if(channelFuture!=null){
			channelFuture.channel().eventLoop().shutdownGracefully();
		}
	}

	public Bootstrap getB() {
		return b;
	}

	public void setB(Bootstrap b) {
		this.b = b;
	}

	public InetSocketAddress getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(InetSocketAddress tcpPort) {
		this.tcpPort = tcpPort;
	}
	
	public void send(TcpMessage msg) {
		LOG.info("sendMsg:******"+msg);
		if(channelFuture==null){
			return ;
		}
		if(!channelFuture.channel().isActive()){
			if(reStarting){
				return ;
			}
			reStart();
			return;
		}
		reStarting=false;
		channelFuture.channel().writeAndFlush(msg);
	}
	
	public void requestAllDevice(){
		TcpMessage msg = TcpMessage.build(TcpMessage.MSG_GETONLINELIST_REQ, "{\"bus_id\":-1}");
		send(msg);
	}
}
