package com.bonc.caseDeclare.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.caseDeclare.mapper.LoginMapper;
import com.bonc.caseDeclare.mapper.SafeSettingsMapper;
import com.bonc.caseDeclare.service.SafeSettingsService;
import com.bonc.util.JsonResult;
import com.bonc.util.Md5Util;

/**
*
* @author zhijie.ma
* @date 2017年9月20日
* 
*/
@Service
public class SafeSettingsServiceImpl implements SafeSettingsService {

	private static final Logger logger = LoggerFactory.getLogger(SafeSettingsServiceImpl.class);
	
	@Autowired
	private LoginMapper loginMapper;
	
	@Autowired
	private SafeSettingsMapper safeSettingsMapper;
	
	@Override
	public JsonResult<Object> editUserPassword(HttpServletRequest req) {
		String oldPassword = (String)req.getParameter("oldPassword");
		String newPassword = (String)req.getParameter("newPassword");
		
		if(oldPassword == null && oldPassword.equals("") || newPassword == null && newPassword.equals("")) {
			logger.info("修改密码参数错误");
			return new JsonResult<>("参数错误");
		}
		
		Map<String, Object> map = (Map<String, Object>) req.getSession().getAttribute("declare_user");
		String user_name = (String) map.get("user_name");
		if(user_name == null && user_name.equals("")) {
			logger.info("修改密码没有获取到用户信息，管理员请排查原因！");
			return new JsonResult<>("修改密码失败，请联系管理员");
		}
		
		Map<String,Object> hashMap= new HashMap<String,Object>();
		hashMap.put("user_name", user_name);
		hashMap.put("password", Md5Util.encoding(oldPassword));
		
		List<Map<String,Object>> selectUser = loginMapper.selectUser(hashMap);
		if(selectUser == null || selectUser.isEmpty()) {
			logger.info("修改密码:原始密码不正确");
			return new JsonResult<>("原始密码不正确！");
		}
		
		hashMap.put("password", Md5Util.encoding(newPassword));
		boolean updateUserPassword = safeSettingsMapper.updateUserPassword(hashMap);
		
		if(!updateUserPassword) {
			logger.info("密码修改失败！原因未知");
			return new JsonResult<>("密码修改失败！");
		}
		
		HttpSession session = req.getSession();
		session.removeAttribute("declare_user");
		
		return new JsonResult<>(JsonResult.SUCCESS, "密码修改成功！");
	}


}
