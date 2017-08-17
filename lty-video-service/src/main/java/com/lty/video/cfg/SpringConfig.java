package com.lty.video.cfg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.lty.video.handlers.ClientInitializer;


@Configuration
@ComponentScan("com.lty.video")
@PropertySource("file:${user.dir}/conf/config.properties")
public class SpringConfig {
	
	@Value("${netty.worker.thread.count}")
	private int workerCount;//netty线程数量

	@Value("${netty.tcp.addr}")
	private String tcpAddr;//netty服务端地址
	
	@Value("${netty.tcp.port}")//netty服务端IP
	private int tcpPort;

	@Value("${netty.so.keepalive}")//保活与心跳
	private boolean keepAlive;
	
	@Value("${netty.so.backlog}")//队列长度
	private int backlog;

	@Value("${netty.reconnectionTime}")
	private int reconnectionTime;
	
	@Value("${netty.readerIdleTimeSeconds}")
	private int readerIdleTimeSeconds;//心跳时间  多久没有读取到服务器的消息
	@Value("${netty.writerIdleTimeSeconds}")//心跳时间  多久往服务器写如消息
	private int writerIdleTimeSeconds;
	
	
	@SuppressWarnings("unchecked")
	@Bean(name = "bootstrap")
	public Bootstrap Bootstrap() {
		Bootstrap b = new Bootstrap();
		b.group(workerGroup() )
              .channel(NioSocketChannel.class)
              .handler(new ClientInitializer(readerIdleTimeSeconds,writerIdleTimeSeconds));
		Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
		Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
		for (@SuppressWarnings("rawtypes")
		ChannelOption option : keySet) {
			b.option(option, tcpChannelOptions.get(option));
		}
		return b;
	}


	@Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
	public NioEventLoopGroup workerGroup() {
		return new NioEventLoopGroup(workerCount);
	}

	@Bean(name = "tcpSocketAddress")
	public InetSocketAddress tcpPort() {
		return new InetSocketAddress(tcpAddr,tcpPort);
	}

	@Bean(name = "tcpChannelOptions")
	public Map<ChannelOption<?>, Object> tcpChannelOptions() {
		Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
		options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
		options.put(ChannelOption.SO_BACKLOG, backlog);
		return options;
	}


	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
}
