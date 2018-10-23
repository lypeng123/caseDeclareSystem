package com.bonc.caseDeclare.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.caseDeclare.service.DeclareInfoService;
import com.bonc.util.JsonResult;

/**
 * 企业申报模块
 * 
 * @author haixia.shi
 * @date 2017年9月9日
 *
 */
@RestController
@RequestMapping("/declareInfo")
public class DeclareInfoController {
	
	private static final Logger logger = LoggerFactory.getLogger(DeclareInfoController.class);

	@Autowired
	private DeclareInfoService declareInfoService;

	/**
	 * 获取申报ID(暂时不用)
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/declareId")
	public JsonResult<Object> declareId(HttpServletRequest request, HttpServletResponse response) {
		String user_id = request.getParameter("user_id");

		if (user_id == null || user_id.equals("")) {
			logger.info("获取申报ID数据：参数有误");
			return new JsonResult<>("参数有误！");
		}

		Object declareId = declareInfoService.declareId(user_id);

		return new JsonResult<>(declareId);
	}

	/**
	 * 获取所有的企业性质数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/propertyDatas")
	public JsonResult<Object> propertyDatas(HttpServletRequest request, HttpServletResponse response) {
		
		Object propertyDatas = declareInfoService.getProperty();

		return new JsonResult<>(propertyDatas);
	}

	/**
	 * 获取所有的主营业务数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/businessDatas")
	public JsonResult<Object> businessDatas(HttpServletRequest request, HttpServletResponse response) {

		Object businessDatas = declareInfoService.getBusiness();

		return new JsonResult<>(businessDatas);
	}

	/**
	 * 获取所有的相关荣誉数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/honorDatas")
	public JsonResult<Object> honorDatas(HttpServletRequest request, HttpServletResponse response) {

		Object honorDatas = declareInfoService.getHonor();

		return new JsonResult<>(honorDatas);
	}

	/**
	 * 获取关联的主营业务数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/businessInfoDatas")
	public JsonResult<Object> businessInfoDatas(HttpServletRequest request, HttpServletResponse response) {
		String user_id = request.getParameter("user_id");

		return declareInfoService.getBusinessInfo(user_id);

	}

	/**
	 * 获取关联的荣誉数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/honorInfoDatas")
	public JsonResult<Object> honorInfoDatas(HttpServletRequest request, HttpServletResponse response) {
		String user_id = request.getParameter("user_id");

		return declareInfoService.getHonorInfo(user_id);
	}

	/**
	 * 申报信息时获取所有数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/allDatas")
	public JsonResult<Object> allDatas(HttpServletRequest request, HttpServletResponse response) {

		String user_id = request.getParameter("user_id");

		if (user_id == null || user_id.equals("")) {
			logger.info("获取所有数据：参数有误");
			return new JsonResult<>("参数有误！");
		}

		Object allDatas = declareInfoService.allDatas(user_id);

		return new JsonResult<>(allDatas);
	}

	/**
	 * 保存按钮的操作
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/opeationDatas")
	//@Async("mqExecutor")
	public JsonResult<Object> opeationDatas(HttpServletRequest request, HttpServletResponse response) {
		return declareInfoService.updateAllDatas(request);

	}

}
