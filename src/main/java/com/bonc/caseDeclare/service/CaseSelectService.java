package com.bonc.caseDeclare.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.bonc.util.JsonResult;

/**
 *  筛选查询模块
 *	@author haixia.shi
 *	@date 2017年9月22日
 *
 */
@Service
public interface CaseSelectService {

	/**
	 * 下载已上传的产品文档
	 * @param response
	 * @param request
	 * @return
	 */
	public String downLoadWord(HttpServletResponse response, HttpServletRequest request);
	
	/**
	 * 案例查询-初筛
	 * @param request
	 * @return
	 */
	public JsonResult<Object> selectFirstInfo(HttpServletRequest request);
	
}
