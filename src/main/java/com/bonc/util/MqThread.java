package com.bonc.util;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 多线程     
 * 使用方法：在要加线程的方法上加一个注解即可    Async("mqExecutor")
 * 		
 * @author zhijie.ma
 * @date 2017年9月14日
 *
 */
//@Configuration
//@EnableAsync
public class MqThread {

	private int corePoolSize = 10;

	private int maxPoolSize = 30;

	private int queueCapacity = 8;

	private int keepAlive = 60;

	@Bean
	public Executor mqExecutor() {
		System.out.println("MqThread线程++++++++++++++++++++++++++++");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("mqExecutor-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setKeepAliveSeconds(keepAlive);
		executor.initialize();
		return executor;
	}

}
