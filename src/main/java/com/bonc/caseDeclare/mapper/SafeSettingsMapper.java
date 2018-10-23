package com.bonc.caseDeclare.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
*	安全设置
* @author zhijie.ma
* @date 2017年9月20日
* 
*/
@Mapper
public interface SafeSettingsMapper {
	
	/**
	 * 修改用户名密码
	 * @param map
	 * @return
	 */
	boolean updateUserPassword(Map<String,Object> map);
	
}
