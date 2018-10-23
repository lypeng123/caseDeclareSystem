package com.bonc.caseDeclare.controller;

import com.bonc.caseDeclare.service.CaseScreenService;
import com.bonc.util.IPvalidateUtil;
import com.bonc.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
*	案例筛选模块
* @author zhijie.ma
* @date 2017年9月24日
* 
*/
@RestController
@RequestMapping("/caseScreen")
public class CaseScreenController {

	private static final Logger logger = LoggerFactory.getLogger(CaseSelectController.class);
	
	@Autowired
	private CaseScreenService caseScreenService;
	
	/**
	 * 获取案例筛选结果列表
	 * @param req
	 * @return
	 */
	@RequestMapping("/caseScreenResult")
	public JsonResult<Object> getCaseScreenResult(HttpServletRequest req){
		String ipAddr = IPvalidateUtil.getIpAddr(req).split(",")[0];// 访问的ip
		logger.info("IP为：【"+ipAddr+"】查看了案例筛选");
		
		JsonResult<Object> caseScreenResult = caseScreenService.getCaseScreenResult(req);
		logger.info("案例筛选结果："+caseScreenResult);
		
		return caseScreenResult;
	}
	
	@RequestMapping("editCaseScreen")
	public JsonResult<Object> editCaseScreen(HttpServletRequest req){
		String ipAddr = IPvalidateUtil.getIpAddr(req).split(",")[0];// 访问的ip
		logger.info("IP为：【"+ipAddr+"】对案例进行了审批");
		
		JsonResult<Object> editCaseState = caseScreenService.editCaseState(req);
		logger.info("案例审批结果："+editCaseState);
		
		return editCaseState;
	}

}
