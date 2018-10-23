package com.bonc.caseDeclare.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bonc.caseDeclare.mapper.CaseScreenMapper;
import com.bonc.caseDeclare.mapper.LoginMapper;
import com.bonc.caseDeclare.service.CaseScreenService;
import com.bonc.util.JsonResult;
import com.bonc.util.PageBeanMysql;

/**
 * 案例筛选服务
 * 
 * @author zhijie.ma
 * @date 2017年9月22日
 * 
 */
@Service
public class CaseScreenServiceImpl implements CaseScreenService {

	private static final Logger logger = LoggerFactory.getLogger(CaseScreenServiceImpl.class);

	@Autowired
	private CaseScreenMapper caseSreenMapper;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private LoginMapper loginMapper;

	@Override
	public JsonResult<Object> getCaseScreenResult(HttpServletRequest req) {

		String audit_state = req.getParameter("audit_state");
		String file_type = req.getParameter("file_type");
		String area_id = req.getParameter("area_id");
		String average = req.getParameter("average"); // 综合得分
		String first_score = req.getParameter("first_score"); // 第一轮专家评分
		String pageNum = req.getParameter("pageNum");

		if (audit_state == null || audit_state.equals("") || pageNum == null || pageNum.equals("")) {
			logger.info("案例筛选参数错误！");
			return new JsonResult<>("案例筛选参数错误！");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("audit_state", audit_state);
		map.put("average", average);
		map.put("first_score", first_score);
		map.put("file_type", file_type);
		map.put("area_id", area_id);

		PageBeanMysql<Object> pageBeanMysql = null;
		Integer pageNumInt = null;
		try {
			pageNumInt = Integer.parseInt(pageNum);
			
			if(pageNumInt<1) {
				logger.info("页码值为："+pageNumInt+"，请检查页码准确性");
				return new JsonResult<>("有请  梦鸽   检查好页码准确性");
			}
			
			pageBeanMysql = new PageBeanMysql<>(pageNumInt);
			map.put("start", pageBeanMysql.getStart());
			map.put("end", pageBeanMysql.getEnd());
			if (average != null && !average.equals("")) {
				Double.parseDouble(average);
			}
			if (first_score != null && !first_score.equals("")) {
				Double.parseDouble(first_score);
			}

		} catch (NumberFormatException e) {
			logger.info("综合得分筛选条件非数值");
			return new JsonResult<>("综合得分筛选条件非数值");
		}

		logger.info("案例筛选参数：" + map);
		List<Map<String, Object>> selectCaseScreenResult = null;
		if (audit_state.equals("0") || audit_state.equals("1") || audit_state.equals("2") || audit_state.equals("3")) {
			// 案例初筛
			selectCaseScreenResult = caseSreenMapper.selectCasePrescreenResult(map);
			// }
			// else if (audit_state.equals("1")) {
			// // 案例复筛
			// selectCaseScreenResult = caseSreenMapper.selectCaseRetrialResult(map);
			// } else if (audit_state.equals("2") || audit_state.equals("3")) {
			// // 案例终筛或入选清单
			// selectCaseScreenResult = caseSreenMapper.selectCaseFinalResult(map);
		} else {
			logger.info("案例筛选，筛选状态不可用");
			return new JsonResult<>("案例筛选，筛选状态不可用");
		}

		if (selectCaseScreenResult == null || selectCaseScreenResult.isEmpty()) {
			logger.info("案例筛选暂无数据！");
			return new JsonResult<>(JsonResult.SUCCESS, "暂无专家打分或暂无企业申报");
		}

		Integer selectDataCount = caseSreenMapper.selectDataCount(map);

		pageBeanMysql.setCount(selectDataCount);
		pageBeanMysql.setData(selectCaseScreenResult);
		pageBeanMysql.setPageNum(pageNumInt);

		return new JsonResult<>(pageBeanMysql);
	}

	@Override
	public JsonResult<Object> editCaseState(HttpServletRequest req) {
		String screen_param = req.getParameter("screen_param");
		String audit_state = req.getParameter("audit_state");

		if (screen_param == null || screen_param.equals("") || audit_state == null || audit_state.equals("")) {
			logger.info("案例审核参数错误！");
			return new JsonResult<>("案例审核参数错误！");
		}

		JSONObject parseObject = null;
		try {
			parseObject = JSONObject.parseObject(screen_param);
		} catch (Exception e) {
			logger.info("参数解析失败");
			return new JsonResult<>("参数解析失败");
		}

		logger.info("案例审核参数："+parseObject+"。审核状态："+audit_state);
		
		List<Map<String, Object>> file_codeList = (List<Map<String, Object>>) parseObject.get("file_codes");
		List<Map<String, Object>> user_idList = (List<Map<String, Object>>) parseObject.get("user_ids");
		
		if (file_codeList == null || user_idList == null || user_idList.isEmpty() || file_codeList.isEmpty()) {
			logger.info("案例审核参数错误！");
			return new JsonResult<>("案例审核参数错误！");
		}
		
		String file_code = (String)file_codeList.get(0).get("file_code");
		String user_id = (String)user_idList.get(0).get("user_id");
		
		if ((String)file_codeList.get(0).get("file_code") == null || (String)user_idList.get(0).get("user_id") == null 
				|| file_code.equals("") || user_id.equals("")) {
			logger.info("案例审核参数错误！");
			return new JsonResult<>("案例审核参数错误！");
		}
		
		for (Map<String, Object> map : file_codeList) {
			map.put("audit_state", audit_state);
			boolean updateCaseState = caseSreenMapper.updateCaseState(map);
			
			logger.info(map.get("file_code")+">>审核结果："+updateCaseState);
		}
		
		for (Map<String, Object> user_idMap : user_idList) {
			user_idMap.put("audit_state", audit_state);
			
			rabbitTemplate.convertAndSend("sendEmailScreenResult", user_idMap);
		}

		return new JsonResult<>(JsonResult.SUCCESS,"审核成功");
	}

}
