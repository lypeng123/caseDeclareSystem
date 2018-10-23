package com.bonc.caseDeclare.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 *
 * @author haixia.shi
 * @date 2017年9月9日
 * DeclareInfoMapper
 *
 */
@Mapper
public interface HistoricalCaseMapper {

	/**
	 *	根据申报年限查询该年所有的申报记录
	 */
	public List<Map<String, Object>> selectDeclareInfoByDeclareTime(Map<String,Object> map);
}
