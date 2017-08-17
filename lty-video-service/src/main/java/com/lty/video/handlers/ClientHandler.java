package com.lty.video.handlers;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import com.lty.video.client.TcpClient;
@Component
@Qualifier("clientHandler")
@Sharable
public class ClientHandler  extends SimpleChannelInboundHandler<TcpMessage> implements ApplicationContextAware {
	private static final Logger LOG = LoggerFactory.getLogger("bizLog");
	
	@Value("${netty.readerIdleTimeSeconds}")
	private int readerIdleTimeSeconds;//心跳时间  多久没有读取到服务器的消息
	@Value("${netty.writerIdleTimeSeconds}")//心跳时间  多久往服务器写如消息
	private int writerIdleTimeSeconds;
	
	public static ApplicationContext applicationContext;//应用上下文
	
	public static Map<String, DeferredResult<String>> suspendedTradeRequests = new ConcurrentHashMap<String, DeferredResult<String>>();
	
 	@Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TcpMessage message) throws Exception {
        try {
			int  msgType = message.getTcpHeader().getUiMsgType();
			LOG.info("msgType:*****"+msgType);
			//如果是推送的码率消息，不处理
			if(TcpMessage.MSG_NOTIFY_STREAMSTATE == msgType){
				LOG.debug("receiveMsg*****"+message);
			}else{
				LOG.info("receiveMsg*****"+message);
			}
			//请求在线设备响应
			if(TcpMessage.MSG_GETONLINELIST_RESP == msgType){
				getDeviceHandler().hand(message);
				return;
			}
			//推送设备状态信息
			if(TcpMessage.MSG_NOTIFY_DEVSTATE == msgType){
				getDeviceHandler().hand(message);
				return;
			}
			//#define MSG_NOTIFY_SERVERSTATE	0x201//推送服务器状态信息
			//#define MSG_NOTIFY_STREAMSTATE	0x202 //推送设备推流状态
			//推送服务器状态信息
			if(TcpMessage.MSG_NOTIFY_SERVERSTATE == msgType){
				getPushHandler().hand(message);
				return;
			}
			//推送设备推流状态
			if(TcpMessage.MSG_NOTIFY_STREAMSTATE == msgType){
				//getChannelSpeedHandler().hand(message);暂不处理
				return;
			}
			//默认是应答处理
			getResponseHandler().hand(message);
		} catch (Exception e) {
			LOG.error("ClientHandler hand exception *****"+e.getMessage(),e);
		}
    }
 	
 	@Override
 	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
 			throws Exception {
 		cause.printStackTrace();
 		LOG.error("exceptionCaught*****"+cause.getMessage(),cause);
 		ctx.close();
 	}
 	
 	
 	
 	@Override
 	public void channelActive(ChannelHandlerContext ctx) throws Exception {
 		LOG.info(ctx.channel().remoteAddress() + "Channel is Active");
 	}
 	@Override
 	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
 		LOG.info(ctx.channel().remoteAddress() + "Channel is Inactive");
		ctx.channel().eventLoop().shutdownGracefully();
		
		LOG.info(ctx.channel().remoteAddress() + "excute reConnnect server!");
		TcpClient bean = ClientHandler.applicationContext.getBean(TcpClient.class);
		bean.reStart();;
		     
 	}
 	
 	@Override
 	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
 			throws Exception {
 		if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE){
				LOG.info("read idle！");
			}
			if (event.state() == IdleState.WRITER_IDLE){
				LOG.info("write idle！");
				ctx.channel().writeAndFlush(TcpMessage.buildHeartMsg());
			}
			if (event.state() == IdleState.ALL_IDLE){
				LOG.info("all idle！");
			}
		}
 	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ClientHandler.applicationContext = applicationContext;
	}
	
	public static DeviceHandler  getDeviceHandler(){
		return ClientHandler.applicationContext.getBean(DeviceHandler.class);
	}
	public static ResponseHandler  getResponseHandler(){
		return ClientHandler.applicationContext.getBean(ResponseHandler.class);
	}
	public static PushHandler  getPushHandler(){
		return ClientHandler.applicationContext.getBean(PushHandler.class);
	}
	
	public static TcpClient  getTcpClient(){
		return ClientHandler.applicationContext.getBean(TcpClient.class);
	}
	
	/*public static ChannelSpeedHandler getChannelSpeedHandler(){
		return ClientHandler.applicationContext.getBean(ChannelSpeedHandler.class);
	}*/
 	
}
