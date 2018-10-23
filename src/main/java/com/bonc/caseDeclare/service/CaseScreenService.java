package com.bonc.caseDeclare.service;

import javax.servlet.http.HttpServletRequest;

import com.bonc.util.JsonResult;

/**
*	案例筛选服务
* @author zhijie.ma
* @date 2017年9月22日
* 
*/
public interface CaseScreenService {

	/**
	 * 获取案例筛选结果
	 * @param req
	 * @return
	 */
	public JsonResult<Object> getCaseScreenResult(HttpServletRequest req);
	
	/**
	 * 进行案例筛选
	 * @param req
	 * @return
	 */
	public JsonResult<Object> editCaseState(HttpServletRequest req);
	
}
