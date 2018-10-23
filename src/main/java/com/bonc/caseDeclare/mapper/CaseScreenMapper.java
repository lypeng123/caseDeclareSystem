package com.bonc.caseDeclare.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
*	案例筛选数据源
* @author zhijie.ma
* @date 2017年9月22日
* 
*/
@Mapper
public interface CaseScreenMapper {

	/**
	 * 案例筛选数据列表
	 * @param map
	 * @return
	 */
	List<Map<String,Object>> selectCasePrescreenResult(Map<String,Object> map);
	
//	/**
//	 * 案例筛选复审结果
//	 * @param map
//	 * @return
//	 */
//	List<Map<String,Object>> selectCaseRetrialResult(Map<String,Object> map);
//	
//	/**
//	 * 案例筛选终选或入选名单
//	 * @param map
//	 * @return
//	 */
//	List<Map<String,Object>> selectCaseFinalResult(Map<String,Object> map);
	
	/**
	 * 根据筛选条件查询数据总条数
	 * @param map
	 * @return
	 */
	Integer selectDataCount(Map<String,Object> map);
	
	/**
	 * 对案例进行筛选
	 * @param map
	 * @return
	 */
	boolean updateCaseState(Map<String,Object> map);
	
}
