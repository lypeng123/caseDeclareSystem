package com.bonc.caseDeclare.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.caseDeclare.service.SafeSettingsService;
import com.bonc.util.IPvalidateUtil;
import com.bonc.util.JsonResult;

/**
*	安全设置模块
* @author zhijie.ma
* @date 2017年9月20日
* 
*/
@RestController
@RequestMapping("safeSettings")
public class SafeSettingsController {

	private static final Logger logger = LoggerFactory.getLogger(SafeSettingsController.class);
	
	@Autowired
	private SafeSettingsService safeSettingsService;
	
	@RequestMapping("editPassword")
	public JsonResult<Object> editUserPassword(HttpServletRequest req){
		String ipAddr = IPvalidateUtil.getIpAddr(req).split(",")[0];// 访问的ip
		logger.info("IP为：【"+ipAddr+"】进行了密码修改");
		
		JsonResult<Object> editUserPassword = safeSettingsService.editUserPassword(req);
		logger.info("修改密码结果："+editUserPassword);
		return editUserPassword;
	}
	
}
