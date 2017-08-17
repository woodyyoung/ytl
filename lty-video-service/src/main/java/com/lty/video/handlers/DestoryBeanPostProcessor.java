package com.lty.video.handlers;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
@Component
public class DestoryBeanPostProcessor implements ApplicationListener<ContextClosedEvent> {
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		if(event.getApplicationContext().getParent() == null){
			System.out.println("容器被关闭中.......!");
			try {  
	            Thread.sleep(10000);  
	        } catch (InterruptedException e) {  
	            e.printStackTrace();  
	        }  
			System.out.println("容器已关闭.......!");
		}
		
	}

}
