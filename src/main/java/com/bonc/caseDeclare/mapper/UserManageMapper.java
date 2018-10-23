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
     * 根据id查询账户信息
     * @param user_id
     * @return
     */
    Map<String,Object> findUserById(@Param("user_id") String user_id);

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
}
