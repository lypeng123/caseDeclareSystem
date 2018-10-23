package com.bonc.caseDeclare.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.bonc.util.PageBeanMysql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.bonc.caseDeclare.mapper.DeclareInfoMapper;
import com.bonc.caseDeclare.service.DeclareInfoService;
import com.bonc.util.JsonResult;

/**
 * 企业申报-申报信息模块
 * 
 * @author haixia.shi
 * @date 2017年9月9日
 *
 */
@Service
public class DeclareInfoServiceImpl implements DeclareInfoService {

	private static final Logger logger = LoggerFactory.getLogger(DeclareInfoServiceImpl.class);

	@Autowired
	private DeclareInfoService declareInfoService;

	@Autowired
	private DeclareInfoMapper declareInfoMapper;

	@Override
	public Object declareId(String user_id) {
		String declareId = declareInfoMapper.declareId(user_id);

		logger.info("申报ID返回结果：" + declareId);

		return declareId;
	}
	
	@Override
	public Object getProperty() {
		List<Map<String, Object>> propertyDatas = declareInfoMapper.getProperty();

		logger.info("企业性质返回结果：" + propertyDatas);

		return propertyDatas;
	}

	@Override
	public Object getBusiness() {
		List<Map<String, Object>> businessDatas = declareInfoMapper.getbusiness();

		logger.info("主营业务返回结果：" + businessDatas);

		return businessDatas;
	}

	@Override
	public Object getHonor() {
		List<Map<String, Object>> honorDatas = declareInfoMapper.getHonor();

		logger.info("相关荣誉返回结果：" + honorDatas);

		return honorDatas;
	}

	@Override
	public JsonResult<Object> getBusinessInfo(String user_id) {
		if (user_id == null || user_id.equals("")) {
			logger.info("获取主营业务数据：参数有误");
			return new JsonResult<>("参数有误！");
		}

		String declare_id = declareInfoMapper.declareId(user_id);
//		if (declare_id == null || declare_id.equals("")) {
//			return new JsonResult<>("未进行申报！");
//		}

		List<Map<String, Object>> businessDatas = declareInfoMapper.getbusinessInfo(declare_id);

		logger.info("关联的主营业务数据返回结果：" + businessDatas);

		return new JsonResult<>(businessDatas);
	}

	@Override
	public JsonResult<Object> getHonorInfo(String user_id) {
		if (user_id == null || user_id.equals("")) {
			logger.info("获取相关荣誉数据：参数有误");
			return new JsonResult<>("参数有误！");
		}

		String declare_id = declareInfoMapper.declareId(user_id);
//		if (declare_id == null || declare_id.equals("")) {
//			return new JsonResult<>("未进行申报！");
//		}

		List<Map<String, Object>> honorDatas = declareInfoMapper.getHonorInfo(declare_id);

		logger.info("关联的荣誉返回结果：" + honorDatas);

		return new JsonResult<>(honorDatas);
	}

	@Override
	public Object allDatas(String user_id) {
		String declare_id = declareInfoMapper.declareId(user_id);
		Object userData = "";
		if (declare_id == null || declare_id.equals("")) {
			userData = declareInfoMapper.getUserInfo(user_id);
			logger.info("申报页返回信息1：" + userData);
			return userData;
		} else {
			String property_id = declareInfoMapper.selectPropertyId(declare_id);
			userData = declareInfoMapper.getUserInfo2(user_id,property_id);
			logger.info("申报页返回信息2：" + userData);
			return userData;
		}
	}

	@Override
	@Transactional
	
	public Object updateUserInfo(Map<String, Object> map) {
		boolean updateUser = declareInfoMapper.updateUserInfo(map);
		
		logger.info("修改用户信息返回结果：" + updateUser);

		return updateUser;
	}

	@Override
	@Transactional
	
	public Object insertDeclareInfo(Map<String, Object> map) {
		boolean inserDeclare = declareInfoMapper.insertDeclareInfo(map);

		logger.info("新增申报信息返回结果：" + inserDeclare);

		return inserDeclare;
	}

	@Override
	@Transactional
	
	public Object insertUserDeclareRel(String user_id, String declare_id) {
		boolean insertUserDeclareRel = declareInfoMapper.insertUserDeclareRel(user_id, declare_id);

		logger.info("新增申报_用户关系返回结果：" + insertUserDeclareRel);

		return insertUserDeclareRel;
	}

	@Override
	@Transactional
	
	public Object insertDeclarePropertyRel(String property_id, String declare_id) {
		boolean insertDeclarePropertyRel = declareInfoMapper.insertDeclarePropertyRel(property_id, declare_id);

		logger.info("新增申报_性质关系返回结果：" + insertDeclarePropertyRel);

		return insertDeclarePropertyRel;
	}

	@Override
	@Transactional
	
	public Object insertDeclareBusinessRel(String business_id, String declare_id) {
		boolean insertDeclareBusinessRel = declareInfoMapper.insertDeclareBusinessRel(business_id, declare_id);

		logger.info("新增申报_主营业务关系返回结果：" + insertDeclareBusinessRel);

		return insertDeclareBusinessRel;
	}

	@Override
	@Transactional
	
	public Object insertDeclareHonorRel(String honor_id, String honor_level, String get_year, String declare_id) {
		boolean insertDeclareHonorRel = declareInfoMapper.insertDeclareHonorRel(honor_id, honor_level, get_year,
				declare_id);

		logger.info("新增申报_荣誉关系返回结果：" + insertDeclareHonorRel);

		return insertDeclareHonorRel;
	}

	@Override
	@Transactional
	
	public Object updateDeclareInfo(Map<String, Object> map) {
		boolean updateDeclareInfo = declareInfoMapper.updateDeclareInfo(map);

		logger.info("修改申报信息返回结果：" + updateDeclareInfo);

		return updateDeclareInfo;
	}

	@Override
	@Transactional
	
	public Object updateDeclarePropertyRel(String property_id, String declare_id) {
		String old_property = declareInfoMapper.selectPropertyId(declare_id);
		boolean updateDeclarePropertyRel = false;
		if(old_property == null || declareInfoMapper.equals("")){
			updateDeclarePropertyRel = declareInfoMapper.insertDeclarePropertyRel(property_id, declare_id);
		}else{
			updateDeclarePropertyRel = declareInfoMapper.updateDeclarePropertyRel(property_id, declare_id);
		}

		logger.info("修改申报_性质关系返回结果：" + updateDeclarePropertyRel);

		return updateDeclarePropertyRel;
	}

	@Override
	@Transactional
	public JsonResult<Object> updateAllDatas(HttpServletRequest request) {
		String user_id = request.getParameter("user_id");

		String company_name = request.getParameter("company_name");
		String declare_person = request.getParameter("declare_person");
		String declare_mail = request.getParameter("declare_mail");
		String declare_phone = request.getParameter("declare_phone");
		String responsible_person = request.getParameter("responsible_person");
		String responsible_phone = request.getParameter("responsible_phone");
		String responsible_mail = request.getParameter("responsible_mail");
		String corporate_jurisdical_person = request.getParameter("corporate_jurisdical_person");
		String company_register_address = request.getParameter("company_register_address");
		String company_office_address = request.getParameter("company_office_address");
		String register_capital = request.getParameter("register_capital");

		String property_describe = request.getParameter("property_describe");
		String business_describe = request.getParameter("business_describe");
		String quoted_state = request.getParameter("quoted_state");
		String quoted_time = request.getParameter("quoted_time");
		String quoted_address = request.getParameter("quoted_address");
		String shares_code = request.getParameter("shares_code");
		String export_state = request.getParameter("export_state");
		String export_address = request.getParameter("export_address");
		String honor_describe = request.getParameter("honor_describe");
		String development_ability = request.getParameter("development_ability");
		String business_income = request.getParameter("business_income");
		String development_investment = request.getParameter("development_investment");
		String tax_nums = request.getParameter("tax_nums");
		String profit = request.getParameter("profit");
		String company_person_nums = request.getParameter("company_person_nums");
		String development_person_nums = request.getParameter("development_person_nums");

		String product_incom = request.getParameter("product_incom");
		String information_income = request.getParameter("information_income");
		String software_income = request.getParameter("software_income");
		String data_income = request.getParameter("data_income");

		String property_id = request.getParameter("property_id");
		String business_data = request.getParameter("business_data");
		String honor_data = request.getParameter("honor_data");
		
		if (user_id == null || user_id.equals("") || company_name == null || company_name.equals("")
				|| declare_person == null || declare_person.equals("") || declare_mail == null
				|| declare_mail.equals("") || declare_phone == null || declare_phone.equals("")
				|| responsible_person == null || responsible_person.equals("") || responsible_phone == null
				|| responsible_phone.equals("") || responsible_mail == null || responsible_mail.equals("")
				|| corporate_jurisdical_person == null || corporate_jurisdical_person.equals("")
				|| company_office_address == null || company_office_address.equals("")
				|| company_register_address == null || company_register_address.equals("") || register_capital == null
				|| register_capital.equals("") || quoted_state == null || quoted_state.equals("")
				|| export_state == null || export_state.equals("") || business_income == null
				|| business_income.equals("") || development_investment == null || development_investment.equals("")
				|| tax_nums == null || tax_nums.equals("") || profit == null || profit.equals("")
				|| company_person_nums == null || company_person_nums.equals("") || development_person_nums == null
				|| development_person_nums.equals("") || business_data == null || business_data.equals("")
				|| data_income == null || data_income.equals("")) {
			logger.info("修改数据：参数有误");
			return new JsonResult<>("参数有误！");
		}
		if ((property_id == null || property_id.equals(""))
				&& (property_describe == null || property_describe.equals(""))) {
			logger.info("修改数据：公司性质未获取到");
			return new JsonResult<>("公司性质未获取到！");
		}
		if(
				declare_person.length()>16 ||
				declare_phone.length()>16 ||
				responsible_person.length()>16 ||
				responsible_phone.length()>16 ||
				corporate_jurisdical_person.length()>16){
			logger.info("修改数据：数据长度太长,超过16字");
			return new JsonResult<>("输入内容超出规定长度，限制16字以内！");
		}
		if(
				company_name.length()>33 ||
				declare_mail.length()>33 ||
				responsible_mail.length()>33 ||
				shares_code.length()>33){
			logger.info("修改数据：数据长度太长,超过33字");
			return new JsonResult<>("输入内容超出规定长度，限制33字以内！");
		}
		if(
				company_register_address.length()>100 ||
				company_office_address.length()>100 ||
				quoted_address.length()>100 ||
				export_address.length()>100){
			logger.info("修改数据：数据长度太长,超过100字");
			return new JsonResult<>("输入内容超出规定长度，限制100字以内！");
		}
		if(
				property_describe.length()>333 ||
				business_describe.length()>333 ||
				honor_describe.length()>333){
			logger.info("修改数据：数据长度太长,超过333字");
			return new JsonResult<>("输入内容超出规定长度，限制333字以内！");
		}
		if(
				development_ability.length()>1000){
			logger.info("修改数据：数据长度太长,超过1000字");
			return new JsonResult<>("输入内容超出规定长度，限制1000字以内！");
		}

		Double register_capital2;
		Double business_income2;
		Double development_investment2;
		Double tax_nums2;
		Double profit2;
		Integer company_person_nums2;
		Integer development_person_nums2;

		Double product_incom2 = null;
		Double information_income2 = null;
		Double software_income2 = null;
		Double data_income2;

		// 将字符串转换为数字
		try {
			register_capital2 = Double.parseDouble(register_capital);
			business_income2 = Double.parseDouble(business_income);
			development_investment2 = Double.parseDouble(development_investment);
			profit2 = Double.parseDouble(profit);
			tax_nums2 = Double.parseDouble(tax_nums);
			company_person_nums2 = Integer.parseInt(company_person_nums);
			development_person_nums2 = Integer.parseInt(development_person_nums);
			data_income2 = Double.parseDouble(data_income);
			if (product_incom != null && !product_incom.equals("") && !information_income.equals("")
					&& !information_income.equals("") && !software_income.equals("") && !software_income.equals("")) {
				product_incom2 = Double.parseDouble(product_incom);
				information_income2 = Double.parseDouble(information_income);
				software_income2 = Double.parseDouble(software_income);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			logger.info("修改数据：存在不合法数据参数");
			return new JsonResult<>("存在不合法数据参数！");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();

		map.put("user_id", user_id);
		map.put("company_name", company_name);
		map.put("declare_person", declare_person);
		map.put("declare_mail", declare_mail);
		map.put("declare_phone", declare_phone);
		map.put("responsible_person", responsible_person);
		map.put("responsible_phone", responsible_phone);
		map.put("responsible_mail", responsible_mail);
		map.put("corporate_jurisdical_person", corporate_jurisdical_person);
		map.put("company_register_address", company_register_address);
		map.put("company_office_address", company_office_address);
		map.put("register_capital", register_capital2);

		declareInfoService.updateUserInfo(map);

		String declare_id = declareInfoMapper.declareId(user_id);
		
		if(quoted_state.equals("1")){
			if(quoted_time == null || quoted_time.equals("") 
					|| quoted_address == null || quoted_address.equals("")
					|| shares_code == null || shares_code.equals("")){
				logger.info("修改数据：上市信息不全");
				return new JsonResult<>("上市信息不全！");
			}
		}
		
		if(export_state.equals("1")){
			if(export_address == null || export_address.equals("")){
				logger.info("获取主营业务数据：出口信息不全");
				return new JsonResult<>("出口信息不全！");
			}
		}

		map2.put("property_describe", property_describe);
		map2.put("business_describe", business_describe);
		map2.put("quoted_state", quoted_state);
		map2.put("quoted_time", quoted_time);
		map2.put("quoted_address", quoted_address);
		map2.put("shares_code", shares_code);
		map2.put("export_state", export_state);
		map2.put("export_address", export_address);
		map2.put("honor_describe", honor_describe);
		map2.put("development_ability", development_ability);
		map2.put("business_income", business_income2);
		map2.put("development_investment", development_investment2);
		map2.put("tax_nums", tax_nums2);
		map2.put("profit", profit2);
		map2.put("company_person_nums", company_person_nums2);
		map2.put("development_person_nums", development_person_nums2);
		map2.put("product_incom", product_incom2);
		map2.put("information_income", information_income2);
		map2.put("software_income", software_income2);
		map2.put("data_income", data_income2);

		// 如果申报ID为空就调插入接口，否则就修改
		if (declare_id == null || declare_id.equals("")) {
			// 生成新的申报ID
			declare_id = UUID.randomUUID().toString();
			map2.put("declare_id", declare_id);
			declareInfoService.insertDeclareInfo(map2);
			declareInfoService.insertUserDeclareRel(user_id, declare_id);
			if (property_id == null || property_id.equals("")) {
				declareInfoMapper.deleteProperty(declare_id);
			} else {
				declareInfoService.insertDeclarePropertyRel(property_id, declare_id);
			}

			// 修改主营业务
			JSONObject parseObject = null;
			try {
				parseObject = JSONObject.parseObject(business_data);
			} catch (Exception e) {
				logger.info("修改数据：主营业务数据格式有误");
				return new JsonResult<>("主营业务数据格式有误");
			}
			List<Map<String, Object>> object = (List<Map<String, Object>>) parseObject.get("data");
			if (object != null && object.size() > 0) {
				for (Map<String, Object> map3 : object) {
					String business_id = (String) map3.get("business_id");
					declareInfoService.insertDeclareBusinessRel(business_id, declare_id);
				}
			}

			// 修改相关荣誉
			JSONObject parseObject2 = null;
			if (honor_data != null && !honor_data.equals("")) {
				try {
					parseObject2 = JSONObject.parseObject(honor_data);
				} catch (Exception e) {
					logger.info("修改数据：相关荣誉数据格式有误");
					return new JsonResult<>("相关荣誉数据格式有误");
				}
				List<Map<String, Object>> object2 = (List<Map<String, Object>>) parseObject2.get("data");
				if (object2 != null && object2.size() > 0) {
					for (Map<String, Object> map3 : object2) {
						String honor_id = (String) map3.get("honor_id");
						String honor_level = (String) map3.get("honor_level");
						String get_year = (String) map3.get("get_year");
						try{
							Integer yesr_change = Integer.parseInt(get_year);
						}catch(Exception e){
							logger.info("修改数据：相关荣誉数据中年份格式有误");
							return new JsonResult<>("相关荣誉数据中年份格式有误");
						}
						declareInfoService.insertDeclareHonorRel(honor_id, honor_level, get_year, declare_id);
					}
				}
			}
		} else {
			map2.put("declare_id", declare_id);
			declareInfoService.updateDeclareInfo(map2);
			if (property_id == null || property_id.equals("")) {
				declareInfoMapper.deleteProperty(declare_id);
			} else {
				declareInfoService.updateDeclarePropertyRel(property_id, declare_id);
			}
			declareInfoMapper.deletebusiness(declare_id);

			// 修改主营业务
			JSONObject parseObject = null;
			try {
				parseObject = JSONObject.parseObject(business_data);
			} catch (Exception e) {
				logger.info("修改数据：主营业务数据格式有误");
				return new JsonResult<>("主营业务数据格式有误");
			}
			List<Map<String, Object>> object = (List<Map<String, Object>>) parseObject.get("data");
			if (object != null && object.size() > 0) {
				for (Map<String, Object> map3 : object) {
					String business_id = (String) map3.get("business_id");
					declareInfoService.insertDeclareBusinessRel(business_id, declare_id);
				}
			}

			// 修改相关荣誉
			JSONObject parseObject2 = null;
			if (honor_data != null && !honor_data.equals("")) {
				declareInfoMapper.deleteHonor(declare_id);
				try {
					parseObject2 = JSONObject.parseObject(honor_data);
				} catch (Exception e) {
					logger.info("修改数据：相关荣誉数据格式有误");
					return new JsonResult<>("相关荣誉数据格式有误");
				}
				List<Map<String, Object>> object2 = (List<Map<String, Object>>) parseObject2.get("data");
				if (object2 != null && object2.size() > 0) {
					for (Map<String, Object> map3 : object2) {
						String honor_id = (String) map3.get("honor_id");
						String honor_level = (String) map3.get("honor_level");
						String get_year = (String) map3.get("get_year");
						try{
							Integer yesr_change = Integer.parseInt(get_year);
						}catch(Exception e){
							logger.info("修改数据：相关荣誉数据中年份格式有误");
							return new JsonResult<>("相关荣誉数据中年份格式有误");
						}
						declareInfoService.insertDeclareHonorRel(honor_id, honor_level, get_year, declare_id);
					}
				}
			}

		}
		Object userData = declareInfoMapper.getUserInfo2(user_id,property_id);
		//logger.info("所有结果"+userData);
		return new JsonResult<>(JsonResult.SUCCESS, "ok",userData);
	}

}
