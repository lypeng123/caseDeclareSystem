package com.bonc.caseDeclare.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bonc.caseDeclare.service.CaseSelectService;
import com.bonc.util.JsonResult;

/**
 *
 *	@author haixia.shi
 *	@date 2017年9月21日
 *
 */
@RestController
@RequestMapping("/caseSelect")
public class CaseSelectController {

	private static final Logger logger = LoggerFactory.getLogger(CaseSelectController.class);
	
	@Autowired
	private CaseSelectService caseSelectService;
	
	/**
	 * 下载已上传的文档
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("/downLoadWord")
	public JsonResult<Object> downLoadWord(HttpServletResponse response, HttpServletRequest request) {
//		String type = request.getParameter("file_type");
//		if(type == null || type.equals("")){
//			return new JsonResult<Object>(JsonResult.ERROR,"参数错误");
//		}
		Object result = "";
		try {
				result = caseSelectService.downLoadWord(response, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JsonResult<Object>(result);
	}
	
	@RequestMapping("/selectFirstInfo")
	public JsonResult<Object> selectFirstInfo(HttpServletResponse response, HttpServletRequest request){

		//new JsonResult<>(JsonResult.SUCCESS,"ok",);
		return caseSelectService.selectFirstInfo(request);
	}
}
