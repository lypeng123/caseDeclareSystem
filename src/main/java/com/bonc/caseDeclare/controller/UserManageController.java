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
     * 删除用户信息
     * @param request
     * @param user_id
     * @return
     */
    @RequestMapping("/deleteUserById")
    public JsonResult<Object> deleteUserById(HttpServletRequest request,String user_id){
        String ipAddr = IPvalidateUtil.getIpAddr(request).split(",")[0];// 访问的ip
        logger.info("IP为：【"+ipAddr+"】删除了用户"+user_id+"的用户信息");
        return userManageService.deleteUserById(user_id);
    }

    /**
     * 根据用户id该用户所有案例列表，查询案例时调用
     * @param request
     * @return
     */
    @RequestMapping("/findCaseByUserId")
    public JsonResult<Object> findCaseByUserId(HttpServletRequest request){
        String user_id =(String) request.getSession().getAttribute("userId");

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

        //String user_id = request.getParameter("userId");
        //String user_id = (String) request.getAttribute("userId");
        String user_id =(String) request.getSession().getAttribute("userId");

        if (user_id == null || user_id.equals("")) {
            logger.info("获取所有数据：参数有误");
            return new JsonResult<>("参数有误！");
        }

        Object allDatas = userManageService.allDatas(user_id);

        return new JsonResult<>(allDatas);
    }

    @RequestMapping("/setUserIdUrl")
    public JsonResult<Object> setUserIdUrl(HttpServletRequest request){
        String userId = request.getParameter("userId");
        request.getSession().setAttribute("userId", userId);
        return new JsonResult<>(JsonResult.SUCCESS,"成功存入");
    }

    /**
     * 获取关联的主营业务数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/businessInfoDatas")
    public JsonResult<Object> businessInfoDatas(HttpServletRequest request, HttpServletResponse response) {
        String user_id =(String) request.getSession().getAttribute("userId");

        return userManageService.getBusinessInfo(user_id);

    }

    /**
     * 获取关联的荣誉数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/honorInfoDatas")
    public JsonResult<Object> honorInfoDatas(HttpServletRequest request, HttpServletResponse response) {
        String user_id =(String) request.getSession().getAttribute("userId");

        return userManageService.getHonorInfo(user_id);
    }

    /**
     * 管理员修改用户界面保存按钮的操作
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/opeationDatas")
    //@Async("mqExecutor")
    public JsonResult<Object> opeationDatas(HttpServletRequest request, HttpServletResponse response) {
        return userManageService.updateAllDatas(request);

    }
}
