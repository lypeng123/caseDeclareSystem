package com.bonc.caseDeclare.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bonc.util.JsonResult;

/**
 * 用户管理
 * @author qiushi.liu
 * @date 2018年10月19日
 *
 */
public interface UserManageService {
    /**
     * 获取所有用户信息
     * @return
     */
    JsonResult<Object> getAllUser(HttpServletRequest req);

    /**
     * 得到所有申报单位账号
     * @param request
     * @return
     */
    JsonResult<Object> allDeclareCompanyAccount(HttpServletRequest request);

    /**
     * 根据id删除用户信息
     * @param user_id
     * @return
     */
    JsonResult<Object> deleteUserById(String user_id);

    /**
     * 根据用户id查询该用户所有案例
     * @param user_id
     * @return
     */
    JsonResult<Object> findCaseByUserId(String user_id);

    /**
     * 根据案例id更新审核状态
     * @param screen_id
     * @return
     */
    JsonResult<Object> updateAuditState(String screen_id,String audit_state);

    /**
     * 根据用户id查询所有资料
     * @param user_id
     * @return
     */
    Object allDatas(String user_id);

    /**
     * 根据用户id获取主营业务
     * @param user_id
     * @return
     */
    JsonResult<Object> getBusinessInfo(String user_id);

    /**
     * 根据用户id获取相关荣誉
     * @param user_id
     * @return
     */
    JsonResult<Object> getHonorInfo(String user_id);

    /**
     * 申报表数据新增或修改
     *
     * @param request
     * @return
     */
    public JsonResult<Object> updateAllDatas(HttpServletRequest request);
}
