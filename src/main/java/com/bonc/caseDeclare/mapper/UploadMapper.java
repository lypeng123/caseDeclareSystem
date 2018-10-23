package com.bonc.caseDeclare.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
*	上传相关信息
* @author zhijie.ma
* @date 2017年9月8日
* 
*/
@Mapper
public interface UploadMapper {
	
	/**
	 * 添加上传产品文件信息
	 * @param map
	 * @return
	 */
	boolean insertUploadProductInfo(Map<String,Object> map);
	
	/**
	 * 添加上传方案文件信息
	 * @param map
	 * @return
	 */
	boolean insertUploadPlanInfo(Map<String,Object> map);
	
	/**
	 * 获取上传产品文件
	 * @param declare_id
	 * @return
	 */
	public List<Map<String,Object>> selectProductFile(@Param("declare_id")String declare_id);
	
	/**
	 * 获取上传方案文件
	 * @param declare_id
	 * @return
	 */
	public List<Map<String,Object>> selectPlanFile(@Param("declare_id")String declare_id);
	
	/**
	 * 查询领域数据
	 * @return
	 */
	public List<Map<String,Object>> selectField();
	
	/**
	 * 删除上传的产品文件(真删)
	 * @param product_id	产品文件ID
	 * @return
	 */
	boolean deleteProductWord(@Param("product_id")String product_id);
	
	/**
	 * 删除上传的方案文件(真删)
	 * @param plan_id	方案文件ID
	 * @return
	 */
	boolean deletePlanWord(@Param("plan_id")String plan_id);
	
	/**
	 * 删除筛选表数据
	 * @param company_name	公司名称
	 * @param file_name	重命名后的文件名
	 * @return
	 */
	boolean deleteScreenTable(@Param("company_name")String company_name,@Param("file_name")String file_name);
	
	/**
	 * 查询文件号最大编码+1
	 * @return
	 */
	public int selectMaxFileCode();
	
	/**
	 * 筛选表插入数据
	 * @param map
	 * @return
	 */
	boolean insertScreenTable(Map<String,Object> map);
	
	/**
	 * 删除产品文件(假删)
	 * 
	 * @param product_id
	 *            产品文件ID
	 * @return
	 */
	public boolean updateDeleteProductWord(@Param("product_id") String product_id);

	/**
	 * 删除方案文件(假删)
	 * 
	 * @param plan_id
	 *            方案文件ID
	 * @return
	 */
	public boolean updateDeletePlanWord(@Param("plan_id") String plan_id);
	
	/**
	 * 查看状态是否存在不可操作项
	 * @param declare_id	申报表ID
	 * @return
	 */
	public List<Map<String,Object>> checkState(@Param("declare_id")String declare_id);

}
