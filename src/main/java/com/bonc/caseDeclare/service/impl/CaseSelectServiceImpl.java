package com.bonc.caseDeclare.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonc.caseDeclare.mapper.CaseSelectMapper;
import com.bonc.caseDeclare.service.CaseSelectService;
import com.bonc.util.JsonResult;
import com.bonc.util.PageBeanMysql;

/**
 *  筛选查询模块
 *	@author haixia.shi
 *	@date 2017年9月22日
 *
 */
@Service
public class CaseSelectServiceImpl implements CaseSelectService{
	
	private static final Logger logger = LoggerFactory.getLogger(CaseSelectServiceImpl.class);

	@Autowired 
	private CaseSelectMapper caseSelectMapper;

	@Override
	public String downLoadWord(HttpServletResponse response, HttpServletRequest request) {
		response.setCharacterEncoding("utf-8");
		// 模板名称
		String filename = request.getParameter("old_file_name");
		// 文件真正地址
		String filepath = request.getParameter("file_path");
		if(filename == null || filename.equals("")
				|| filepath == null || filepath.equals("")){
			logger.info("下载失败");
			return "error";
		}
		try {
			// 调用ExportExcel的文件下载方法
			DownLoadWord.downAttachment(filepath, filename, request, response);
			logger.info("下载成功！");
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("下载失败");
		return "error";
	}

	@Override
	public JsonResult<Object> selectFirstInfo(HttpServletRequest request) {
		String audit_state = request.getParameter("audit_state");
		String file_type = request.getParameter("file_type");
		String area_id = request.getParameter("area_id");
		String property_id = request.getParameter("property_id");
		String quoted_state = request.getParameter("quoted_state");
		String company_name = request.getParameter("company_name");
		String field_id = request.getParameter("field_id"); 
		String business_id = request.getParameter("business_id");
		String pageNum = request.getParameter("pageNum");
		if(pageNum == null || pageNum.equals("")
				|| audit_state == null || audit_state.equals("")){
			logger.info("参数有误");
			return new JsonResult<>("参数有误！");
		}
		
		Integer nums;
		try{
			nums = Integer.parseInt(pageNum);
			if(nums == 0){
				logger.info("页码无效！");
				return new JsonResult<>("页码无效！");
			}
		}catch(NumberFormatException e){
			logger.info("页码无效！");
			return new JsonResult<>("页码无效！");
		}
		
		
		PageBeanMysql<Object> pageBeanMysql = new PageBeanMysql<Object>(nums);
		Integer start = pageBeanMysql.getStart();
		Integer end = pageBeanMysql.getEnd();
		if(start < 0 || end < 0){
			logger.info("页码无效！");
			return new JsonResult<>("页码无效！");
		}
		
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("audit_state", audit_state);
		map.put("fiel_type", file_type);
		map.put("area_id", area_id);
		map.put("property_id", property_id);
		map.put("quoted_state", quoted_state);
		map.put("company_name", company_name);
		map.put("field_id", field_id);
		map.put("business_id", business_id);
		map.put("start", start);
		map.put("end", end);

		List<Map<String,Object>> selectFirstInfo = caseSelectMapper.selectCase(map);
		
		Integer count = caseSelectMapper.selectCount(map);
		
		pageBeanMysql.setPageNum(nums);
		pageBeanMysql.setCount(count);
		pageBeanMysql.setData(selectFirstInfo);
		
		logger.info("查询结果："+ pageBeanMysql);
		return new JsonResult<>(JsonResult.SUCCESS,"ok",pageBeanMysql);
	}


}
