package com.lty.video.handlers;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.lty.video.client.TcpClient;
@Component
public class InstantiationTracingBeanPostProcessor implements ApplicationListener<ContextRefreshedEvent> {
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		if(arg0.getApplicationContext().getParent() == null){
			System.out.println("容器初始化成功!");
			TcpClient bean = arg0.getApplicationContext().getBean(TcpClient.class);
			bean.requestAllDevice();
		}
	}
}
