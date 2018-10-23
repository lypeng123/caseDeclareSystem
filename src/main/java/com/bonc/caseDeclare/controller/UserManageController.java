package com.bonc.caseDeclare.controller;

import com.bonc.caseDeclare.service.DeclareInfoService;
import com.bonc.caseDeclare.service.HistoricalCaseService;
import com.bonc.caseDeclare.service.UserManageService;
import com.bonc.util.IPvalidateUtil;
import com.bonc.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/userManage")
public class UserManageController {

    private static final Logger logger = LoggerFactory.getLogger(UserManageController.class);

    @Autowired
    private UserManageService userManageService;

    /**
     * 获取用户信息列表
     * @param req
     * @return
     */
    @RequestMapping("/getUserInfo")
    public JsonResult<Object> getgetUserInfoResult(HttpServletRequest req){
        String ipAddr = IPvalidateUtil.getIpAddr(req).split(",")[0];// 访问的ip
        logger.info("IP为：【"+ipAddr+"】获取了用户信息列表");

        //JsonResult<Object> userResult = userManageService.getAllUser(req);
        JsonResult<Object> userResult = userManageService.allDeclareCompanyAccount(req);
        logger.info("用户信息查询结果："+userResult);

        return userResult;
    }

    /**
     * 查询企业信息
     */
    @RequestMapping("/allDeclareCompanyAccount")
    public JsonResult<Object> allDeclareCompanyAccount(HttpServletRequest request) {
        return userManageService.allDeclareCompanyAccount(request);

    }

    /**
     * 根据用户id查询用户信息
     * @param request
     * @return
     */
    @RequestMapping("/findUserById")
    public JsonResult<Object> findUserById(HttpServletRequest request,String user_id){
        String ipAddr = IPvalidateUtil.getIpAddr(request).split(",")[0];// 访问的ip
        logger.info("IP为：【"+ipAddr+"】查询了用户"+user_id+"的用户信息");
        JsonResult<Object> userById = userManageService.findUserById(user_id);
        logger.info("用户"+user_id+"的用户信息："+userById);
        return userById;
    }

    /**
     * 修改用户信息
     * @param request
     * @param user_id
     * @return
     */
    @RequestMapping("/updateUserById")
    public JsonResult<Object> updateById(HttpServletRequest request,String user_id){
        String ipAddr = IPvalidateUtil.getIpAddr(request).split(",")[0];// 访问的ip
        logger.info("IP为：【"+ipAddr+"】修改了用户"+user_id+"的用户信息");
        return userManageService.updateUserById(request,user_id,ipAddr);
    }

    /**
     * 删除用户信息
     * @param request
     * @param user_id
     * @return
     */
    @RequestMapping("/deleteUserById")
    public JsonResult<Object> deleteUserById(HttpServletRequest request,String user_id){
        String ipAddr = IPvalidateUtil.getIpAddr(request).split(",")[0];// 访问的ip
        logger.info("IP为：【"+ipAddr+"】修改了用户"+user_id+"的用户信息");
        return userManageService.deleteUserById(user_id);
    }

    /**
     * 根据用户id该用户所有案例列表，查询案例时调用
     * @param request
     * @param user_id
     * @return
     */
    @RequestMapping("/findCaseByUserId")
    public JsonResult<Object> findCaseByUserId(HttpServletRequest request,String user_id){
        String ipAddr = IPvalidateUtil.getIpAddr(request).split(",")[0];// 访问的ip
        logger.info("IP为：【"+ipAddr+"】查看了用户"+user_id+"的案例信息");
        return userManageService.findCaseByUserId(user_id);
    }

    /**
     * 管理员更新审核状态
     * @param request
     * @param screen_id 案例表ID
     * @return
     */
    @RequestMapping("/updateAuditState")
    public JsonResult<Object> updateAuditState(HttpServletRequest request, @PathVariable("screen_id") String screen_id, @PathVariable("audit_state") String audit_state){
        String ipAddr = IPvalidateUtil.getIpAddr(request).split(",")[0];// 访问的ip
        logger.info("IP为：【"+ipAddr+"】更新了案例"+screen_id+"的审核状态");
        return userManageService.updateAuditState(screen_id,audit_state);
    }

    /**
     * 获取用户所有资料
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/allDatas")
    public JsonResult<Object> allDatas(HttpServletRequest request, HttpServletResponse response) {

        String user_id = request.getParameter("user_id");

        if (user_id == null || user_id.equals("")) {
            logger.info("获取所有数据：参数有误");
            return new JsonResult<>("参数有误！");
        }

        Object allDatas = userManageService.allDatas(user_id);

        return new JsonResult<>(allDatas);
    }
}
