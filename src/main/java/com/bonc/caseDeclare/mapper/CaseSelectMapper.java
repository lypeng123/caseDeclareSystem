package com.bonc.caseDeclare.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 *
 *	@author haixia.shi
 *	@date 2017年9月21日
 *
 */
@Mapper
public interface CaseSelectMapper {

	/**
	 * 查询筛选信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> selectCase(Map<String,Object> map);
	
	/**
	 * 查询信息数量
	 * @param map
	 * @return
	 */
	public Integer selectCount(Map<String,Object> map);
}
