package com.bonc.caseDeclare.service;

import javax.servlet.http.HttpServletRequest;

import com.bonc.util.JsonResult;

/**
 * 登陆注册服务
 * @author zhijie.ma
 * @date 2017年9月10日
 *
 */
public interface LoginService {
	
	/**
	 * 提供用户注册功能类
	 * @param map
	 * @return
	 */
	JsonResult<Object> registUser(HttpServletRequest req,String IP);
	
	/**
	 * 登陆
	 * @param req
	 * @param IP	IP地址
	 * @return
	 */
	JsonResult<Object> loginUser(HttpServletRequest req,String IP);
	
	/**
	 * 异步邮箱注册
	 * @param req
	 * @param IP	IP地址
	 * @return
	 */
	JsonResult<Object> registEmail(HttpServletRequest req,String IP);
	
	/**
	 * 获取所有省份信息
	 * @return
	 */
	JsonResult<Object> getAllProvince();
}
