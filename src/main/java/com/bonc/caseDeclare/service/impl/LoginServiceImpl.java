package com.bonc.caseDeclare.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.bonc.caseDeclare.mapper.LoginMapper;
import com.bonc.caseDeclare.service.LoginService;
import com.bonc.util.GeneratorPassword;
import com.bonc.util.JsonResult;
import com.bonc.util.Md5Util;
import com.bonc.util.SendEmailUtils;

/**
 * 提供注册登陆服务
 * 
 * @author zhijie.ma
 * @date 2017年9月8日
 *
 */
@Service
public class LoginServiceImpl implements LoginService {

	private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Autowired
	private LoginMapper loginMapper;

	@Override
	@Transactional
	// @Async("mqExecutor")
	public JsonResult<Object> registUser(HttpServletRequest req, String IP) {

		String company_name = req.getParameter("company_name");
		String responsible_person = req.getParameter("responsible_person");
		String company_register_address = req.getParameter("company_register_address");
		String area_id = req.getParameter("area_id");
		String declare_phone = req.getParameter("declare_phone");
		String declare_mail = req.getParameter("declare_mail");
		String credit_code = req.getParameter("credit_code");
//		String recommend = req.getParameter("recommend");
		String declare_person = req.getParameter("declare_person");
		String password = req.getParameter("password");

		if (company_name == null || company_name == "" || responsible_person == null || responsible_person == ""
				|| company_register_address == null || company_register_address == "" || declare_phone == null
				|| declare_phone == "" || declare_mail == null || declare_mail == "" || credit_code == null
				|| credit_code == "" || password == null || password == ""
//				|| recommend == null || recommend == "" 
				|| declare_person == null
				|| declare_person == "" || area_id == null || area_id == "") {
			logger.info("注册参数错误！");
			return new JsonResult<>("注册参数错误！");
		}

		String randomUUID = UUID.randomUUID().toString();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", randomUUID);
		map.put("company_name", company_name);
		map.put("responsible_person", responsible_person);
		map.put("company_register_address", company_register_address);
		map.put("area_id", area_id);
		map.put("declare_person", declare_person);
		map.put("register_time,", new Date());

		map.put("declare_phone", declare_phone);
		map.put("declare_mail", declare_mail);
		map.put("credit_code", credit_code);
		map.put("state", 1);
		map.put("ip", IP);
		map.put("type", 3);

		List<Map<String, Object>> selectUser = loginMapper.selectUser(map);
		if (!selectUser.isEmpty()) {
			logger.info((String) map.get("declare_mail") + ":该账户已经注册，请勿重复注册");
			return new JsonResult<>("该账户已经注册，请勿重复注册");
		}

		// 根据email添加用户名和密码 QY10001
//		HashMap<String, Object> hashMap = new HashMap<String, Object>();
//		selectUser = loginMapper.selectUser(hashMap);
//		String user_name = null;
//		Integer maxUserCode;
//		if (selectUser.isEmpty()) {
//			user_name = "QY10001";
//		} else {
//			maxUserCode = loginMapper.selectMaxUserCode();
//			user_name = "QY" + maxUserCode;
//		}
//
//		String password = GeneratorPassword.getPassword(6, 2);

		String passwordMd5 = Md5Util.encoding(password);

		map.put("user_name", declare_mail);
		map.put("password", passwordMd5);

		logger.info("用户注册相关信息" + map);

		boolean insertUser = loginMapper.insertUser(map);
		if (!insertUser) {
			logger.info("【未知错误导致注册失败，请尽快解决！！】");
			return new JsonResult<>("注册失败");
		}

//		SendEmailUtils.sendEmailConfigure((String) map.get("declare_person"), user_name,
//				(String) map.get("declare_mail"), password);

		return new JsonResult<>(JsonResult.SUCCESS, "注册成功！");
	}

	@Override
	public JsonResult<Object> loginUser(HttpServletRequest req, String IP) {
		String user_name = req.getParameter("user_name");
		String password = req.getParameter("password");
		if (user_name == null || user_name == "" || password == null || password == "") {
			logger.info("登陆参数错误！");
			return new JsonResult<>("登陆参数错误！");
		}
		password = Md5Util.encoding(password);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_name", user_name);
		map.put("password", password);

		List<Map<String, Object>> selectUser = loginMapper.selectUser(map);
		if (selectUser.isEmpty()) {
			logger.info("用户名或密码错误！");
			return new JsonResult<>("用户名或密码错误！");
		}

		HttpSession session = req.getSession();
		Map<String, Object> userMap = selectUser.get(0);
		String state = (String) userMap.get("state");
//		String type = (String) userMap.get("type");
		List<Map<String,Object>> selectAllowUserMenu = loginMapper.selectAllowUserMenu(userMap);
		
		if (state.equals("0")) {
			logger.info("该账户已被停用");
			return new JsonResult<>("该账户已被停用");
		} else if (state.equals("2")) {
			logger.info("用户未完成注册，请完成注册再进行登陆！");
			return new JsonResult<>("该账户未完成注册");
		}
		
		
		JSONObject jsonObject = new JSONObject();
		JSONObject jsonObject2 = new JSONObject();
		jsonObject2.put("data", selectAllowUserMenu);
		jsonObject.put("nav_data", jsonObject2);
		jsonObject.put("userInfo", userMap);
		
		session.setAttribute("declare_user", userMap);

//		System.out.println(jsonObject);
		return new JsonResult<>(JsonResult.SUCCESS, "登陆成功！", jsonObject);
	}

	@Override
	public JsonResult<Object> registEmail(HttpServletRequest req, String IP) {

		String declare_mail = req.getParameter("declare_mail");
		String declare_person = req.getParameter("declare_person");

		if (declare_mail == null || declare_mail == "" || declare_person == null || declare_person == "") {
			logger.info("注册邮箱参数错误！");
			return new JsonResult<>("注册邮箱参数错误！");
		}

		String randomUUID = UUID.randomUUID().toString();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", randomUUID);
		map.put("register_time", new Date());
		map.put("declare_mail", declare_mail);
		map.put("declare_person", declare_person);
		map.put("state", 2);
		map.put("ip", IP);
		map.put("type", 3);

		List<Map<String, Object>> selectUser = loginMapper.selectUser(map);
		if (!selectUser.isEmpty()) {
			logger.info((String) map.get("declare_mail") + ":该账户已经注册，请勿重复注册");
			return new JsonResult<>("该账户已经注册，请勿重复注册");
		}

		// 根据email添加用户名和密码 QY10001
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		selectUser = loginMapper.selectUser(hashMap);
		String user_name = null;
		Integer maxUserCode;
		if (selectUser.isEmpty()) {
			user_name = "QY10001";
		} else {
			maxUserCode = loginMapper.selectMaxUserCode();
			user_name = "QY" + maxUserCode;
		}

		String password = GeneratorPassword.getPassword(6, 2);

		String passwordMd5 = Md5Util.encoding(password);

		map.put("user_name", user_name);
		map.put("password", passwordMd5);

		logger.info("用户邮箱相关信息" + map);

		boolean insertUser = loginMapper.insertUser(map);
		if (!insertUser) {
			logger.info("【未知错误导致注册失败，请尽快解决！！】");
			return new JsonResult<>("注册失败");
		}

		SendEmailUtils.sendEmailConfigure((String) map.get("declare_person"), user_name,
				(String) map.get("declare_mail"), password);

		return new JsonResult<>(JsonResult.SUCCESS, "邮箱验证成功！");

	}

	@Override
	public JsonResult<Object> getAllProvince() {
		List<Map<String, Object>> allProvince = loginMapper.selectAllProvince();

		if (allProvince.isEmpty()) {
			logger.info("获取省份信息失败，请联系管理员");
			return new JsonResult<>("获取省份信息失败，请联系管理员");
		}
//		logger.info("所有省份信息：" + allProvince);
		return new JsonResult<Object>(allProvince);
	}

}
