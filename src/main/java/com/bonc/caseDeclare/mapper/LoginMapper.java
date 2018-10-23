package com.bonc.caseDeclare.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
*	登陆注册
* @author zhijie.ma
* @date 2017年9月8日
* 
*/
@Mapper
public interface LoginMapper {
	
	/**
	 * 添加用户信息
	 * @param map
	 */
	boolean insertUser(Map<String,Object> map);
	
	/**
	 * 查询用户信息
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> selectUser(Map<String,Object> map);
	
	/**
	 * 查询用户名最大编码+1
	 * @return
	 */
	Integer selectMaxUserCode();
	
	/**
	 * 添加用户名和密码等用户信息
	 * @param map
	 * @return
	 */
	boolean updateUser(Map<String,Object> map);
	
	/**
	 * 获取所有省份
	 * @return
	 */
	List<Map<String,Object>> selectAllProvince();
	
	/**
	 * 获取某个用户允许访问的页面
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> selectAllowUserMenu(Map<String,Object> map);
}
