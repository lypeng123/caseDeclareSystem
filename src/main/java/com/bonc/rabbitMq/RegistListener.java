package com.bonc.rabbitMq;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bonc.caseDeclare.mapper.LoginMapper;
import com.bonc.util.JsonResult;
import com.bonc.util.SendEmailUtils;

/**
 * 注册模块队列监听器
 * 
 * @author zhijie.ma
 * @date 2017年6月2日
 * 
 */
@Component
public class RegistListener {

	private final static Logger logger = LoggerFactory.getLogger(RegistListener.class);

	@Autowired
	private LoginMapper loginMapper;

	/**
	 * 监听注册邮件信息
	 * 
	 * @param map
	 */
//	@RabbitListener(queues = "declare_mzj_sendRegistEmail")
//	public void addMessage(Map<String, Object> map) {
//		String declare_person = (String) map.get("declare_person");
//		String declare_mail = (String) map.get("declare_mail");
//		String user_name = (String) map.get("user_name");
//		String password = (String) map.get("password");
//		SendEmailUtils.sendEmailConfigure(declare_person, user_name, declare_mail, password);
//	}
//
//	/**
//	 * 监听注册邮件用户信息
//	 * 
//	 * @param map
//	 */
//	@RabbitListener(queues = "declare_mzj_registEmailUser")
//	public void registEmail(Map<String, Object> map) {
//		boolean insertUser = loginMapper.insertUser(map);
//		logger.info((String) map.get("user_name") + " >> 注册邮件用户信息结果:" + insertUser);
//	}
//
//	/**
//	 * 监听注册邮件信息
//	 * 
//	 * @param map
//	 */
//	@RabbitListener(queues = "declare_mzj_registUser")
//	public void registUser(Map<String, Object> map) {
//		boolean updateUser = loginMapper.updateUser(map);
//		logger.info((String) map.get("user_name") + " >> 注册用户信息结果:" + updateUser);
//	}

	@RabbitListener(queues = "sendEmailScreenResult")
	public void sendEmailScreenResult(Map<String, Object> map) {
		List<Map<String, Object>> selectUser = loginMapper.selectUser(map);
		String audit_state = (String) map.get("audit_state");
		
		if (selectUser == null || selectUser.isEmpty()) {
			logger.info("用户id有误");
		}

		if (audit_state.equals("-2")) {

		} else if (audit_state.equals("-1")) {

		} else if (audit_state.equals("1")) {

		} else if (audit_state.equals("2")) {

		} else if (audit_state.equals("3")) {

		}
		Map<String, Object> map2 = selectUser.get(0);
		// 邮箱
		String declare_mail = (String) map2.get("user_name");
		logger.info(declare_mail + "案例筛选邮件已发出：【假性邮件】");

	}
}
