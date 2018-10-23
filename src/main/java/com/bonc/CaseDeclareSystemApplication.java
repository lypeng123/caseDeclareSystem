package com.bonc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@ServletComponentScan	//开启拦截器
@EnableRedisHttpSession
@EnableTransactionManagement  //为项目增加事务
public class CaseDeclareSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaseDeclareSystemApplication.class, args);
	}
}
