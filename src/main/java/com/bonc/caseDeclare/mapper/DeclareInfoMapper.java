package com.bonc.caseDeclare.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author haixia.shi
 * @date 2017年9月9日
 *
 */
@Mapper
public interface DeclareInfoMapper {

	/**
	 * 获取所有的企业性质
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getProperty();

	/**
	 * 获取所有的主营业务
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getbusiness();

	/**
	 * 获取所有的荣誉
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getHonor();

	/**
	 * 查询申报ID
	 * 
	 * @param user_id
	 *            用户ID
	 * @return
	 */
	public String declareId(@Param("user_id") String user_id);

	/**
	 * 申报信息时获取用户注册信息
	 * 
	 * @param user_id
	 *            用户ID
	 * @return
	 */
	public Map<String, Object> getUserInfo(@Param("user_id") String user_id);

	/**
	 * 获取性质ID
	 * 
	 * @param declare_id
	 *            申报ID
	 * @return
	 */
	public String selectPropertyId(@Param("declare_id") String declare_id);

	/**
	 * 修改申报信息时获取用户注册信息
	 * 
	 * @param user_id
	 *            用户ID
	 * @return
	 */
	public Map<String, Object> getUserInfo2(@Param("user_id") String user_id, @Param("property_id") String property_id);

	/**
	 * 获取关联的主营业务数据
	 * 
	 * @param declare_id
	 *            申报ID
	 * @return
	 */
	public List<Map<String, Object>> getbusinessInfo(@Param("declare_id") String declare_id);

	/**
	 * 获取关联的荣誉数据
	 * 
	 * @param declare_id
	 * @return
	 */
	public List<Map<String, Object>> getHonorInfo(@Param("declare_id") String declare_id);

	/**
	 * 用户信息表数据修改
	 * 
	 * @param map
	 *            所有用户表添加数据
	 */
	public boolean updateUserInfo(Map<String, Object> map);

	/**
	 * 申报信息表数据添加
	 * 
	 * @param map
	 *            所有申报表添加数据
	 */
	public boolean insertDeclareInfo(Map<String, Object> map);

	/**
	 * 用户_申报关系表数据添加
	 * 
	 * @param user_id
	 *            用户ID
	 * @param declare_id
	 *            申报表ID
	 */
	public boolean insertUserDeclareRel(@Param("user_id") String user_id, @Param("declare_id") String declare_id);

	/**
	 * 申报_单位性质关系表数据添加
	 * 
	 * @param property_id
	 *            单位性质ID
	 * @param declare_id
	 *            申报表ID
	 */
	public boolean insertDeclarePropertyRel(@Param("property_id") String property_id,
			@Param("declare_id") String declare_id);

	/**
	 * 申报_主营业务关系表数据添加
	 * 
	 * @param business_id
	 *            主营业务ID
	 * @param declare_id
	 *            申报表ID
	 */
	public boolean insertDeclareBusinessRel(@Param("business_id") String business_id,
			@Param("declare_id") String declare_id);

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
	public boolean insertDeclareHonorRel(@Param("honor_id") String honor_id, @Param("honor_level") String honor_level,
			@Param("get_year") String get_year, @Param("declare_id") String declare_id);

	/**
	 * 申报信息表数据修改
	 * 
	 * @param map
	 *            所有申报表添加数据
	 */
	public boolean updateDeclareInfo(Map<String, Object> map);

	/**
	 * 申报_单位性质关系表数据修改
	 * 
	 * @param property_id
	 *            单位性质ID
	 * @param declare_id
	 *            申报表ID
	 */
	public boolean updateDeclarePropertyRel(@Param("property_id") String property_id,
			@Param("declare_id") String declare_id);

	/**
	 * 删除关于主营业务的选择
	 * 
	 * @param declare_id
	 * @return
	 */
	public boolean deletebusiness(@Param("declare_id") String declare_id);

	/**
	 * 删除关于荣誉的选择
	 * 
	 * @param declare_id
	 *            申报ID
	 */
	public boolean deleteHonor(@Param("declare_id") String declare_id);

	/**
	 * 删除关于性质的选择
	 * 
	 * @param declare_id
	 *            申报ID
	 */
	public boolean deleteProperty(@Param("declare_id") String declare_id);

	/**
	 *	根据申报年限查询该年所有的申报记录
	 */
	public List<Map<String, Object>> selectDeclareInfoByDeclareTime(@Param("declare_time") Integer declare_time,@Param("next_declare_time") Integer next_declare_time);

}
