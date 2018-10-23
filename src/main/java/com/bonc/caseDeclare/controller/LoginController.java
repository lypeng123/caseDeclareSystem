package com.bonc.caseDeclare.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.caseDeclare.service.LoginService;
import com.bonc.util.IPvalidateUtil;
import com.bonc.util.JsonResult;

/**
 * 登陆注册模块
 * @author zhijie.ma
 * @date 2017年9月8日
 *
 */
@RestController
@RequestMapping("login")
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;
	
	/**
	 * 登陆
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping("login")
	public JsonResult<Object> login(HttpServletRequest req,HttpServletResponse response){
		String ipAddr = IPvalidateUtil.getIpAddr(req).split(",")[0];// 访问的ip
		logger.info("IP为：【"+ipAddr+"】进行了登陆");
		
		JsonResult<Object> loginUser = loginService.loginUser(req, ipAddr);
		logger.info("登陆结果："+loginUser);
		
		return loginUser; 
	}
	
	/**
	 * 注册
	 * @param req
	 * @param response
	 * @return
	 */
	@RequestMapping("regist")
	//@Async("mqExecutor")
	public JsonResult<Object> regist(HttpServletRequest req,HttpServletResponse response){
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("注册转码失败！");
			return new JsonResult<>("输入内容不合法");
		}
		String ipAddr = IPvalidateUtil.getIpAddr(req).split(",")[0];// 访问的ip
		logger.info("IP为：【"+ipAddr+"】进行了注册");
		JsonResult<Object> registUser = loginService.registUser(req,ipAddr);
		logger.info("注册反馈信息为："+registUser);
		
		return registUser;
	}
	
//	@RequestMapping("registEmail")
	//@Async("mqExecutor")
	public JsonResult<Object> registEmail(HttpServletRequest req,HttpServletResponse response){
		try {
			req.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("注册邮箱转码失败！");
			return new JsonResult<>("输入内容不合法！");
		}
		String ipAddr = IPvalidateUtil.getIpAddr(req).split(",")[0];// 访问的ip
		logger.info("IP为：【"+ipAddr+"】进行了邮箱注册");
		JsonResult<Object> registUser = loginService.registEmail(req,ipAddr);
		logger.info("邮箱注册反馈信息为："+registUser);
		
		return registUser;
	}
	
	/**
	 * 登出
	 * @param req
	 * @return
	 */
	@RequestMapping("logout")
	public JsonResult<Object> logout(HttpServletRequest req){
		HttpSession session = req.getSession();
		session.removeAttribute("declare_user");
		return new JsonResult<>(JsonResult.SUCCESS,"登出成功");
	}
	
	/**
	 * 获取所有省份信息
	 * @return
	 */
	@RequestMapping("getAllProvince")
	public JsonResult<Object> getAllProvince(HttpServletRequest req){
		
		String ipAddr = IPvalidateUtil.getIpAddr(req).split(",")[0];// 访问的ip
		logger.info("IP为：【"+ipAddr+"】获取了省份列表");
		
		return loginService.getAllProvince();
	}
}
