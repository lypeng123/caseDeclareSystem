package com.bonc.rabbitMq;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bonc.caseDeclare.mapper.DeclareInfoMapper;
import com.bonc.caseDeclare.mapper.UploadMapper;
import com.bonc.caseDeclare.service.DeclareInfoService;
import com.bonc.util.JsonResult;

/**
 *
 * @author haixia.shi
 * @date 2017年9月18日
 *
 */
//@Component
public class DeclareListener {

	private final static Logger logger = LoggerFactory.getLogger(DeclareListener.class);

	@Autowired
	private DeclareInfoService declareInfoService;

	@Autowired
	private DeclareInfoMapper declareInfoMapper;

	@Autowired
	private UploadMapper uploadMapper;

	/**
	 * 新增申报内容
	 * 
	 * @param list
	 */
//	@RabbitListener(queues = "declare_shx_insertDeclareInfo")
	public void insertDeclareInfo(List<Object> list) {
		String declare_id = (String) list.get(0);
		String user_id = (String) list.get(1);
		String property_id = (String) list.get(2);
		String business_data = (String) list.get(3);
		String honor_data = (String) list.get(4);
		Map<String, Object> map = (Map<String, Object>) list.get(5);
		Map<String, Object> map2 = (Map<String, Object>) list.get(6);
		JSONObject parseObject = (JSONObject) list.get(7);
		JSONObject parseObject2 = (JSONObject) list.get(8);

		declareInfoService.updateUserInfo(map);
		declareInfoService.insertDeclareInfo(map2);
		declareInfoService.insertUserDeclareRel(user_id, declare_id);
		if (property_id == null || property_id.equals("")) {
			declareInfoMapper.deleteProperty(declare_id);
		} else {
			declareInfoService.insertDeclarePropertyRel(property_id, declare_id);
		}

		// 修改主营业务
		if (business_data != null && !business_data.equals("")) {
			List<Map<String, Object>> object = (List<Map<String, Object>>) parseObject.get("data");
			if (object != null && object.size() > 0) {
				for (Map<String, Object> map3 : object) {
					String business_id = (String) map3.get("business_id");
					declareInfoService.insertDeclareBusinessRel(business_id, declare_id);
				}
			}
		}

		// 修改相关荣誉
		if (honor_data != null && !honor_data.equals("")) {
			List<Map<String, Object>> object2 = (List<Map<String, Object>>) parseObject2.get("data");
			if (object2 != null && object2.size() > 0) {
				for (Map<String, Object> map4 : object2) {
					String honor_id = (String) map4.get("honor_id");
					String honor_level = (String) map4.get("honor_level");
					String get_year = (String) map4.get("get_year");
					declareInfoService.insertDeclareHonorRel(honor_id, honor_level, get_year, declare_id);
				}
			}
		}
	}

	/**
	 * 修改申报内容
	 * 
	 * @param list
	 */
//	@RabbitListener(queues = "declare_shx_updateDeclareInfo")
	public void updateDeclareInfo(List<Object> list) {
		String declare_id = (String) list.get(0);
		String property_id = (String) list.get(2);
		String business_data = (String) list.get(3);
		String honor_data = (String) list.get(4);
		Map<String, Object> map = (Map<String, Object>) list.get(5);
		Map<String, Object> map2 = (Map<String, Object>) list.get(6);
		JSONObject parseObject = (JSONObject) list.get(7);
		JSONObject parseObject2 = (JSONObject) list.get(8);

		declareInfoService.updateUserInfo(map);
		declareInfoService.updateDeclareInfo(map2);
		if (property_id == null || property_id.equals("")) {
			declareInfoMapper.deleteProperty(declare_id);
		} else {
			declareInfoService.updateDeclarePropertyRel(property_id, declare_id);
		}
		declareInfoMapper.deletebusiness(declare_id);
		// 修改主营业务
		if (business_data != null && !business_data.equals("")) {
			List<Map<String, Object>> object = (List<Map<String, Object>>) parseObject.get("data");
			if (object != null && object.size() > 0) {
				for (Map<String, Object> map3 : object) {
					String business_id = (String) map3.get("business_id");
					declareInfoService.insertDeclareBusinessRel(business_id, declare_id);
				}
			}
		}

		// 修改相关荣誉
		if (honor_data != null && !honor_data.equals("")) {
			declareInfoMapper.deleteHonor(declare_id);
			List<Map<String, Object>> object2 = (List<Map<String, Object>>) parseObject2.get("data");
			if (object2 != null && object2.size() > 0) {
				for (Map<String, Object> map4 : object2) {
					String honor_id = (String) map4.get("honor_id");
					String honor_level = (String) map4.get("honor_level");
					String get_year = (String) map4.get("get_year");
					declareInfoService.insertDeclareHonorRel(honor_id, honor_level, get_year, declare_id);
				}
			}
		}
	}

	/**
	 * 上传产品文件
	 * 
	 * @param map
	 */
//	@RabbitListener(queues = "declare_shx_upLoadProductWord")
	public void upLoadProductWord(Map<String, Object> map) {
		boolean insertUploadProductInfo = uploadMapper.insertUploadProductInfo(map);
		logger.info("添加上传信息结果：" + insertUploadProductInfo);
	}
	
	/**
	 * 上传方案文件
	 * 
	 * @param map
	 */
//	@RabbitListener(queues = "declare_shx_upLoadPlanWord")
	public void upLoadPlanWord(Map<String, Object> map) {
		boolean insertUploadPlanInfo = uploadMapper.insertUploadPlanInfo(map);
		logger.info("添加上传信息结果：" + insertUploadPlanInfo);
	}
	
	/**
	 * 删除产品文件
	 * 
	 * @param map
	 */
//	@RabbitListener(queues = "declare_shx_deleteProductFile")
	public void deleteProductWord(Map<String, Object> map) {
		String product_id = (String) map.get("product_id");
		boolean deleteProductWord = uploadMapper.deleteProductWord(product_id);
		logger.info("删除产品信息结果：" + deleteProductWord);
	}
	
	/**
	 * 删除方案文件
	 * 
	 * @param map
	 */
//	@RabbitListener(queues = "declare_shx_deletePlanFile")
	public void deletePlanWord(Map<String, Object> map) {
		String plan_id = (String) map.get("plan_id");
		boolean deleteProductWord = uploadMapper.deletePlanWord(plan_id);
		logger.info("删除方案信息结果：" + deleteProductWord);
	}
}
