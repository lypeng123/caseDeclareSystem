package com.bonc.caseDeclare.service;

import javax.servlet.http.HttpServletRequest;

import com.bonc.util.JsonResult;

/**
*	安全设置服务
* @author zhijie.ma
* @date 2017年9月20日
* 
*/
public interface SafeSettingsService {
	
	/**
	 * 修改用户密码
	 * @param map
	 * @return
	 */
	public JsonResult<Object> editUserPassword(HttpServletRequest req);
	
}
