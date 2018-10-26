package com.bonc.caseDeclare.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *	用户管理数据源
 * @author qiushi.liu
 * @date 2018年10月19日
 *
 */
@Mapper
public interface UserManageMapper {

    /**
     * 获取用户信息
     * @return
     */
    public List<Map<String, Object>> getAllUser(Map<String, Object> map);

    /**
     * 根据账号注册时间查询该年所有的单位账号信息
     * @param map
     * @return
     */
    List<Map<String,Object>> allDeclareCompanyAccount(Map<String,Object> map);

    /**
     * 查询该年所有的注册单位账号信息
     * @param map
     * @return
     */
    Integer selectCount(Map<String,Object> map);

    /**
     * 删除用户
     * @param user_id
     * @return
     */
    boolean deleteUserById(@Param("user_id") String user_id);

    /**
     * 根据用户ID查询该用户对应案例列表
     * @param user_id
     * @return
     */
    List<Map<String,Object>> findCaseByUserId(@Param("user_id") String user_id);

    /**
     * 修改用户时得到用户用户信息
     * @param user_id
     * @return
     */
    Object getUserInfo(@Param("user_id") String user_id);

    /**
     * 修改用户时得到用户信息
     * @param user_id
     * @param property_id
     * @return
     */
    Object getUserInfo2(@Param("user_id") String user_id, @Param("property_id")String property_id);

    /**
     * 获取申报信息id
     * @param user_id
     * @return
     */
    String declareId(@Param("user_id") String user_id);


    /**
     * 查询申报信息
     * @param declare_id
     * @return
     */
    String selectPropertyId(@Param("declare_id") String declare_id);

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

    /**
     * 根据用户申报id查找方案id
     * @param declare_id
     * @return
     */
    String findPlanId(@Param("declare_id") String declare_id);
}
