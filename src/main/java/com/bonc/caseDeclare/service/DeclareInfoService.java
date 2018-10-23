package com.bonc.caseDeclare.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.bonc.util.JsonResult;

/**
 *
 * @author haixia.shi
 * @date 2017年9月9日
 *
 */
@Service
public interface DeclareInfoService {

	/**
	 * 获取申报ID
	 * @param user_id	用户ID
	 * @return
	 */
	public Object declareId(String user_id);
	
	/**
	 * 获取所有的企业性质
	 * @return
	 */
	public Object getProperty();
	
	/**
	 * 获取所有的主营业务
	 * @return
	 */
	public Object getBusiness();
	
	/**
	 * 获取所有的荣誉(暂时不用)
	 * @return
	 */
	public Object getHonor();
	
	/**
	 * 获取关联的主营业务数据
	 * @param user_id	用户ID
	 * @return
	 */
	public JsonResult<Object> getBusinessInfo(String user_id);
	
	/**
	 * 获取关联的荣誉数据
	 * @param user_id	用户ID
	 * @return
	 */
	public JsonResult<Object> getHonorInfo(String user_id);
	
	/**
	 * 获取所有申报相关数据
	 * @param user_id	用户ID
	 * @return
	 */
	public Object allDatas(String user_id);

	/**
	 * 用户信息表数据添加
	 * 
	 * @param map
	 *            所有信息表数据
	 * @return
	 */
	
	public Object updateUserInfo(Map<String, Object> map);

	/**
	 * 用户信息表数据添加
	 * 
	 * @param map
	 *            所有信息表数据
	 * @return
	 */
	public Object insertDeclareInfo(Map<String, Object> map);

	/**
	 * 用户_申报关系表数据添加
	 * 
	 * @param user_id
	 *            用户ID
	 * @param declare_id
	 *            申报表ID
	 * @return
	 */
	public Object insertUserDeclareRel(String user_id, String declare_id);

	/**
	 * 申报_单位性质关系表数据添加
	 * 
	 * @param property_id
	 *            单位性质ID
	 * @param declare_id
	 *            申报表ID
	 */
	public Object insertDeclarePropertyRel(String property_id, String declare_id);

	/**
	 * 申报_主营业务关系表数据添加
	 * 
	 * @param business_id
	 *            主营业务ID
	 * @param declare_id
	 *            申报表ID
	 */
	public Object insertDeclareBusinessRel(String business_id, String declare_id);

	/**
	 * 申报_相关荣誉关系表数据添加
	 * 
	 * @param honor_id
	 *            荣誉ID
	 * @param honor_level
	 *            荣誉级别
	 * @param get_year
	 *            授予年份
	 * @param declare_id
	 *            申报ID
	 */
	public Object insertDeclareHonorRel(String honor_id, String honor_level, String get_year, String declare_id);

	/**
	 * 用户信息表数据修改
	 * 
	 * @param map
	 *            所有信息表数据
	 * @return
	 */
	public Object updateDeclareInfo(Map<String, Object> map);

	/**
	 * 申报_单位性质关系表数据修改
	 * 
	 * @param property_id
	 *            单位性质ID
	 * @param declare_id
	 *            申报表ID
	 */
	public Object updateDeclarePropertyRel(String property_id, String declare_id);
	
	/**
	 * 申报表数据新增或修改
	 * 
	 * @param request
	 * @return
	 */
	public JsonResult<Object> updateAllDatas(HttpServletRequest request);

}
