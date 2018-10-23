package com.bonc.rabbitMq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 *	@author haixia.shi
 *	@date 2017年9月18日
 *
 */
//@Configuration
public class RabbitMQSHXConfigration {

	/**
	 * 新增申报信息
	 * @return
	 */
	@Bean
	public Queue insertDeclareInfoQueue(){
		return new Queue("declare_shx_insertDeclareInfo");
	}
	
	/**
	 * 修改申报信息
	 * @return
	 */
	@Bean
	public Queue updateDeclareInfoQueue(){
		return new Queue("declare_shx_updateDeclareInfo");
	}
	
	/**
	 * 上传产品文件
	 * @return
	 */
	@Bean
	public Queue upLoadProductWordQueue(){
		return new Queue("declare_shx_upLoadProductWord");
	}
	
	/**
	 * 上传方案文件
	 * @return
	 */
	@Bean
	public Queue upLoadPlanWordQueue(){
		return new Queue("declare_shx_upLoadPlanWord");
	}
	
	/**
	 * 删除产品文件
	 * @return
	 */
	@Bean
	public Queue deleteProductFileQueue(){
		return new Queue("declare_shx_deleteProductFile");
	}
	
	/**
	 * 删除方案文件
	 * @return
	 */
	@Bean
	public Queue deletePlanFileQueue(){
		return new Queue("declare_shx_deletePlanFile");
	}
}
