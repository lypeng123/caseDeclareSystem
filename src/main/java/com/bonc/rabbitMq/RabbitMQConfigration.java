package com.bonc.rabbitMq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
*	定制队列名称
* @author zhijie.ma
* @date 2017年6月2日
* 
*/
@Configuration
public class RabbitMQConfigration {
	
//	@Bean
//	public Queue sendRegistEmailQueue(){
//		return new Queue("declare_mzj_sendRegistEmail");
//	}
//	
//	@Bean
//	public Queue addEmail() {
//		return new Queue("declare_mzj_registEmailUser");
//	}
//	
//	@Bean
//	public Queue addUser() {
//		return new Queue("declare_mzj_registUser");
//	}
	
	@Bean
	public Queue sendEmailScreenResult() {
		return new Queue("sendEmailScreenResult");
	}
}
